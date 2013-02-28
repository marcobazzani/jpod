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
import de.intarsys.nativec.type.NativeNumber;
import de.intarsys.nativec.type.NativeShort;
import de.intarsys.nativec.type.NativeStaticStruct;
import de.intarsys.nativec.type.NativeStructType;

/**
 * The base charmap structure.
 * 
 * <pre>
 *  typedef struct  FT_CharMapRec_
 *  {
 *  FT_Face      face;
 *  FT_Encoding  encoding;
 *  FT_UShort    platform_id;
 *  FT_UShort    encoding_id;
 *  } FT_CharMapRec;
 * </pre>
 */
public class FTCharMap extends NativeStaticStruct {
	/**
	 * The meta class implementation
	 */
	public static class MetaClass extends NativeStructType {
		protected MetaClass(Class instanceClass) {
			super(instanceClass);
		}

		@Override
		public INativeObject createNative(INativeHandle handle) {
			return new FTCharMap(handle);
		}

	}

	/** The meta class instance */
	public static final MetaClass META = new MetaClass(MetaClass.class
			.getDeclaringClass());

	static {
		META.declare("face", FTFace.META.Ref());
		META.declare("encoding", FTEnum.META);
		META.declare("platformId", NativeShort.META);
		META.declare("encodingId", NativeShort.META);
	}

	private FTEnum cachedEncoding = null;

	private int encodingId = -1;

	private int platformId = -1;

	public FTCharMap() {
	}

	protected FTCharMap(INativeHandle handle) {
		super(handle);
	}

	public FTEnum getEncoding() {
		if (cachedEncoding == null) {
			cachedEncoding = (FTEnum) getNativeObject("encoding");
		}
		return cachedEncoding;
	}

	public int getEncodingId() {
		if (encodingId == -1) {
			encodingId = ((NativeNumber) getNativeObject("encodingId"))
					.intValue();
		}
		return encodingId;
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

	public int getPlatformId() {
		if (platformId == -1) {
			platformId = ((NativeNumber) getNativeObject("platformId"))
					.intValue();
		}
		return platformId;
	}

	public boolean isEncodingAdobeCustom() {
		return getEncoding().equals(FTEnum.ENCODING_ADOBE_CUSTOM);
	}

	public boolean isEncodingAdobeExpert() {
		return getEncoding().equals(FTEnum.ENCODING_ADOBE_EXPERT);
	}

	public boolean isEncodingAdobeLatin1() {
		return getEncoding().equals(FTEnum.ENCODING_ADOBE_LATIN1);
	}

	public boolean isEncodingAdobeStandard() {
		return getEncoding().equals(FTEnum.ENCODING_ADOBE_STANDARD);
	}

	public boolean isEncodingMSSymbol() {
		return getEncoding().equals(FTEnum.ENCODING_MS_SYMBOL);
	}

	public boolean isEncodingUnicode() {
		return getEncoding().equals(FTEnum.ENCODING_UNICODE);
	}

}
