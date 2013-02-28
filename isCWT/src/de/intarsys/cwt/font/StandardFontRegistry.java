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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.intarsys.tools.collection.ListTools;

/**
 * 
 */
public class StandardFontRegistry implements IFontRegistry {

	private final Map<String, IFontFamily> fontFamilyMap = new HashMap<String, IFontFamily>();

	private final List<IFontFamily> fontFamilies = new ArrayList<IFontFamily>();

	private final Map<String, Object> fontMap = new HashMap<String, Object>();

	private final List<IFont> fonts = new ArrayList<IFont>();

	private static final Logger Log = PACKAGE.Log;

	public StandardFontRegistry() {
		//
	}

	protected IFont basicLookup(String key, IFontQuery query) {
		if (key == null) {
			return null;
		}
		Object result = fontMap.get(key);
		if (result == null) {
			Log.log(Level.FINEST, "font registry missed " + key);
			return null;
		} else if (result instanceof IFont) {
			Log.log(Level.FINEST, "font registry found " + key);
			return (IFont) result;
		} else {
			// collision
			// there are a number of ill declared fonts around....
			for (Iterator it = ((List) result).iterator(); it.hasNext();) {
				IFont font = (IFont) it.next();
				if (basicMatchFamilyStyle(query, font)) {
					Log.log(Level.FINEST,
							"font registry resolved by family style " + key);
					return font;
				}
			}
			for (Iterator it = ((List) result).iterator(); it.hasNext();) {
				IFont font = (IFont) it.next();
				if (basicMatchFamily(query, font)) {
					Log.log(Level.FINEST, "font registry resolved by family "
							+ key);
					return font;
				}
			}
			return null;
		}
	}

	protected boolean basicMatchFamily(IFontQuery query, IFont font) {
		return query.getFontFamilyName().indexOf(font.getFontFamilyName()) >= 0;
	}

	protected boolean basicMatchFamilyStyle(IFontQuery query, IFont font) {
		return query.getFontNameCanonical().equals(font.getFontNameCanonical());
	}

	protected void basicRegister(String key, IFont value) {
		String program = "embedded font";
		if (value.getFontProgram() != null) {
			program = value.getFontProgram().getLocator().getFullName();
		}
		// overwrite - this is the most obvious case
		Log.log(Level.FINEST, "font registry register " + program + " with "
				+ key);
		Object previous = fontMap.put(key, value);
		if (previous == null) {
			return;
		}
		if (previous instanceof List) {
			((List) previous).add(value);
			// reset, was overwritten
			fontMap.put(key, previous);
		} else {
			// new entry
			fontMap.put(key, ListTools.with(previous, value));
		}
	}

	synchronized public IFontFamily[] getFontFamilies() {
		FontEnvironment.get().registerUserFonts();
		FontEnvironment.get().registerSystemFonts();
		return fontFamilies.toArray(new IFontFamily[fontFamilies.size()]);
	}

	synchronized public IFont[] getFonts() {
		FontEnvironment.get().registerUserFonts();
		FontEnvironment.get().registerSystemFonts();
		return fonts.toArray(new IFont[fonts.size()]);
	}

	protected Object getLookupKeyFontFamily(IFontQuery query) {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		if (query.getFontType() == null) {
			sb.append("Any");
		} else {
			sb.append(query.getFontType());
		}
		sb.append("]");
		sb.append(query.getFontFamilyName());
		return sb.toString();
	}

	protected String getLookupKeyFontName(IFontQuery query) {
		if (query.getFontName() == null) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		if (query.getFontType() == null) {
			sb.append("Any");
		} else {
			sb.append(query.getFontType());
		}
		sb.append("]");
		sb.append(query.getFontName());
		return sb.toString();
	}

	protected String getLookupKeyFontNameAsCanonical(IFontQuery query) {
		if (query.getFontName() == null) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		if (query.getFontType() == null) {
			sb.append("Any");
		} else {
			sb.append(query.getFontType());
		}
		sb.append("-canonical]");
		sb.append(query.getFontName());
		return sb.toString();
	}

	protected String getLookupKeyFontNameCanonical(IFontQuery query) {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		if (query.getFontType() == null) {
			sb.append("Any");
		} else {
			sb.append(query.getFontType());
		}
		sb.append("-canonical]");
		sb.append(query.getFontNameCanonical());
		return sb.toString();
	}

	protected String getLookupKeyFontNamePostScript(IFontQuery query) {
		if (query.getFontName() == null) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		if (query.getFontType() == null) {
			sb.append("Any");
		} else {
			sb.append(query.getFontType());
		}
		sb.append("-postscript]");
		sb.append(query.getFontName());
		return sb.toString();
	}

	public IFont lookupFont(IFontQuery query) {
		// best fallbacks ever
		IFont result = basicLookup(getLookupKeyFontName(query), query);
		if (result == null) {
			result = basicLookup(getLookupKeyFontNamePostScript(query), query);
			if (result == null) {
				result = basicLookup(getLookupKeyFontNameCanonical(query),
						query);
				if (result == null) {
					result = basicLookup(
							getLookupKeyFontNameAsCanonical(query), query);
				}
			}
		}
		return result;
	}

	public IFontFamily lookupFontFamily(IFontQuery query) {
		return fontFamilyMap.get(getLookupKeyFontFamily(query));
	}

	public void registerFont(IFont font) {
		String type = font.getFontType();
		String familyName = font.getFontFamilyName();
		if (familyName != null) {
			String familyKey = "[" + type + "]" + familyName;
			IFontFamily fontFamily = fontFamilyMap.get(familyKey);
			if (fontFamily == null) {
				fontFamily = new FontFamily(familyName);
				fontFamilyMap.put(familyKey, fontFamily);
				fontFamilies.add(fontFamily);
			}
			fontFamily.registerFont(font);
			//
			familyKey = "[Any]" + familyName;
			fontFamily = fontFamilyMap.get(familyKey);
			if (fontFamily == null) {
				fontFamily = new FontFamily(familyName);
				fontFamilyMap.put(familyKey, fontFamily);
			}
			fontFamily.registerFont(font);
		}
		String fontName = font.getFontName();
		String fontNamePostScript = font.getFontNamePostScript();
		String fontNameCanonical = font.getFontNameCanonical();
		String fontKey;
		fontKey = "[" + type + "]" + fontName;
		basicRegister(fontKey, font);
		fontKey = "[" + type + "-postscript]" + fontNamePostScript;
		basicRegister(fontKey, font);
		fontKey = "[" + type + "-canonical]" + fontNameCanonical;
		basicRegister(fontKey, font);
		fontKey = "[Any]" + fontName;
		basicRegister(fontKey, font);
		fontKey = "[Any-postscript]" + fontNamePostScript;
		basicRegister(fontKey, font);
		fontKey = "[Any-canonical]" + fontNameCanonical;
		basicRegister(fontKey, font);
		//
		fonts.add(font);
	}

	protected String removeSpaces(String name) {
		int pos = 0;
		while ((pos = name.indexOf(' ')) != -1) {
			name = name.substring(0, pos) + name.substring(pos + 1);
		}
		return name;
	}

}
