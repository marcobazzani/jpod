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

import org.w3c.dom.Attr;
import org.w3c.dom.Element;

import de.intarsys.tools.functor.ArgTools;
import de.intarsys.tools.functor.IArgs;

public class ElementArgsAdapter implements IArgs {

	protected class Binding implements IBinding {
		private String name;

		public Binding(String name) {
			super();
			this.name = name;
		}

		@Override
		public String getName() {
			return name;
		}

		@Override
		public Object getValue() {
			return ElementArgsAdapter.this.get(name);
		}

		@Override
		public boolean isDefined() {
			return ElementArgsAdapter.this.isDefined(name);
		}

		@Override
		public void setValue(Object value) {
			ElementArgsAdapter.this.put(name, value);
		}

	}

	private final Element element;

	public ElementArgsAdapter(Element element) {
		super();
		this.element = element;
	}

	public IBinding add(Object object) {
		throw new UnsupportedOperationException("can't write"); //$NON-NLS-1$
	}

	@Override
	public Iterator<IBinding> bindings() {
		return new Iterator<IBinding>() {
			private Iterator elements = DOMTools.getElementIterator(element);
			private Iterator attributes = DOMTools
					.getAttributeIterator(element);

			@Override
			public boolean hasNext() {
				return elements.hasNext() || attributes.hasNext();
			}

			@Override
			public IBinding next() {
				IBinding binding;
				if (elements.hasNext()) {
					Element element = (Element) elements.next();
					return new Binding(element.getTagName());
				} else if (attributes.hasNext()) {
					Attr attribute = (Attr) attributes.next();
					return new Binding(attribute.getName());
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
		return new ElementArgsAdapter(element);
	}

	@Override
	public IBinding declare(final String name) {
		return new Binding(name);
	}

	public Object get(int index) {
		// not supported
		return null;
	}

	public Object get(int index, Object defaultValue) {
		return defaultValue;
	}

	public Object get(String name) {
		Attr result = element.getAttributeNode(name);
		if (result == null) {
			Element[] children = DOMTools.getElements(element, name);
			if (children != null && children.length > 0) {
				return new ElementChildrenArgsAdapter(children);
			}
			return null;
		}
		return result.getValue();
	}

	public Object get(String name, Object defaultValue) {
		Attr result = element.getAttributeNode(name);
		if (result == null) {
			Element[] children = DOMTools.getElements(element, name);
			if (children != null && children.length > 0) {
				return new ElementChildrenArgsAdapter(children);
			} else {
				return defaultValue;
			}
		}
		return result.getValue();
	}

	public boolean isDefined(String name) {
		return get(name) != null;
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
		return 0;
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
