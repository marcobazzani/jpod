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
package de.intarsys.cwt.freetype.nativec;

import de.intarsys.nativec.api.INativeHandle;
import de.intarsys.nativec.type.INativeObject;
import de.intarsys.nativec.type.INativeType;
import de.intarsys.nativec.type.NativeStaticStruct;
import de.intarsys.nativec.type.NativeStructType;

/**
 * A handle to a FreeType library instance. Each `library' is completely
 * independent from the others; it is the `root' of a set of objects like fonts,
 * faces, sizes, etc. It also embeds a memory manager (see FT_Memory), as well
 * as a scan-line converter object (see FT_Raster). note Library objects are
 * normally created by FT_Init_FreeType, and destroyed with FT_Done_FreeType.
 */
public class FTLibrary extends NativeStaticStruct {
	/**
	 * The meta class implementation
	 */
	public static class MetaClass extends NativeStructType {
		protected MetaClass(Class instanceClass) {
			super(instanceClass);
		}

		@Override
		public INativeObject createNative(INativeHandle handle) {
			return new FTLibrary(handle);
		}

	}

	/** The meta class instance */
	public static final MetaClass META = new MetaClass(MetaClass.class
			.getDeclaringClass());

	static {
		// nothing to declare here
	}

	/**
	 * 
	 */
	public FTLibrary() {
	}

	protected FTLibrary(INativeHandle handle) {
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

}
