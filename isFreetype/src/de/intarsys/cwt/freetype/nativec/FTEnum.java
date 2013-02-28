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
import de.intarsys.nativec.type.NativeSimple;
import de.intarsys.nativec.type.NativeSimpleType;

/**
 * 
 */
public class FTEnum extends NativeSimple {
	/**
	 * The meta class implementation
	 */
	public static class MetaClass extends NativeSimpleType {
		protected MetaClass() {
			super();
		}

		@Override
		public INativeObject createNative(INativeHandle handle) {
			return new FTEnum(handle);
		}

		@Override
		public INativeObject createNative(Object value) {
			throw new IllegalStateException("meta constructor missing");
		}

		@Override
		public int getByteCount() {
			return SIZE_INT;
		}
	}

	/** The meta class instance */
	public static final MetaClass META = new MetaClass();

	public static final FTEnum ENCODING_ADOBE_CUSTOM = new FTEnum('A', 'D',
			'B', 'C');
	public static final FTEnum ENCODING_ADOBE_EXPERT = new FTEnum('A', 'D',
			'B', 'E');
	public static final FTEnum ENCODING_ADOBE_LATIN1 = new FTEnum('l', 'a',
			't', '1');
	public static final FTEnum ENCODING_ADOBE_STANDARD = new FTEnum('A', 'D',
			'O', 'B');
	public static final FTEnum ENCODING_MS_SYMBOL = new FTEnum('s', 'y', 'm',
			'b');
	public static final FTEnum ENCODING_UNICODE = new FTEnum('u', 'n', 'i', 'c');

	public FTEnum(char a, char b, char c, char d) {
		allocate();
		handle.setByte(3, (byte) a);
		handle.setByte(2, (byte) b);
		handle.setByte(1, (byte) c);
		handle.setByte(0, (byte) d);
	}

	protected FTEnum(INativeHandle handle) {
		super(handle);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof FTEnum) {
			return intValue() == ((FTEnum) obj).intValue();
		} else {
			return false;
		}
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

	public Object getValue() {
		return new Integer(intValue());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return intValue();
	}

	public int intValue() {
		return handle.getInt(0);
	}

	public void setValue(Object value) {
		handle.setInt(0, ((Number) value).intValue());
	}

	@Override
	public String toString() {
		return "<" + (char) handle.getByte(0) + (char) handle.getByte(1)
				+ (char) handle.getByte(2) + (char) handle.getByte(3) + ">";
	}
}
