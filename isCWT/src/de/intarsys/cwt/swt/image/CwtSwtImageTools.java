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
package de.intarsys.cwt.swt.image;

import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.RGB;

public class CwtSwtImageTools {

	private static PaletteData[] GrayPalettes;

	private static PaletteData RgbPalette;

	public static PaletteData getGrayPalette(int bits) {
		int paletteIndex;

		if (GrayPalettes == null) {
			GrayPalettes = new PaletteData[4];
		}

		paletteIndex = (int) (Math.log(bits) / Math.log(2));
		if (GrayPalettes[paletteIndex] == null) {
			RGB[] colors;

			colors = new RGB[(int) Math.pow(2, bits)];
			for (int index = 0; index < colors.length; index++) {
				int value;

				value = (int) (index * (float) 255 / (colors.length - 1));
				colors[index] = new RGB(value, value, value);
			}
			GrayPalettes[paletteIndex] = new PaletteData(colors);
		}
		return GrayPalettes[paletteIndex];
	}

	public static PaletteData getRgbPalette() {
		if (RgbPalette == null) {
			RgbPalette = new PaletteData(0xFF0000, 0xFF00, 0xFF);
		}
		return RgbPalette;
	}

}
