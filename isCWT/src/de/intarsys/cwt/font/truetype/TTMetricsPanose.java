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
public class TTMetricsPanose {
	public static int bFamilyType = 0;

	public static int bSerifStyle = 1;

	public static int bWeight = 2;

	public static int bProportion = 3;

	public static int bContrast = 4;

	public static int bStrokeVariation = 5;

	public static int bArmStyle = 6;

	public static int bLetterform = 7;

	public static int bMidline = 8;

	public static int bXHeight = 9;

	public static int cProportionMonospaced = 9;

	private byte[] panose;

	public TTMetricsPanose(byte[] panoseBytes) {
		if ((panoseBytes == null) || (panoseBytes.length != 10)) {
			throw new IllegalArgumentException(
					"TrueType table OS/2: panose bytes must not be null and exactly 10 bytes long"); //$NON-NLS-1$
		}
		this.panose = panoseBytes;
	}

	public byte[] getPanose() {
		return panose;
	}

	public boolean isProportionMonospaced() {
		return getPanose()[bProportion] == cProportionMonospaced;
	}
}
