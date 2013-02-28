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
package de.intarsys.nativec.type;

import de.intarsys.nativec.api.INativeHandle;
import de.intarsys.nativec.api.NativeInterface;
import de.intarsys.nativec.api.NativeTools;

/**
 * An object representing "void" (nothing)
 */
public class NativeVoid extends NativeSimple {

	/** The meta class instance */
	public static final NativeVoidType META = new NativeVoidType();

	public static final NativeVoid NULL = (NativeVoid) META
			.createNative(NativeInterface.NULL);

	static {
		NativeType.register(NativeVoid.class, META);
	}

	static public NativeVoid createFromAddress(long address) {
		return (NativeVoid) NativeVoid.META.createNative(NativeTools
				.toHandle(address));
	}

	/**
	 * Create a new wrapper instance
	 */
	public NativeVoid() {
		allocate();
	}

	protected NativeVoid(INativeHandle handle) {
		super(handle);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.intarsys.graphic.freetype.NativeObject#getMetaClass()
	 */
	@Override
	public INativeType getNativeType() {
		return META;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.intarsys.tools.nativec.NativePrimitive#getValue()
	 */
	public Object getValue() {
		return null;
	}

	public void setValue(Object value) {
	}
}
