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
import de.intarsys.nativec.type.NativeInt;
import de.intarsys.nativec.type.NativeLong;
import de.intarsys.nativec.type.NativeStaticStruct;
import de.intarsys.nativec.type.NativeStructType;
import de.intarsys.nativec.type.NativeVoid;
import de.intarsys.nativec.type.StructMember;

/**
 * FreeType root glyph slot class structure. A glyph slot is a container where
 * individual glyphs can be loaded, be they vectorial or bitmap/graymaps.
 */
public class FTGlyphSlot extends NativeStaticStruct {
	/**
	 * The meta class implementation
	 */
	public static class MetaClass extends NativeStructType {
		protected MetaClass(Class instanceClass) {
			super(instanceClass);
		}

		@Override
		public INativeObject createNative(INativeHandle handle) {
			return new FTGlyphSlot(handle);
		}

	}

	static final private StructMember advance;

	static final private StructMember bitmap;

	static final private StructMember bitmapLeft;

	static final private StructMember bitmapTop;

	static final private StructMember controlData;

	static final private StructMember controlLen;

	static final private StructMember face;

	static final private StructMember format;

	static final private StructMember generic;

	static final private StructMember internal;

	static final private StructMember library;

	static final private StructMember linearHoriAdvance;

	static final private StructMember linearVertAdvance;

	/** The meta class instance */
	public static final MetaClass META = new MetaClass(MetaClass.class
			.getDeclaringClass());

	static final private StructMember metrics;

	static final private StructMember next;

	static final private StructMember numSubglyphs;

	static final private StructMember other;

	static final private StructMember outline;

	static final private StructMember reserved;

	static final private StructMember subglyphs;

	static {
		library = META.declare("library", FTLibrary.META.Ref()); //$NON-NLS-1$
		face = META.declare("face", FTFace.META.Ref()); //$NON-NLS-1$
		next = META.declare("next", FTGlyphSlot.META.Ref()); //$NON-NLS-1$
		reserved = META.declare("reserved", NativeInt.META); //$NON-NLS-1$
		generic = META.declare("generic", FTGeneric.META); //$NON-NLS-1$
		metrics = META.declare("metrics", FTGlyphMetrics.META); //$NON-NLS-1$
		linearHoriAdvance = META.declare("linearHoriAdvance", FTFixed.META); //$NON-NLS-1$
		linearVertAdvance = META.declare("linearVertAdvance", FTFixed.META); //$NON-NLS-1$
		advance = META.declare("advance", FTVector.META); //$NON-NLS-1$
		format = META.declare("format", FTEnum.META); //$NON-NLS-1$
		bitmap = META.declare("bitmap", FTBitmap.META); //$NON-NLS-1$
		bitmapLeft = META.declare("bitmapLeft", NativeInt.META); //$NON-NLS-1$
		bitmapTop = META.declare("bitmapTop", NativeInt.META); //$NON-NLS-1$
		outline = META.declare("outline", FTOutline.META); //$NON-NLS-1$
		numSubglyphs = META.declare("numSubglyphs", NativeInt.META); //$NON-NLS-1$
		subglyphs = META.declare("subglyphs", NativeVoid.META.Ref()); //$NON-NLS-1$
		controlData = META.declare("controlData", NativeVoid.META.Ref()); //$NON-NLS-1$
		controlLen = META.declare("controlLen", NativeLong.META); //$NON-NLS-1$
		other = META.declare("other", NativeLong.META); //$NON-NLS-1$
		internal = META.declare("internal", NativeLong.META); //$NON-NLS-1$
	}

	public FTGlyphSlot() {
		super();
	}

	protected FTGlyphSlot(INativeHandle handle) {
		super(handle);
	}

	public FTBitmap getBitmap() {
		return (FTBitmap) bitmap.getNativeObject(this);
	}

	public int getBitmapLeft() {
		return bitmapLeft.getInt(this, 0);
	}

	public int getBitmapTop() {
		return bitmapTop.getInt(this, 0);
	}

	public FTGlyphMetrics getGlyphMetrics() {
		return (FTGlyphMetrics) metrics.getNativeObject(this);
	}

	public long getLinearHoriAdvance() {
		return linearHoriAdvance.getCLong(this, 0);
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

	public FTOutline getOutline() {
		return (FTOutline) outline.getNativeObject(this);
	}

}
