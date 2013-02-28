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

/**
 * 
 */
public class FontStyle {
	/** The number of font styles */
	public static final int COUNT = 4;

	/** The enumeration of supported font styles */
	public static final FontStyle UNDEFINED = new FontStyle("?", -1); //$NON-NLS-1$

	public static final FontStyle REGULAR = new FontStyle("Regular", 0); //$NON-NLS-1$

	public static final FontStyle ITALIC = new FontStyle("Italic", 1); //$NON-NLS-1$

	public static final FontStyle BOLD = new FontStyle("Bold", 2); //$NON-NLS-1$

	public static final FontStyle BOLD_ITALIC = new FontStyle("BoldItalic", 3); //$NON-NLS-1$

	public static FontStyle getFontStyle(String name) {
		if (name == null) {
			return REGULAR;
		}
		name = name.trim().toLowerCase();
		boolean bold = false;
		boolean italic = false;
		if (name.indexOf("bold") >= 0) { //$NON-NLS-1$
			bold = true;
		}
		if (name.indexOf("italic") >= 0) { //$NON-NLS-1$
			italic = true;
		}
		if (name.indexOf("oblique") >= 0) { //$NON-NLS-1$
			italic = true;
		}
		if (bold) {
			if (italic) {
				return BOLD_ITALIC;
			} else {
				return BOLD;
			}
		} else {
			if (italic) {
				return ITALIC;
			} else {
				return REGULAR;
			}
		}
	}

	/** The external representation of the font style */
	private final String id;

	private final int index;

	private FontStyle(String label, int index) {
		this.id = label;
		this.index = index;
	}

	public FontStyle getBoldFlavor() {
		if (this == FontStyle.ITALIC) {
			return FontStyle.BOLD_ITALIC;
		} else {
			return FontStyle.BOLD;
		}
	}

	public String getId() {
		return id;
	}

	protected int getIndex() {
		return index;
	}

	public FontStyle getItalicFlavor() {
		if (this == FontStyle.BOLD) {
			return FontStyle.BOLD_ITALIC;
		} else {
			return FontStyle.ITALIC;
		}
	}

	@Override
	public String toString() {
		return getId();
	}
}
