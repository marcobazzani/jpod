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

import java.io.IOException;

import de.intarsys.nativec.api.INativeFunction;
import de.intarsys.nativec.api.INativeLibrary;
import de.intarsys.nativec.api.NativeInterface;
import de.intarsys.nativec.type.NativeBuffer;
import de.intarsys.nativec.type.NativeReference;
import de.intarsys.tools.installresource.InstallZip;

/**
 * The freetype native interface wrapper.
 */
public class _FTNI {

	private INativeLibrary LIB;

	private INativeFunction doneFace;

	private INativeFunction doneFreeType;

	private INativeFunction getCharIndex;

	private INativeFunction getFirstChar;

	private INativeFunction getPostscriptName;

	private INativeFunction getNameIndex;

	private INativeFunction initFreeType;

	private INativeFunction loadChar;

	private INativeFunction loadGlyph;

	private INativeFunction newFace;

	private INativeFunction newMemoryFace;

	private INativeFunction renderGlyph;

	private INativeFunction selectCharMap;

	private INativeFunction setCharMap;

	private INativeFunction setCharSize;

	private INativeFunction getSfntNameCount;

	private INativeFunction getSfntName;

	public _FTNI() {
		super();
		init();
	}

	public synchronized int DoneFace(FTFace face) {
		return doneFace.invoke(Integer.class, face).intValue();
	}

	public synchronized int DoneFreeType(FTLibrary library) {
		return (doneFreeType.invoke(Integer.class, library)).intValue();
	}

	public synchronized int GetCharIndex(FTFace face, int code) {
		return (getCharIndex.invoke(Integer.class, face, code)).intValue();
	}

	public synchronized int GetFirstChar(FTFace face) {
		return (getFirstChar.invoke(Integer.class, face, new byte[4]))
				.intValue();
	}

	public synchronized int GetNameIndex(FTFace face, String name) {
		return (getNameIndex.invoke(Integer.class, face, name)).intValue();
	}

	public synchronized String GetPostscriptName(FTFace face) {
		return getPostscriptName.invoke(String.class, face);
	}

	public synchronized int GetSfntName(FTFace face, int index,
			FTSfntName sfntName) {
		return (getSfntName.invoke(Integer.class, face, index, sfntName))
				.intValue();
	}

	public synchronized int GetSfntNameCount(FTFace face) {
		return (getSfntNameCount.invoke(Integer.class, face)).intValue();
	}

	private void init() {
		try {
			// look for preinstalled version...
			LIB = NativeInterface.get().createLibrary("freetype");
		} catch (Throwable t) {
			try {
				InstallZip installPackage = new InstallZip(
						"de/intarsys/cwt/freetype/nativec", "freetype.zip",
						true);
				installPackage.load();
				if (installPackage.getFile() != null) {
					NativeInterface.get().addSearchPath(
							installPackage.getFile().getAbsolutePath());
				}
			} catch (IOException e) {
				//
			}
			LIB = NativeInterface.get().createLibrary("freetype");
		}
		doneFace = LIB.getFunction("FT_Done_Face");
		doneFreeType = LIB.getFunction("FT_Done_FreeType");
		getCharIndex = LIB.getFunction("FT_Get_Char_Index");
		getFirstChar = LIB.getFunction("FT_Get_First_Char");
		getPostscriptName = LIB.getFunction("FT_Get_Postscript_Name");
		getNameIndex = LIB.getFunction("FT_Get_Name_Index");
		initFreeType = LIB.getFunction("FT_Init_FreeType");
		loadChar = LIB.getFunction("FT_Load_Char");
		loadGlyph = LIB.getFunction("FT_Load_Glyph");
		newFace = LIB.getFunction("FT_New_Face");
		newMemoryFace = LIB.getFunction("FT_New_Memory_Face");
		renderGlyph = LIB.getFunction("FT_Render_Glyph");
		selectCharMap = LIB.getFunction("FT_Select_Charmap");
		setCharMap = LIB.getFunction("FT_Set_Charmap");
		setCharSize = LIB.getFunction("FT_Set_Char_Size");
		getSfntNameCount = LIB.getFunction("FT_Get_Sfnt_Name_Count");
		getSfntName = LIB.getFunction("FT_Get_Sfnt_Name");
	}

	public synchronized int InitFreeType(NativeReference refLibrary) {
		int rc = (initFreeType.invoke(Integer.class, refLibrary)).intValue();
		return rc;
	}

	public synchronized int LoadChar(FTFace face, int code, int flags) {
		return (loadChar.invoke(Integer.class, face, code, flags)).intValue();
	}

	public synchronized int LoadGlyph(FTFace face, int index, int flags) {
		return (loadGlyph.invoke(Integer.class, face, index, flags)).intValue();
	}

	public synchronized int NewFace(FTLibrary library, String name, int index,
			NativeReference refFace) {
		int rc = (newFace.invoke(Integer.class, library, name, index, refFace))
				.intValue();
		return rc;
	}

	public synchronized int NewMemoryFace(FTLibrary library,
			NativeBuffer buffer, int fontIndex, NativeReference refFace) {
		int length = buffer.getSize();
		int rc = (newMemoryFace.invoke(Integer.class, library, buffer, length,
				fontIndex, refFace)).intValue();
		return rc;
	}

	public synchronized int RenderGlyph(FTGlyphSlot glyph, int flags) {
		return (renderGlyph.invoke(Integer.class, glyph, flags)).intValue();
	}

	public synchronized int SelectCharMap(FTFace face, FTEnum encoding) {
		return (selectCharMap.invoke(Integer.class, face, encoding.intValue()))
				.intValue();
	}

	public synchronized int SetCharMap(FTFace face, FTCharMap map) {
		int rc = (setCharMap.invoke(Integer.class, face, map)).intValue();
		return rc;
	}

	public synchronized int SetCharSize(FTFace face, int width, int height,
			int hRes, int vRes) {
		return (setCharSize.invoke(Integer.class, face, width, height, hRes,
				vRes)).intValue();
	}
}
