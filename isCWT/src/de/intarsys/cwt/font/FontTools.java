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

public class FontTools {

	static public String createCanonicalName(IFont font) {
		return createCanonicalName(font.getFontFamilyName(), font
				.getFontStyle().getId());
	}

	static public String createCanonicalName(String fontFamilyName,
			String styleName) {
		return fontFamilyName.replaceAll("\\s", "") + "," + styleName;
	}

	public static String getFontFamilyName(String fontName) {
		if (fontName == null) {
			return null;
		}
		int posMinus = fontName.indexOf('-');
		if (posMinus > 0) {
			fontName = fontName.substring(0, posMinus);
		}
		return fontName;
	}

	public static FontStyle getFontStyle(String fontName) {
		if (fontName == null) {
			return FontStyle.REGULAR;
		}
		int posMinus = fontName.indexOf('-');
		if (posMinus > 0) {
			fontName = fontName.substring(posMinus + 1);
		}
		return FontStyle.getFontStyle(fontName);
	}

	static public IFont lookupFont(IFontQuery query) {
		IFont result = lookupFontOrMap(query);
		if (result == null && FontEnvironment.get().registerUserFonts()) {
			result = lookupFontOrMap(query);
		}
		// do not nest in the condition above!
		if (result == null && FontEnvironment.get().registerSystemFonts()) {
			result = lookupFontOrMap(query);
		}
		return result;
	}

	static protected IFont lookupFontOrMap(IFontQuery query) {
		IFont result = FontRegistry.get().lookupFont(query);
		if (result == null) {
			result = FontMapper.get().lookupFont(query.getFontName());
			if (result != null) {
				String queryType = query.getFontType();
				String resultType = result.getFontType();
				if (queryType != null && resultType != null
						&& !"Any".equals(queryType)
						&& !resultType.equals(queryType)) {
					// ensure that we do not mistakenly return a mapped font of
					// wrong type.
					result = null;
				}
			}
		}
		return result;
	}

	static public void mapAlias(String name, String alias) {
		FontAliasMap map = new FontAliasMap(FontMapper.get(), name, alias);
		FontMapper.get().registerFontMap(map);
	}

	static public void mapFont(String name, IFont font) {
		FontFontMap map = new FontFontMap(FontMapper.get(), name, font);
		FontMapper.get().registerFontMap(map);
	}

}
