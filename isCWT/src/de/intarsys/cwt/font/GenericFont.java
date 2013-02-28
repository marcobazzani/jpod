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
package de.intarsys.cwt.font;

import de.intarsys.cwt.freetype.Face;
import de.intarsys.cwt.freetype.SfntName;
import de.intarsys.tools.locator.ILocator;

public class GenericFont extends CommonFont {

	public static GenericFont createNew(ILocator locator, Face face) {
		String fontPSName = face.getPostscriptName();
		String familyName = face.getFamilyName();
		String styleName = face.getStyleName();
		String fontName = null;
		// look up font name for TrueType
		int sfntCount = face.getSfntNameCount();
		for (int i = 0; i < sfntCount; i++) {
			SfntName sfntName = face.getSfntName(i);
			if (sfntName.getNameId() == 4) {
				// full font name
				fontName = sfntName.getName();
				if (sfntName.getPlatformId() == 3) {
					// try to use the microsoft platform name
					break;
				}
			}
		}
		if (fontPSName == null) {
			fontPSName = familyName;
		}
		if (fontName == null) {
			fontName = fontPSName;
		}
		FontStyle style = FontStyle.getFontStyle(styleName);
		// ...little bit ugly, but clean up someday
		GenericFont font = new GenericFont();
		GenericFontProgram fontProgram = new GenericFontProgram(font, locator);
		font.setFontProgram(fontProgram);
		font.setFontFamilyName(familyName);
		font.setFontNamePostScript(fontPSName);
		font.setFontName(fontName);
		font.setFontStyle(style);
		String type = locator.getType();
		if (type != null) {
			type = type.toLowerCase();
		}
		if ("ttf".equals(type) || "ttc".equals(type) || "otf".equals(type)
				|| "otc".equals(type)) {
			font.setFontType("TrueType");
		} else if ("pfb".equals(type)) {
			font.setFontType("Type1");
		} else {
			font.setFontType("Unknown");
		}
		return font;
	}

	private String fontType;

	private String fontName;

	private String fontNamePostScript;

	private String fontFamilyName;

	private FontStyle fontStyle;

	protected GenericFont() {
		super();
	}

	public String getFontFamilyName() {
		return fontFamilyName;
	}

	public String getFontName() {
		return fontName;
	}

	public String getFontNameCanonical() {
		String tempFamilyName = null;
		FontStyle tempStyle = null;
		if (getFontFamilyName() == null) {
			tempFamilyName = FontTools.getFontFamilyName(getFontName());
		} else {
			tempFamilyName = getFontFamilyName();
		}
		if (getFontStyle() == null) {
			tempStyle = FontTools.getFontStyle(getFontName());
		} else {
			tempStyle = getFontStyle();
		}
		return FontTools.createCanonicalName(tempFamilyName, tempStyle.getId());
	}

	public String getFontNamePostScript() {
		return fontNamePostScript;
	}

	public FontStyle getFontStyle() {
		return fontStyle;
	}

	public String getFontType() {
		return fontType;
	}

	protected void setFontFamilyName(String familyName) {
		this.fontFamilyName = familyName;
	}

	protected void setFontName(String fontName) {
		this.fontName = fontName;
	}

	protected void setFontNamePostScript(String fontPSName) {
		this.fontNamePostScript = fontPSName;
	}

	protected void setFontStyle(FontStyle style) {
		this.fontStyle = style;
	}

	protected void setFontType(String fontType) {
		this.fontType = fontType;
	}
}
