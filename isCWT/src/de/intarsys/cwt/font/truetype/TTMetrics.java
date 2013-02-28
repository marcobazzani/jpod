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
public class TTMetrics {
	private short xAvgCharWidth;

	private int usWeightClass;

	private int usWidthClass;

	private int fsType;

	private short ySubscriptXSize;

	private short ySubscriptYSize;

	private short ySubscriptXOffset;

	private short ySubscriptYOffset;

	private short ySuperscriptXSize;

	private short ySuperscriptYSize;

	private short ySuperscriptXOffset;

	private short ySuperscriptYOffset;

	private short yStrikeoutSize;

	private short yStrikeoutPosition;

	private short sFamilyClass;

	private TTMetricsPanose panose;

	private byte[] achVendID = new byte[4];

	private int fsSelection;

	private int usFirstCharIndex;

	private int usLastCharIndex;

	private short sTypoAscender;

	private short sTypoDescender;

	private short sTypoLineGap;

	private int usWinAscent;

	private int usWinDescent;

	private short sxHeight;

	private short sCapHeight;

	private int usDefaultChar;

	private int usBreakChar;

	private int usMaxContext;

	public byte[] getAchVendID() {
		return achVendID;
	}

	public int getFsSelection() {
		return fsSelection;
	}

	public int getFsType() {
		return fsType;
	}

	public TTMetricsPanose getPanose() {
		return panose;
	}

	public short getSCapHeight() {
		return sCapHeight;
	}

	public short getSFamilyClass() {
		return sFamilyClass;
	}

	public short getSTypoAscender() {
		return sTypoAscender;
	}

	public short getSTypoDescender() {
		return sTypoDescender;
	}

	public short getSTypoLineGap() {
		return sTypoLineGap;
	}

	public short getSxHeight() {
		return sxHeight;
	}

	public int getUsBreakChar() {
		return usBreakChar;
	}

	public int getUsDefaultChar() {
		return usDefaultChar;
	}

	public int getUsFirstCharIndex() {
		return usFirstCharIndex;
	}

	public int getUsLastCharIndex() {
		return usLastCharIndex;
	}

	public int getUsMaxContext() {
		return usMaxContext;
	}

	public int getUsWeightClass() {
		return usWeightClass;
	}

	public int getUsWidthClass() {
		return usWidthClass;
	}

	public int getUsWinAscent() {
		return usWinAscent;
	}

	public int getUsWinDescent() {
		return usWinDescent;
	}

	public short getXAvgCharWidth() {
		return xAvgCharWidth;
	}

	public short getYStrikeoutPosition() {
		return yStrikeoutPosition;
	}

	public short getYStrikeoutSize() {
		return yStrikeoutSize;
	}

	public short getYSubscriptXOffset() {
		return ySubscriptXOffset;
	}

	public short getYSubscriptXSize() {
		return ySubscriptXSize;
	}

	public short getYSubscriptYOffset() {
		return ySubscriptYOffset;
	}

	public short getYSubscriptYSize() {
		return ySubscriptYSize;
	}

	public short getYSuperscriptXOffset() {
		return ySuperscriptXOffset;
	}

	public short getYSuperscriptXSize() {
		return ySuperscriptXSize;
	}

	public short getYSuperscriptYOffset() {
		return ySuperscriptYOffset;
	}

	public short getYSuperscriptYSize() {
		return ySuperscriptYSize;
	}

	public void setAchVendID(byte[] bs) {
		achVendID = bs;
	}

	public void setFsSelection(int i) {
		fsSelection = i;
	}

	public void setFsType(int s) {
		fsType = s;
	}

	public void setPanose(byte[] bs) {
		if ((bs != null) && (bs.length == 10)) {
			panose = new TTMetricsPanose(bs);
		}
	}

	public void setSCapHeight(short capHeight) {
		sCapHeight = capHeight;
	}

	public void setSFamilyClass(short s) {
		sFamilyClass = s;
	}

	public void setSTypoAscender(short s) {
		sTypoAscender = s;
	}

	public void setSTypoDescender(short s) {
		sTypoDescender = s;
	}

	public void setSTypoLineGap(short s) {

		sTypoLineGap = s;
	}

	public void setSxHeight(short sxHeight) {
		this.sxHeight = sxHeight;
	}

	public void setUsBreakChar(int usBreakChar) {
		this.usBreakChar = usBreakChar;
	}

	public void setUsDefaultChar(int usDefaultChar) {
		this.usDefaultChar = usDefaultChar;
	}

	public void setUsFirstCharIndex(int i) {
		usFirstCharIndex = i;
	}

	public void setUsLastCharIndex(int i) {
		usLastCharIndex = i;
	}

	public void setUsMaxContext(int usMaxContext) {
		this.usMaxContext = usMaxContext;
	}

	public void setUsWeightClass(int i) {
		usWeightClass = i;
	}

	public void setUsWidthClass(int i) {
		usWidthClass = i;
	}

	public void setUsWinAscent(int i) {
		usWinAscent = i;
	}

	public void setUsWinDescent(int i) {
		usWinDescent = i;
	}

	public void setXAvgCharWidth(short s) {
		xAvgCharWidth = s;
	}

	public void setYStrikeoutPosition(short s) {
		yStrikeoutPosition = s;
	}

	public void setYStrikeoutSize(short s) {
		yStrikeoutSize = s;
	}

	public void setYSubscriptXOffset(short s) {
		ySubscriptXOffset = s;
	}

	public void setYSubscriptXSize(short s) {
		ySubscriptXSize = s;
	}

	public void setYSubscriptYOffset(short s) {
		ySubscriptYOffset = s;
	}

	public void setYSubscriptYSize(short s) {
		ySubscriptYSize = s;
	}

	public void setYSuperscriptXOffset(short s) {
		ySuperscriptXOffset = s;
	}

	public void setYSuperscriptXSize(short s) {
		ySuperscriptXSize = s;
	}

	public void setYSuperscriptYOffset(short s) {
		ySuperscriptYOffset = s;
	}

	public void setYSuperscriptYSize(short s) {
		ySuperscriptYSize = s;
	}
}
