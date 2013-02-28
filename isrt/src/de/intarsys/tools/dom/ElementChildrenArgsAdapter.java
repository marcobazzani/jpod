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
package de.intarsys.tools.dom;

import java.util.Collections;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

import org.w3c.dom.Element;

import de.intarsys.tools.functor.ArgTools;
import de.intarsys.tools.functor.IArgs;

public class ElementChildrenArgsAdapter implements IArgs {

	protected class Binding implements IBinding {

		private int index;

		public Binding(int index) {
			super();
			this.index = index;
		}

		@Override
		public String getName() {
			return null;
		}

		@Override
		public Object getValue() {
			return ElementChildrenArgsAdapter.this.get(index);
		}

		@Override
		public boolean isDefined() {
			return false;
		}

		@Override
		public void setValue(Object value) {
			ElementChildrenArgsAdapter.this.put(index, value);
		}

	}

	final private Element[] children;

	public ElementChildrenArgsAdapter(Element[] children) {
		super();
		this.children = children;
	}

	public IBinding add(Object object) {
		throw new UnsupportedOperationException("can't write"); //$NON-NLS-1$
	}

	@Override
	public Iterator<IBinding> bindings() {
		return new Iterator<IBinding>() {
			private int index = 0;

			@Override
			public boolean hasNext() {
				return index < children.length;
			}

			@Override
			public IBinding next() {
				if (index < children.length) {
					return new Binding(index++);
				} else {
					throw new NoSuchElementException();
				}
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}
		};
	}

	public void clear() {
		throw new UnsupportedOperationException("can't write"); //$NON-NLS-1$
	}

	@Override
	public IArgs copy() {
		return new ElementChildrenArgsAdapter(children);
	}

	@Override
	public IBinding declare(final String name) {
		return new Binding(0);
	}

	public Object get(int index) {
		if (index < 0 || index >= children.length) {
			return null;
		}
		return new ElementArgsAdapter(children[index]);
	}

	public Object get(int index, Object defaultValue) {
		if (index < 0 || index >= children.length) {
			return defaultValue;
		}
		return new ElementArgsAdapter(children[index]);
	}

	public Object get(String name) {
		// not supported
		return null;
	}

	public Object get(String name, Object defaultValue) {
		// not supported
		return defaultValue;
	}

	public boolean isDefined(String name) {
		// not supported
		return false;
	}

	public Set names() {
		// not supported
		return Collections.EMPTY_SET;
	}

	public IBinding put(int index, Object value) {
		throw new UnsupportedOperationException("can't write to ArgsAdapter"); //$NON-NLS-1$
	}

	public IBinding put(String name, Object value) {
		throw new UnsupportedOperationException("can't write to ArgsAdapter"); //$NON-NLS-1$
	}

	public int size() {
		return children.length;
	}

	@Override
	public String toString() {
		return ArgTools.toString(this, ""); //$NON-NLS-1$
	}

	public void undefine(int index) {
		throw new UnsupportedOperationException("can't write to ArgsAdapter"); //$NON-NLS-1$
	}

	public void undefine(String name) {
		throw new UnsupportedOperationException("can't write to ArgsAdapter"); //$NON-NLS-1$
	}

}
