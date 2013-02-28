/*
 * Copyright (c) 2008, intarsys consulting GmbH
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
package de.intarsys.cwt.image;

import java.io.IOException;
import java.lang.ref.SoftReference;

import de.intarsys.tools.attribute.AttributeMap;

public abstract class ImageEntry implements IImageEntry {

	final private IImageContainer container;

	final private AttributeMap attributes = new AttributeMap();

	private SoftReference delegate;

	protected ImageEntry(IImageContainer container) {
		super();
		this.container = container;
	}

	final public Object getAttribute(Object key) {
		return attributes.get(key);
	}

	public IImageContainer getContainer() {
		return container;
	}

	synchronized protected IImage getDelegate() {
		IImage image = null;
		if (delegate != null) {
			image = (IImage) delegate.get();
		}
		if (image == null) {
			try {
				image = loadContent();
			} catch (Exception e) {
				image = getErrorImage(e);
			}
			delegate = new SoftReference(image);
		}
		return image;
	}

	abstract protected IImage getErrorImage(Exception e);

	public int getHeight() {
		return getDelegate().getHeight();
	}

	public int getWidth() {
		return getDelegate().getWidth();
	}

	abstract protected IImage loadContent() throws IOException;

	final public Object removeAttribute(Object key) {
		return attributes.remove(key);
	}

	final public Object setAttribute(Object key, Object o) {
		return attributes.put(key, o);
	}

}
