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
import de.intarsys.nativec.type.NativeArray;
import de.intarsys.nativec.type.NativeBuffer;
import de.intarsys.nativec.type.NativeByte;
import de.intarsys.nativec.type.NativeInt;
import de.intarsys.nativec.type.NativeLong;
import de.intarsys.nativec.type.NativeShort;
import de.intarsys.nativec.type.NativeStaticStruct;
import de.intarsys.nativec.type.NativeString;
import de.intarsys.nativec.type.NativeStructType;
import de.intarsys.nativec.type.NativeVoid;
import de.intarsys.nativec.type.StructMember;

/**
 * FreeType root face class structure. A face object models the resolution and
 * point-size independent data found in a font file.
 * 
 * <pre>
 *  typedef struct  FT_FaceRec_
 *  {
 *  FT_Long           num_faces;
 *  FT_Long           face_index;
 * 
 *  FT_Long           face_flags;
 *  FT_Long           style_flags;
 * 
 *  FT_Long           num_glyphs;
 * 
 *  FT_String*        family_name;
 *  FT_String*        style_name;
 * 
 *  FT_Int            num_fixed_sizes;
 *  FT_Bitmap_Size*   available_sizes;
 * 
 *  FT_Int            num_charmaps;
 *  FT_CharMap*       charmaps;
 * 
 *  FT_Generic        generic;
 * 
 *  FT_BBox           bbox;
 * 
 *  FT_UShort         units_per_EM;
 *  FT_Short          ascender;
 *  FT_Short          descender;
 *  FT_Short          height;
 * 
 *  FT_Short          max_advance_width;
 *  FT_Short          max_advance_height;
 * 
 *  FT_Short          underline_position;
 *  FT_Short          underline_thickness;
 * 
 *  FT_GlyphSlot      glyph;
 *  FT_Size           size;
 *  FT_CharMap        charmap;
 * 
 *  FT_Driver         driver;
 *  FT_Memory         memory;
 *  FT_Stream         stream;
 * 
 *  FT_ListRec        sizes_list;
 * 
 *  FT_Generic        autohint;
 *  void*             extensions;
 * 
 *  FT_Face_Internal  internal;
 *  } FT_FaceRec;
 * </pre>
 */
public class FTFace extends NativeStaticStruct {
	/**
	 * The meta class implementation
	 */
	public static class MetaClass extends NativeStructType {
		protected MetaClass(Class instanceClass) {
			super(instanceClass);
		}

		@Override
		public INativeObject createNative(INativeHandle handle) {
			return new FTFace(handle);
		}

	}

	static final private StructMember ascender;

	static final private StructMember availableSizes;

	static final private StructMember bbox;

	static final private StructMember charMap;

	static final private StructMember charMaps;

	static final private StructMember descender;

	static final private StructMember faceFlags;

	static final private StructMember faceIndex;

	static final private StructMember familyName;

	static final private StructMember generic;

	static final private StructMember glyphSlot;

	static final private StructMember height;

	static final private StructMember maxAdvanceHeight;

	static final private StructMember maxAdvanveWidth;

	/** The meta class instance */
	public static final MetaClass META = new MetaClass(MetaClass.class
			.getDeclaringClass());

	static final private StructMember numCharMaps;

	static final private StructMember numFaces;

	static final private StructMember numFixedSizes;

	static final private StructMember numGlyphs;

	static final private StructMember size;

	static final private StructMember styleFlags;

	static final private StructMember styleName;

	static final private StructMember underlinePosition;

	static final private StructMember underlineThickness;

	static final private StructMember unitsPerEM;

	static {
		numFaces = META.declare("numFaces", NativeLong.META); //$NON-NLS-1$
		faceIndex = META.declare("faceIndex", NativeLong.META); //$NON-NLS-1$
		faceFlags = META.declare("faceFlags", NativeLong.META); //$NON-NLS-1$
		styleFlags = META.declare("styleFlags", NativeLong.META); //$NON-NLS-1$
		numGlyphs = META.declare("numGlyphs", NativeLong.META); //$NON-NLS-1$
		familyName = META.declare("familyName", NativeString.META.Ref()); //$NON-NLS-1$
		styleName = META.declare("styleName", NativeString.META.Ref()); //$NON-NLS-1$
		numFixedSizes = META.declare("numFixedSizes", NativeInt.META); //$NON-NLS-1$
		availableSizes = META.declare("availableSizes", FTBitmapSize.META.Ref()
				.Array(0).Ref()); //$NON-NLS-1$
		numCharMaps = META.declare("numCharMaps", NativeInt.META); //$NON-NLS-1$
		charMaps = META
				.declare("charMaps", FTCharMap.META.Ref().Array(0).Ref()); //$NON-NLS-1$
		generic = META.declare("generic", FTGeneric.META); //$NON-NLS-1$
		bbox = META.declare("bbox", FTBBox.META); //$NON-NLS-1$
		unitsPerEM = META.declare("unitsPerEM", NativeShort.META); //$NON-NLS-1$
		ascender = META.declare("ascender", NativeShort.META); //$NON-NLS-1$
		descender = META.declare("descender", NativeShort.META); //$NON-NLS-1$
		height = META.declare("height", NativeShort.META); //$NON-NLS-1$
		maxAdvanveWidth = META.declare("maxAdvanceWidth", NativeShort.META); //$NON-NLS-1$
		maxAdvanceHeight = META.declare("maxAdvanceHeight", NativeShort.META); //$NON-NLS-1$
		underlinePosition = META.declare("underlinePosition", NativeShort.META); //$NON-NLS-1$
		underlineThickness = META.declare("underlineThickness", //$NON-NLS-1$
				NativeShort.META);
		glyphSlot = META.declare("glyph", FTGlyphSlot.META.Ref());
		size = META.declare("size", FTSize.META.Ref());
		charMap = META.declare("charMap", FTCharMap.META.Ref());

		// rest is private

		// no explicit type for FT_DriverRec - use void
		META.declare("driver", NativeVoid.META.Ref());
		// no explicit type for FT_MemoryRec - use void
		META.declare("memory", NativeVoid.META.Ref());
		// no explicit type for FT_MemoryRec - use void
		META.declare("stream", NativeVoid.META.Ref());
		// no explicit type for FT_ListRec - approximate
		META.declare("sizesList", NativeByte.META.Array(16));
		META.declare("autohint", FTGeneric.META);
		META.declare("extensions", NativeVoid.META.Ref());
		// no explicit type for FT_Face_InternalRec - use void
		META.declare("internal", NativeVoid.META.Ref());
	}

	private NativeBuffer faceBuffer;

	public FTFace() {
	}

	protected FTFace(INativeHandle handle) {
		super(handle);
	}

	public short getAscender() {
		return ascender.getShort(this, 0);
	}

	public FTBBox getBBox() {
		return (FTBBox) bbox.getNativeObject(this);
	}

	public FTCharMap getCharMap() {
		return (FTCharMap) charMap.getValue(this);
	}

	public NativeArray getCharMaps() {
		NativeArray array = (NativeArray) charMaps.getValue(this);
		array.setSize(getNumCharMaps());
		return array;
	}

	public short getDescender() {
		return descender.getShort(this, 0);
	}

	public NativeBuffer getFaceBuffer() {
		return faceBuffer;
	}

	public long getFaceFlags() {
		return faceFlags.getCLong(this, 0);
	}

	public long getFaceIndex() {
		return faceIndex.getCLong(this, 0);
	}

	public String getFamilyName() {
		return ((NativeString) familyName.getValue(this)).stringValue();
	}

	public FTGlyphSlot getGlyphSlot() {
		return (FTGlyphSlot) glyphSlot.getValue(this);
	}

	public short getHeight() {
		return height.getShort(this, 0);
	}

	@Override
	public INativeType getNativeType() {
		return META;
	}

	public int getNumCharMaps() {
		return numCharMaps.getInt(this, 0);
	}

	public long getNumFaces() {
		return numFaces.getCLong(this, 0);
	}

	public long getNumGlyphs() {
		return numGlyphs.getCLong(this, 0);
	}

	public long getStyleFlags() {
		return styleFlags.getCLong(this, 0);
	}

	public String getStyleName() {
		return ((NativeString) styleName.getValue(this)).stringValue();
	}

	public short getUnderlinePosition() {
		return underlinePosition.getShort(this, 0);
	}

	public short getUnderlineThickness() {
		return underlineThickness.getShort(this, 0);
	}

	public short getUnitsPerEM() {
		return unitsPerEM.getShort(this, 0);
	}

	public void setCharMap(_FTNI ftni, FTCharMap charMap) {
		ftni.SetCharMap(this, charMap);
	}

	public void setFaceBuffer(NativeBuffer faceBuffer) {
		this.faceBuffer = faceBuffer;
	}
}