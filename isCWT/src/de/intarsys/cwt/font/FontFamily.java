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
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * An abstraction of a fonts family.
 */
public class FontFamily implements IFontFamily {

	private final IFont[] fonts;

	private String name = "";

	private String type = "";

	public FontFamily(String name) {
		fonts = new IFont[FontStyle.COUNT];
		this.name = name;
	}

	public String getFamilyName() {
		return name;
	}

	public IFont getFont(FontStyle style) {
		return fonts[style.getIndex()];
	}

	public Iterator<IFont> getFontIterator() {
		return new Iterator<IFont>() {
			int index = 0;

			public boolean hasNext() {
				while (index < fonts.length) {
					if (fonts[index] != null) {
						return true;
					}
					index++;
				}
				return false;
			}

			public IFont next() {
				while (index < fonts.length) {
					if (fonts[index] != null) {
						return fonts[index++];
					}
					index++;
				}
				throw new NoSuchElementException("no more fonts"); //$NON-NLS-1$
			}

			public void remove() {
				throw new UnsupportedOperationException("can not remove font"); //$NON-NLS-1$
			}
		};
	}

	public IFont[] getFonts() {
		List<IFont> result = new ArrayList<IFont>();
		for (int i = 0; i < fonts.length; i++) {
			if (fonts[i] != null) {
				result.add(fonts[i]);
			}
		}
		return result.toArray(new IFont[result.size()]);
	}

	public FontStyle[] getFontStyles() {
		List<FontStyle> result = new ArrayList<FontStyle>();
		for (int i = 0; i < fonts.length; i++) {
			if (fonts[i] != null) {
				result.add(fonts[i].getFontStyle());
			}
		}
		return result.toArray(new FontStyle[result.size()]);
	}

	public String getFontType() {
		return type;
	}

	public void registerFont(IFont font) {
		fonts[font.getFontStyle().getIndex()] = font;
		type = font.getFontType();
	}
}
