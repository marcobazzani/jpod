/*
 * Copyright (c) 2007, intarsys consulting GmbH
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * - Redistributions of source code must retain the above copyright notice,
 *   this list of conditions and the following disclaimer.
 *
 * - Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * - Neither the name of intarsys nor the names of its contributors may be used
 *   to endorse or promote products derived from this software without specific
 *   prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package de.intarsys.tools.concurrent;

import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This is an alternate implementation for {@link FutureTask}, which is in some
 * cases not flexible enough.
 * 
 * @param <R>
 */
abstract public class AbstractFutureTask<R> implements Runnable, Future {

	protected final static Logger Log = PACKAGE.Log;

	private ITaskListener taskListener;

	static private int COUNTER = 0;

	final private Object lockTask = new Object();

	private Throwable exception;

	private R result;

	private boolean cancelled = false;

	private boolean computed = false;

	final private int id = COUNTER++;

	private boolean active = false;

	private boolean asynch = false;

	protected AbstractFutureTask() {
		super();
	}

	protected AbstractFutureTask(ITaskListener callback) {
		super();
		this.taskListener = callback;
	}

	protected Throwable basicGetException() {
		synchronized (lockTask) {
			return exception;
		}
	}

	protected R basicGetResult() {
		return result;
	}

	protected void basicRun() {
		R tempResult;
		try {
			tempResult = compute();
		} catch (Throwable e) {
			setException(e);
			return;
		}
		if (!isAsynch()) {
			setResult(tempResult);
		}
	}

	public boolean cancel(boolean interrupt) {
		synchronized (lockTask) {
			if (cancelled) {
				if (Log.isLoggable(Level.FINEST)) {
					Log.finest("" + this + " can't cancel, already canceled"); //$NON-NLS-1$
				}
				return false;
			}
			if (computed) {
				if (Log.isLoggable(Level.FINEST)) {
					Log.finest("" + this + " can't cancel, already computed"); //$NON-NLS-1$
				}
				return false;
			}
			if (Log.isLoggable(Level.FINEST)) {
				Log.finest("" + this + " cancel " + (active ? "active" : "inactive") + " task"); //$NON-NLS-1$
			}
			cancelled = true;
			active = false;
			lockTask.notifyAll();
		}
		taskCancelled();
		return true;
	}

	protected abstract R compute() throws Exception;

	public R get() throws InterruptedException, ExecutionException {
		try {
			return get(0, TimeUnit.MILLISECONDS);
		} catch (TimeoutException e) {
			throw new InterruptedException();
		}
	}

	/**
	 * Wait for the result. The "timeout" parameter allows to
	 * <ul>
	 * <li>wait indefinite</li> when timeout == 0;
	 * <li>wait specified duration</li> when timeout > 0;
	 * <li>don't wait (peek)</li> when timeout < 0;
	 * </ul>
	 * 
	 * @see java.util.concurrent.Future#get(long, java.util.concurrent.TimeUnit)
	 */
	public R get(final long timeout, TimeUnit unit)
			throws InterruptedException, ExecutionException, TimeoutException {
		long remainNanos = TimeUnit.NANOSECONDS.convert(timeout, unit);
		long lastNanos = System.nanoTime();
		synchronized (lockTask) {
			while (true) {
				if (cancelled) {
					throw new CancellationException();
				}
				if (computed) {
					if (exception != null) {
						throw new ExecutionException(exception);
					}
					// may be null!
					return result;
				}
				if (timeout >= 0) {
					if (timeout != 0) {
						long nowNanos = System.nanoTime();
						remainNanos -= nowNanos - lastNanos;
						lastNanos = nowNanos;
					}
					long tempMillis = TimeUnit.MILLISECONDS.convert(
							remainNanos, TimeUnit.NANOSECONDS);
					if (tempMillis <= 0 && timeout != 0) {
						throw new TimeoutException();
					}
					lockTask.wait(tempMillis);
				} else {
					// support a unblocked "get current value"
					return null;
				}
			}
		}
	}

	public Throwable getException() {
		synchronized (lockTask) {
			return exception;
		}
	}

	public ITaskListener getTaskListener() {
		synchronized (lockTask) {
			return taskListener;
		}
	}

	final protected void handleException() {
		try {
			if (cancelled) {
				if (Log.isLoggable(Level.FINEST)) {
					Log.finest("" + this + " exception after cancel"); //$NON-NLS-1$
				}
				undo();
			} else {
				if (Log.isLoggable(Level.FINEST)) {
					Log.finest("" + this + " exception, propagate"); //$NON-NLS-1$
				}
				taskFailed();
			}
		} catch (Exception e) {
			Log.log(Level.SEVERE,
					"" + this + " exception in exception handling", e); //$NON-NLS-1$
		} finally {
			handleFinally();
		}
	}

	final protected void handleFinally() {
		try {
			if (Log.isLoggable(Level.FINEST)) {
				Log.finest("" + this + " finally, propagate"); //$NON-NLS-1$
			}
			taskFinally();
		} catch (Exception e) {
			Log.log(Level.SEVERE,
					"" + this + " exception in finally handling", e); //$NON-NLS-1$
		}
	}

	final protected void handleResult() {
		try {
			if (cancelled) {
				if (Log.isLoggable(Level.FINEST)) {
					Log.finest("" + this + " computed after cancel, undo"); //$NON-NLS-1$
				}
				undo();
			} else {
				if (Log.isLoggable(Level.FINEST)) {
					Log.finest("" + this + " computed, propagate"); //$NON-NLS-1$
				}
				taskFinished();
			}
		} catch (Exception e) {
			Log.log(Level.SEVERE,
					"" + this + " exception in result handling", e); //$NON-NLS-1$
		} finally {
			handleFinally();
		}
	}

	public boolean isActive() {
		synchronized (lockTask) {
			return active;
		}
	}

	public boolean isAsynch() {
		return asynch;
	}

	public boolean isCancelled() {
		synchronized (lockTask) {
			return cancelled;
		}
	}

	public boolean isDone() {
		synchronized (lockTask) {
			return computed || cancelled;
		}
	}

	public boolean isFailed() {
		synchronized (lockTask) {
			return exception != null;
		}
	}

	public void reset() {
		synchronized (lockTask) {
			active = false;
			computed = false;
			exception = null;
			result = null;
		}
	}

	final public void run() {
		synchronized (lockTask) {
			if (active || cancelled || computed) {
				if (Log.isLoggable(Level.FINEST)) {
					Log.finest("" + this + " will not run" + (cancelled ? " (canceled)" : "")); //$NON-NLS-1$
				}
				return;
			}
			active = true;
		}
		taskStarted();
		basicRun();
	}

	final public void runAsync() {
		setAsynch(true);
		run();
	}

	public void setAsynch(boolean asynch) {
		this.asynch = asynch;
	}

	protected void setException(Throwable e) {
		synchronized (lockTask) {
			computed = true;
			active = false;
			exception = e;
			lockTask.notifyAll();
		}
		if (Log.isLoggable(Level.FINEST)) {
			Log.finest("" + this + " computation failed"); //$NON-NLS-1$
		}
		handleException();
	}

	protected void setResult(R object) {
		synchronized (lockTask) {
			computed = true;
			active = false;
			result = object;
			lockTask.notifyAll();
		}
		if (Log.isLoggable(Level.FINEST)) {
			Log.finest("" + this + " computation ready"); //$NON-NLS-1$
		}
		handleResult();
	}

	public void setTaskListener(ITaskListener taskListener) {
		synchronized (lockTask) {
			if (active || cancelled || computed) {
				throw new IllegalStateException("already started");
			}
			this.taskListener = taskListener;
		}
	}

	protected void taskCancelled() {
		if (taskListener != null) {
			taskListener.taskCancelled(this);
		}
	}

	protected void taskFailed() {
		if (taskListener != null) {
			taskListener.taskFailed(this, new ExecutionException(
					basicGetException()));
		}
	}

	protected void taskFinally() {
		// redefine
	}

	protected void taskFinished() {
		if (taskListener != null) {
			taskListener.taskFinished(this, basicGetResult());
		}
	}

	protected void taskStarted() {
		if (taskListener != null) {
			taskListener.taskStarted(this);
		}
	}

	@Override
	public String toString() {
		return getClass().getName() + " - " + id;
	}

	protected void undo() {
		// redefine
	}

}
