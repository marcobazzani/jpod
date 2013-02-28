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
package de.intarsys.tools.locator;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.net.URL;

import de.intarsys.tools.adapter.AdapterTools;
import de.intarsys.tools.adapter.IAdapterSupport;
import de.intarsys.tools.randomaccess.IRandomAccess;

/**
 * An adapter for implementing a delegation model.
 * 
 */
abstract public class DelegatingLocator implements ILocator, IAdapterSupport {

	protected ILocator delegate;

	public DelegatingLocator(ILocator delegate) {
		super();
		this.delegate = delegate;
	}

	public void delete() throws IOException {
		getDelegate().delete();
	}

	public boolean exists() {
		try {
			return getDelegate().exists();
		} catch (IOException e) {
			return false;
		}
	}

	public <T> T getAdapter(Class<T> clazz) {
		try {
			return AdapterTools.getAdapter(getDelegate(), clazz);
		} catch (IOException e) {
			return null;
		}
	}

	public ILocator getChild(String name) {
		try {
			return getDelegate().getChild(name);
		} catch (IOException e) {
			return null;
		}
	}

	public ILocator getDelegate() throws IOException {
		return delegate;
	}

	public String getFullName() {
		try {
			return getDelegate().getFullName();
		} catch (IOException e) {
			return "";
		}
	}

	public InputStream getInputStream() throws IOException {
		return getDelegate().getInputStream();
	}

	public long getLength() throws IOException {
		return getDelegate().getLength();
	}

	public String getLocalName() {
		try {
			return getDelegate().getLocalName();
		} catch (IOException e) {
			return "";
		}
	}

	public OutputStream getOutputStream() throws IOException {
		return getDelegate().getOutputStream();
	}

	public ILocator getParent() {
		try {
			return getDelegate().getParent();
		} catch (IOException e) {
			return null;
		}
	}

	public IRandomAccess getRandomAccess() throws IOException {
		return getDelegate().getRandomAccess();
	}

	public Reader getReader() throws IOException {
		return getDelegate().getReader();
	}

	public Reader getReader(String encoding) throws IOException {
		return getDelegate().getReader(encoding);
	}

	public String getType() {
		try {
			return getDelegate().getType();
		} catch (IOException e) {
			return "";
		}
	}

	public String getTypedName() {
		try {
			return getDelegate().getTypedName();
		} catch (IOException e) {
			return "";
		}
	}

	public Writer getWriter() throws IOException {
		return getDelegate().getWriter();
	}

	public Writer getWriter(String encoding) throws IOException {
		return getDelegate().getWriter(encoding);
	}

	public boolean isDirectory() {
		try {
			return getDelegate().isDirectory();
		} catch (IOException e) {
			return false;
		}
	}

	public boolean isOutOfSynch() {
		try {
			return getDelegate().isOutOfSynch();
		} catch (IOException e) {
			return false;
		}
	}

	public boolean isReadOnly() {
		try {
			return getDelegate().isReadOnly();
		} catch (IOException e) {
			return true;
		}
	}

	public ILocator[] listLocators(ILocatorNameFilter filter)
			throws IOException {
		return getDelegate().listLocators(filter);
	}

	public void rename(String newName) throws IOException {
		getDelegate().rename(newName);
	}

	public void setReadOnly() {
		try {
			getDelegate().setReadOnly();
		} catch (IOException e) {
			//
		}
	}

	public void synch() {
		try {
			getDelegate().synch();
		} catch (IOException e) {
			//
		}
	}

	public URL toURL() {
		try {
			return getDelegate().toURL();
		} catch (IOException e) {
			return null;
		}
	}
}
