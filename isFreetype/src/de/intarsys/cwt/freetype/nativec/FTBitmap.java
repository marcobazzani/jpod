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
import de.intarsys.nativec.type.NativeBuffer;
import de.intarsys.nativec.type.NativeByte;
import de.intarsys.nativec.type.NativeInt;
import de.intarsys.nativec.type.NativeNumber;
import de.intarsys.nativec.type.NativeShort;
import de.intarsys.nativec.type.NativeStaticStruct;
import de.intarsys.nativec.type.NativeStructType;
import de.intarsys.nativec.type.NativeVoid;

/**
 * 
 */
public class FTBitmap extends NativeStaticStruct {
	/**
	 * The meta class implementation
	 */
	public static class MetaClass extends NativeStructType {
		protected MetaClass(Class instanceClass) {
			super(instanceClass);
		}

		@Override
		public INativeObject createNative(INativeHandle handle) {
			return new FTBitmap(handle);
		}

	}

	/** The meta class instance */
	public static final MetaClass META = new MetaClass(MetaClass.class
			.getDeclaringClass());

	static {
		META.declare("rows", NativeInt.META);
		META.declare("width", NativeInt.META);
		META.declare("pitch", NativeInt.META);
		META.declare("buffer", NativeBuffer.META.Ref());
		META.declare("numGrays", NativeShort.META);
		META.declare("pixelMode", NativeByte.META);
		META.declare("paletteMode", NativeByte.META);
		META.declare("palette", NativeVoid.META.Ref());
	}

	/**
	 * 
	 */
	public FTBitmap() {
	}

	protected FTBitmap(INativeHandle handle) {
		super(handle);
	}

	public NativeBuffer getBuffer() {
		int size = getRows() * getPitch();
		NativeBuffer buffer = (NativeBuffer) getNativeObject("buffer")
				.getValue();
		buffer.setSize(size);
		return buffer;
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

	public int getPitch() {
		return ((NativeNumber) getNativeObject("pitch")).intValue();
	}

	public int getRows() {
		return ((NativeNumber) getNativeObject("rows")).intValue();
	}

	public int getWidth() {
		return ((NativeNumber) getNativeObject("width")).intValue();
	}

}
