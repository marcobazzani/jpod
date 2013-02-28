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
public class TTHorizontalHeader {
	private short ascender;

	private short descender;

	private short lineGap;

	private int advanceWidthMax;

	private short minLeftSideBearing;

	private short minRightSideBearing;

	private short xMaxExtent;

	private short caretSlopeRise;

	private short caretSlopeRun;

	private int numberOfHMetrics;

	public int getAdvanceWidthMax() {
		return advanceWidthMax;
	}

	public short getAscender() {
		return ascender;
	}

	public short getCaretSlopeRise() {
		return caretSlopeRise;
	}

	public short getCaretSlopeRun() {
		return caretSlopeRun;
	}

	public short getDescender() {
		return descender;
	}

	public short getLineGap() {
		return lineGap;
	}

	public short getMinLeftSideBearing() {
		return minLeftSideBearing;
	}

	public short getMinRightSideBearing() {
		return minRightSideBearing;
	}

	public int getNumberOfHMetrics() {
		return numberOfHMetrics;
	}

	public short getXMaxExtent() {
		return xMaxExtent;
	}

	public void setAdvanceWidthMax(int i) {
		advanceWidthMax = i;
	}

	public void setAscender(short s) {
		ascender = s;
	}

	public void setCaretSlopeRise(short s) {
		caretSlopeRise = s;
	}

	public void setCaretSlopeRun(short s) {
		caretSlopeRun = s;
	}

	public void setDescender(short s) {
		descender = s;
	}

	public void setLineGap(short s) {
		lineGap = s;
	}

	public void setMinLeftSideBearing(short s) {
		minLeftSideBearing = s;
	}

	public void setMinRightSideBearing(short s) {
		minRightSideBearing = s;
	}

	public void setNumberOfHMetrics(int i) {
		numberOfHMetrics = i;
	}

	public void setXMaxExtent(short s) {
		xMaxExtent = s;
	}
}
