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
package de.intarsys.nativec.jna;

import java.util.ArrayList;
import java.util.List;

import com.sun.jna.FastMemory;
import com.sun.jna.Function;
import com.sun.jna.Native;
import com.sun.jna.Pointer;

import de.intarsys.nativec.api.ICallback;
import de.intarsys.nativec.api.INativeCallback;
import de.intarsys.nativec.api.INativeFunction;
import de.intarsys.nativec.api.INativeHandle;
import de.intarsys.nativec.api.INativeInterface;
import de.intarsys.nativec.api.INativeLibrary;

/**
 * An {@link INativeInterface} implemented using JNA, a LGPL licensed Java
 * native interface abstraction.
 * <p>
 * In our point of view, JNA has the power of deploying all what we wanted to
 * have, but is ill designed in some key hot spots - so we worked around and
 * built on top of our own interfaces.
 * 
 */
public class JnaNativeInterface implements INativeInterface {

	static {
		/*
		 * Ugly hack - multithreaded loading caused a deadlock here as
		 * com.sun.jna.Native requests a native library which later requires a
		 * lock on the class loader, while another piece of code
		 * (Security.addProvider) first locks the class loader and later on
		 * requests the runtime to load a native library. <p> We try here to
		 * provide the same ordering (which is based on a lot of assumptions...)
		 */
		ClassLoader loader = JnaNativeInterface.class.getClassLoader();
		synchronized (loader) {
			try {
				Class.forName("com.sun.jna.Native", true, loader);
			} catch (ClassNotFoundException e) {
				// will fail later anyway
			}
		}
	}

	private List<String> searchPaths = new ArrayList<String>();

	public void addSearchPath(String path) {
		if (searchPaths.contains(path)) {
			return;
		}
		searchPaths.add(path);
	}

	public INativeHandle allocate(int size) {
		return new JnaNativeHandle(new FastMemory(size));
	}

	public INativeCallback createCallback(ICallback callback) {
		if (callback == null) {
			return null;
		}
		if (callback.getCallingConvention() == ICallback.CallingConventionStdcall) {
			return new JnaNativeCallbackAlt(callback);
		}
		if (callback.getCallingConvention() == ICallback.CallingConventionCdecl) {
			return new JnaNativeCallbackStd(callback);
		}
		throw new IllegalArgumentException("illegal calling convention");
	}

	public INativeFunction createFunction(long address) {
		Pointer pointer;
		Function function;

		pointer = new Pointer(address);
		function = Function.getFunction(pointer);
		return new JnaNativeFunction(function);
	}

	public INativeHandle createHandle(long address) {
		return new JnaNativeHandle(address);
	}

	public INativeLibrary createLibrary(String name) {
		return new JnaNativeLibrary(this, name);
	}

	protected List<String> getSearchPaths() {
		return searchPaths;
	}

	public int longSize() {
		return Native.LONG_SIZE;
	}

	public int pointerSize() {
		return Native.POINTER_SIZE;
	}

	public int wideCharSize() {
		return 2;
	}
}
