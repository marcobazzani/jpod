package de.intarsys.tools.lang;

import java.util.concurrent.Callable;

abstract public class CallableAdapter<T> implements Runnable, Callable<T> {

	private T result = null;

	private Exception exception;

	public T getResult() throws Exception {
		if (exception != null) {
			throw exception;
		}
		return result;
	}

	public T getResultUnchecked() {
		if (exception != null) {
			return null;
		}
		return result;
	}

	@Override
	public void run() {
		try {
			setResult(call());
		} catch (Exception e) {
			exception = e;
		}
	}

	protected void setResult(T result) {
		this.result = result;
	}
}
