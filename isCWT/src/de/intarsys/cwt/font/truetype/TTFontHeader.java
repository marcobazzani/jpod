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
package de.intarsys.cwt.font.truetype;

/**
 * 
 */
public class TTFontHeader {
	public static int MACSTYLE_BOLD = 1 << 0; // Bit 0

	public static int MACSTYLE_ITALIC = 1 << 1; // Bit 1

	public static int MACSTYLE_UNDERLINE = 1 << 2;

	public static int MACSTYLE_OUTLINE = 1 << 3;

	public static int MACSTYLE_SHADOW = 1 << 4;

	public static int MACSTYLE_CONDENSED = 1 << 5;

	public static int MACSTYLE_EXTENDED = 1 << 6;

	private int flags;

	private int unitsPerEm;

	private short xMin;

	private short yMin;

	private short xMax;

	private short yMax;

	private int macStyle;

	private boolean shortLocationFormat;

	public int getFlags() {
		return flags;
	}

	public int getMacStyle() {
		return macStyle;
	}

	public int getUnitsPerEm() {
		return unitsPerEm;
	}

	public short getXMax() {
		return xMax;
	}

	public short getXMin() {
		return xMin;
	}

	public short getYMax() {
		return yMax;
	}

	public short getYMin() {
		return yMin;
	}

	public boolean isShortLocationFormat() {
		return shortLocationFormat;
	}

	public void setFlags(int i) {
		flags = i;
	}

	public void setMacStyle(int i) {
		macStyle = i;
	}

	public void setShortLocationFormat(boolean b) {
		shortLocationFormat = b;
	}

	public void setUnitsPerEm(int i) {
		unitsPerEm = i;
	}

	public void setXMax(short i) {
		xMax = i;
	}

	public void setXMin(short i) {
		xMin = i;
	}

	public void setYMax(short i) {
		yMax = i;
	}

	public void setYMin(short i) {
		yMin = i;
	}
}
