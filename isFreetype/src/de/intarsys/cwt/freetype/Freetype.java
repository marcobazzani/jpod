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

import de.intarsys.cwt.freetype.nativec.FTLibrary;
import de.intarsys.cwt.freetype.nativec._FTNI;
import de.intarsys.nativec.type.NativeReference;

public class Freetype {

	public static final byte CURVE_TAG_CONIC = 0;

	public static final byte CURVE_TAG_CUBIC = 2;

	public static final byte CURVE_TAG_ON = 1;

	public static final int LOAD_CROP_BITMAP = 0x40;

	public static final int LOAD_DEFAULT = 0x0;

	public static final int LOAD_FORCE_AUTOHINT = 0x20;

	public static final int LOAD_IGNORE_GLOBAL_ADVANCE_WIDTH = 0x200;

	public static final int LOAD_IGNORE_TRANSFORM = 0x800;

	public static final int LOAD_LINEAR_DESIGN = 0x2000;

	public static final int LOAD_MONOCHROME = 0x1000;

	public static final int LOAD_NO_BITMAP = 0x8;

	public static final int LOAD_NO_HINTING = 0x2;

	public static final int LOAD_NO_RECURSE = 0x400;

	public static final int LOAD_NO_SCALE = 0x1;

	public static final int LOAD_PEDANTIC = 0x80;

	public static final int LOAD_RENDER = 0x4;

	public static final int LOAD_VERTICAL_LAYOUT = 0x10;

	public static final int OUTLINE_EVEN_ODD_FILL = 0x2;

	public static final int OUTLINE_HIGH_PRECISION = 0x100;

	public static final int OUTLINE_IGNORE_DROPOUTS = 0x8;

	public static final int OUTLINE_NONE = 0x0;

	public static final int OUTLINE_OWNER = 0x1;

	public static final int OUTLINE_REVERSE_FILL = 0x4;

	public static final int OUTLINE_SINGLE_PASS = 0x200;

	public static final int RENDER_MODE_LCD = 3;

	public static final int RENDER_MODE_LCD_V = 4;

	public static final int RENDER_MODE_LIGHT = 1;

	public static final int RENDER_MODE_MONO = 2;

	public static final int RENDER_MODE_NORMAL = 0;

	static private _FTNI ftni;

	static {
		ftni = new _FTNI();
	}

	static public Library initFreeType() {
		NativeReference refLibrary = NativeReference.create(FTLibrary.META);
		int rc = ftni.InitFreeType(refLibrary);
		FTLibrary library = (FTLibrary) refLibrary.getValue();
		return new Library(ftni, library);
	}

	private Freetype() {
		// instance creation prohibited
	}
}
