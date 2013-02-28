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
package de.intarsys.cwt.freetype;

import java.awt.geom.Rectangle2D;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.intarsys.cwt.freetype.nativec.FTBBox;
import de.intarsys.cwt.freetype.nativec.FTCharMap;
import de.intarsys.cwt.freetype.nativec.FTEnum;
import de.intarsys.cwt.freetype.nativec.FTFace;
import de.intarsys.cwt.freetype.nativec.FTSfntName;
import de.intarsys.cwt.freetype.nativec._FTNI;
import de.intarsys.nativec.type.NativeBuffer;

public class Face {

	private static final Logger Log = PACKAGE.Log;

	final private FTFace face;

	final private NativeBuffer fontData;

	final private _FTNI ftni;

	private CharMap cachedCharMap = null;

	/**
	 * 
	 */
	protected Face(_FTNI pFtni, FTFace pFace, NativeBuffer pFontData) {
		this.ftni = pFtni;
		this.face = pFace;
		this.fontData = pFontData;
	}

	public void clearCache() {
		cachedCharMap = null;
	}

	public void doneFace() {
		int rc = ftni.DoneFace(face);
		if (rc != 0) {
			Log.log(Level.WARNING, "error " + rc + " in DoneFace");
		}
	}

	public int getAscender() {
		return face.getAscender();
	}

	public Rectangle2D getBBox() {
		FTBBox faceBox = face.getBBox();
		Rectangle2D.Float result = new Rectangle2D.Float();
		result.x = faceBox.getMinX();
		result.y = faceBox.getMinY();
		result.width = faceBox.getMaxX() - faceBox.getMinX();
		result.height = faceBox.getMaxY() - faceBox.getMinY();
		return result;
	}

	public int getCharIndex(int code) {
		return ftni.GetCharIndex(face, code);
	}

	public CharMap getCharMap() {
		if (cachedCharMap == null) {
			cachedCharMap = CharMap.create(ftni, face.getCharMap());
		}
		return cachedCharMap;
	}

	public CharMap getCharMap(int index) {
		return new CharMap(ftni, (FTCharMap) face.getCharMaps().getValue(index));
	}

	public int getDescender() {
		return face.getDescender();
	}

	public FTFace getFace() {
		return face;
	}

	public String getFamilyName() {
		return face.getFamilyName();
	}

	public int getFirstChar() {
		return ftni.GetFirstChar(face);
	}

	public NativeBuffer getFontData() {
		return fontData;
	}

	public GlyphSlot getGlyphSlot() {
		return new GlyphSlot(ftni, face.getGlyphSlot());
	}

	public int getHeight() {
		return face.getHeight();
	}

	public int getNameIndex(String name) {
		return ftni.GetNameIndex(face, name);
	}

	public int getNumCharMaps() {
		return face.getNumCharMaps();
	}

	public String getPostscriptName() {
		return ftni.GetPostscriptName(face);
	}

	public SfntName getSfntName(int index) {
		FTSfntName ftSfntName = new FTSfntName();
		int rc = ftni.GetSfntName(face, index, ftSfntName);
		if (rc != 0) {
			return null;
		}
		return new SfntName(ftni, ftSfntName);
	}

	public int getSfntNameCount() {
		return ftni.GetSfntNameCount(face);
	}

	public String getStyleName() {
		return face.getStyleName();
	}

	public int getUnderlinePosition() {
		return face.getUnderlinePosition();
	}

	public int getUnderlineThickness() {
		return face.getUnderlineThickness();
	}

	public int getUnitsPerEM() {
		return face.getUnitsPerEM();
	}

	public void loadChar(int code, int flags) throws FreetypeException {
		int rc = ftni.LoadChar(face, code, flags);
		if (rc != 0) {
			throw new FreetypeException();
		}
	}

	public void loadGlyph(int index, int flags) throws FreetypeException {
		int rc = ftni.LoadGlyph(face, index, flags);
		if (rc != 0) {
			throw new FreetypeException();
		}
	}

	public void selectCharMap(FTEnum id) throws FreetypeException {
		int rc = ftni.SelectCharMap(face, id);
		if (rc != 0) {
			throw new FreetypeException();
		}
	}

	public void setCharMap(CharMap charMap) {
		ftni.SetCharMap(face, charMap.getCharMap());
		clearCache();
	}

	public void setCharSize(int width, int height, int hRes, int vRes) {
		ftni.SetCharSize(face, width, height, hRes, vRes);
	}
}
