/*
 * Copyright (c) 2012, intarsys consulting GmbH
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
package de.intarsys.tools.ui;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.SwingUtilities;

import de.intarsys.tools.event.AttributeChangedEvent;
import de.intarsys.tools.event.Event;
import de.intarsys.tools.event.INotificationListener;
import de.intarsys.tools.event.INotificationObserver;
import de.intarsys.tools.event.INotificationSupport;
import de.intarsys.tools.functor.IArgs;
import de.intarsys.tools.functor.IArgsConfigurable;

/**
 * Abstraction of an user interface component.
 * <p>
 * The {@link UIComponent} spans a hierarchical tree. Each {@link UIComponent}
 * maps to a real toolkit interface component that gets realized upon calling
 * "createComponent".
 * <p>
 * A {@link UIComponent} is associated with a model object via the
 * {@link INotificationObserver} interface.
 * 
 * @param <M>
 *            The model object
 * @param <C>
 *            The toolkit container class
 * @param <T>
 *            The toolkit component class
 */
abstract public class UIComponent<M extends INotificationSupport, C, T>
		implements IUIComponent<M, C, T>, IArgsConfigurable {

	private C container;

	final private IUIComponent parent;

	private T component;

	private M model;

	private static final Logger Log = PACKAGE.Log;

	private INotificationListener listenModelChange = new INotificationListener() {
		@Override
		public void handleEvent(final Event event) {
			modelChanged(event);
		}
	};

	private IArgs configuration;

	private boolean disposed = false;

	private boolean componentCreated = false;

	public UIComponent() {
		this(null);
	}

	public UIComponent(IUIComponent<? extends INotificationSupport, C, T> parent) {
		super();
		this.parent = parent;
	}

	abstract protected T basicCreateComponent(C parent);

	public void configure(de.intarsys.tools.functor.IArgs args)
			throws de.intarsys.tools.functor.ArgsConfigurationException {
		configuration = args;
	}

	@Override
	final public void createComponent(C parent) {
		setContainer(parent);
		T tempComponent = basicCreateComponent(parent);
		setComponent(tempComponent);
		componentCreated = true;
		updateView(null);
	}

	public void dispose() {
		if (disposed) {
			return;
		}
		disposed = true;
		componentCreated = false;
		setObservable(null);
	}

	final public T getComponent() {
		return component;
	}

	public IArgs getConfiguration() {
		return configuration;
	}

	protected C getContainer() {
		return container;
	}

	@Override
	synchronized public M getObservable() {
		return model;
	}

	public IUIComponent getParent() {
		return parent;
	}

	protected boolean isComponentCreated() {
		return componentCreated;
	}

	public boolean isDisposed() {
		return disposed;
	}

	protected void modelChanged(final Event event) {
		if (getObservable() == null) {
			// out of date....
			return;
		}
		// try to prevent deadlock situations by queuing invocation
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					if (getObservable() == null) {
						// at least now - out of date....
						return;
					}
					if (!isComponentCreated()) {
						return;
					}
					updateView(event);
				} catch (Exception e) {
					Log.log(Level.WARNING, "unexpeced error in updateView", e);
				}
			}

		});
	}

	protected void setComponent(T component) {
		this.component = component;
	}

	protected void setComponentCreated(boolean componentAvailable) {
		this.componentCreated = componentAvailable;
	}

	protected void setContainer(C parent) {
		this.container = parent;
	}

	@Override
	synchronized public void setObservable(M observable) {
		if (model == observable) {
			return;
		}
		basicSetModel(observable);
		if (model != null) {
			modelChanged(new AttributeChangedEvent(model, null, null, null));
		}
	}

	protected void basicSetModel(M observable) {
		if (model != null) {
			model.removeNotificationListener(AttributeChangedEvent.ID,
					listenModelChange);
		}
		model = observable;
		if (model != null) {
			model.addNotificationListener(AttributeChangedEvent.ID,
					listenModelChange);
		}
	}

	protected void updateView(Event e) {
	}

}
