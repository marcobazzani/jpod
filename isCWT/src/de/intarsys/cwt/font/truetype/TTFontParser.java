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

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.intarsys.tools.randomaccess.IRandomAccess;
import de.intarsys.tools.stream.StreamTools;
import de.intarsys.tools.string.StringTools;

/**
 * Apple TT specification<br/>
 * http://developer.apple.com/fonts/TTRefMan/RM06/Chap6.html <br/>
 * Microsoft OpenType spec <br/>
 * http://www.microsoft.com/typography/otspec/otff.htm
 */
public class TTFontParser {
	// todo support indexed fonts

	/** the parsed table directory information */
	private TTTable[] tables;

	public TTFontParser() {
	}

	public Map parseTable_cmap(TTTable data) throws IOException {
		Map result = new HashMap();
		IRandomAccess random = data.getRandomAccess();
		try {
			random.seek(0);
			// skip version
			random.seekBy(2);
			int count = readUShort(random);
			for (int i = 0; i < count; i++) {
				int platformID = readUShort(random);
				int platformSpecificID = readUShort(random);
				int offset = readInt(random);
				TTTable subtable = new TTTable(data.getFont(), data.getOffset()
						+ offset, -1);
				String key = StringTools.EMPTY + platformID
						+ ":" + platformSpecificID; //$NON-NLS-1$
				result.put(key, subtable);
			}
			return result;
		} finally {
			StreamTools.close(random);
		}
	}

	public Map parseTable_cmap_subtable(TTTable data) throws IOException {
		Map result = null;
		IRandomAccess random = data.getRandomAccess();
		try {
			random.seek(0);
			int format = readUShort(random);
			switch (format) {
			case 0:
				result = readCMapFormat0(random);
				break;
			case 4:
				result = readCMapFormat4(random);
				break;
			case 6:
				result = readCMapFormat6(random);
				break;
			}
			return result;
		} finally {
			StreamTools.close(random);
		}
	}

	/**
	 * This table gives global information about the font.
	 * 
	 * <pre>
	 *  Type        Name        Description
	 *  FIXED        Table version number        0x00010000 for version 1.0.
	 *  FIXED        fontRevision        Set by font manufacturer.
	 *  ULONG        checkSumAdjustment        To compute:  set it to 0, sum the entire font as ULONG, then store 0xB1B0AFBA - sum.
	 *  ULONG        magicNumber        Set to 0x5F0F3CF5.
	 *  USHORT        flags        Bit 0 - baseline for font at y=0;
	 *                                         Bit 1 - left sidebearing at x=0;
	 *                                         Bit 2 - instructions may depend on point size;
	 *                                         Bit 3 - force ppem to integer values for all internal scaler math; may use fractional ppem sizes if this bit is clear;
	 *                                         Bit 4 - instructions may alter advance width (the advance widths might not scale linearly);
	 *                                         Note: All other bits must be zero.
	 *  USHORT        unitsPerEm        Valid range is from 16 to 16384
	 *  longDateTime        created        International date (8-byte field).
	 *  longDateTime        modified        International date (8-byte field).
	 *  FWORD        xMin        For all glyph bounding boxes.
	 *  FWORD        yMin        For all glyph bounding boxes.
	 *  FWORD        xMax        For all glyph bounding boxes.
	 *  FWORD        yMax        For all glyph bounding boxes.
	 *  USHORT        macStyle        Bit 0 bold (if set to 1); Bit 1 italic (if set to 1)Bits 2-15 reserved (set to 0).
	 *  USHORT        lowestRecPPEM        Smallest readable size in pixels.
	 *  SHORT        fontDirectionHint         0   Fully mixed directional glyphs; 1   Only strongly left to right; 2   Like 1 but also contains neutrals ;-1   Only strongly right to left;-2   Like -1 but also contains neutrals.
	 *  SHORT        indexToLocFormat        0 for short offsets, 1 for long.
	 *  SHORT        glyphDataFormat        0 for current format.
	 * </pre>
	 * 
	 * @param data
	 *            docme
	 * 
	 * @return docme
	 * 
	 * @throws IOException
	 *             docme
	 */
	public TTFontHeader parseTable_head(TTTable data) throws IOException {
		TTFontHeader result = new TTFontHeader();
		IRandomAccess random = data.getRandomAccess();
		try {
			random.seek(0);
			// skip version
			// skip font Revision
			// skip checksum
			// skip magic number
			random.seekBy(16);
			result.setFlags(readUShort(random));
			result.setUnitsPerEm(readUShort(random));
			// skip created
			// skip modified
			random.seekBy(16);
			result.setXMin(readShort(random));
			result.setYMin(readShort(random));
			result.setXMax(readShort(random));
			result.setYMax(readShort(random));
			result.setMacStyle(readUShort(random));
			// skip smallest size in pixels
			// skip font direction hint
			random.seekBy(4);
			result.setShortLocationFormat(readShort(random) == 0);
			return result;
		} finally {
			StreamTools.close(random);
		}
	}

	/**
	 * <pre>
	 *  FIXED        Table version number        0x00010000 for version 1.0.
	 *  FWORD        Ascender        Typographic ascent.
	 *  FWORD        Descender        Typographic descent.
	 *  FWORD        LineGap        Typographic line gap. Negative LineGap values are treated as zero in Windows 3.1, System 6, and System 7.
	 *  UFWORD        advanceWidthMax        Maximum advance width value in ‘hmtx’ table.
	 *  FWORD        minLeftSideBearing        Minimum left sidebearing value in ‘hmtx’ table.
	 *  FWORD        minRightSideBearing        Minimum right sidebearing value; calculated as Min(aw - lsb - (xMax - xMin)).
	 *  FWORD        xMaxExtent        Max(lsb + (xMax - xMin)).
	 *  SHORT        caretSlopeRise        Used to calculate the slope of the cursor (rise/run); 1 for vertical.
	 *  SHORT        caretSlopeRun        0 for vertical.
	 *  SHORT        (reserved)        set to 0
	 *  SHORT        (reserved)        set to 0
	 *  SHORT        (reserved)        set to 0
	 *  SHORT        (reserved)        set to 0
	 *  SHORT        (reserved)        set to 0
	 *  SHORT        metricDataFormat        0 for current format.
	 *  USHORT        numberOfHMetrics        Number of hMetric entries in  ‘hmtx’ table; may be smaller than the total number of glyphs in the font.
	 * </pre>
	 * 
	 * @param data
	 *            docme
	 * 
	 * @return docme
	 * 
	 * @throws IOException
	 *             docme
	 */
	public TTHorizontalHeader parseTable_hhea(TTTable data) throws IOException {
		TTHorizontalHeader result = new TTHorizontalHeader();
		IRandomAccess random = data.getRandomAccess();
		try {
			random.seek(0);
			// skip version
			random.seekBy(4);
			result.setAscender(readShort(random));
			result.setDescender(readShort(random));
			result.setLineGap(readShort(random));
			result.setAdvanceWidthMax(readUShort(random));
			result.setMinLeftSideBearing(readShort(random));
			result.setMinRightSideBearing(readShort(random));
			result.setXMaxExtent(readShort(random));
			result.setCaretSlopeRise(readShort(random));
			result.setCaretSlopeRun(readShort(random));
			// skip
			random.seekBy(12);
			result.setNumberOfHMetrics(readUShort(random));
			// skip rest
			return result;
		} finally {
			StreamTools.close(random);
		}
	}

	/**
	 * <pre>
	 * The type longHorMetric is defined as an array where each element has
	 * two parts: the advance width, which is of type USHORT, and the left side
	 * bearing, which is of type SHORT. These fields are in font design units.
	 *  typedef struct         _longHorMetric {
	 *                 USHORT        advanceWidth;
	 *                 SHORT                lsb;
	 *  }  longHorMetric;
	 * 
	 *  Field Type Description
	 *  hMetrics longHorMetric [numberOfHMetrics] Paired advance width and
	 *                                 left side bearing values for each glyph. The value
	 *                                 numOfHMetrics comes from the 'hhea' table. If the font is
	 *                                 monospaced, only one entry need be in the array, but that
	 *                                 entry is required. The last entry applies to all subsequent
	 *                                 glyphs.
	 *  leftSideBearing SHORT[ ] Here the advanceWidth is assumed to be the
	 *                                 same as the advanceWidth for the last entry above. The
	 *                                 number of entries in this array is derived from numGlyphs
	 *                                 (from 'maxp' table) minus numberOfHMetrics. This generally
	 *                                 is used with a run of monospaced glyphs (e.g., Kanji fonts
	 *                                 or Courier fonts). Only one run is allowed and it must be
	 *                                 at the end. This allows a monospaced font to vary the left
	 *                                 side bearing values for each glyph.
	 * </pre>
	 * 
	 * @param data
	 *            docme
	 * @param count
	 *            docme
	 * 
	 * @return docme
	 * 
	 * @throws IOException
	 *             docme
	 */
	public int[] parseTable_hmtx(TTTable data, int count) throws IOException {
		int[] widths = new int[count];
		IRandomAccess random = data.getRandomAccess();
		try {
			random.seek(0);
			for (int i = 0; i < count; i++) {
				widths[i] = readUShort(random);
				readShort(random);
			}
			return widths;
		} finally {
			StreamTools.close(random);
		}
	}

	public int[] parseTable_loca(TTTable data, boolean shortLocationFormat)
			throws IOException {
		int count = 0;
		int[] locations;
		IRandomAccess random = data.getRandomAccess();
		try {
			random.seek(0);
			if (shortLocationFormat) {
				count = (int) random.getLength() / 2;
				locations = new int[count];

				for (int i = 0; i < count; i++) {
					locations[i] = readUShort(random) << 1;
				}
			} else {
				count = (int) random.getLength() / 4;
				locations = new int[count];

				for (int i = 0; i < count; i++) {
					locations[i] = readInt(random);
				}
			}
			return locations;
		} finally {
			StreamTools.close(random);
		}
	}

	public TTNaming parseTable_name(TTTable data) throws IOException {
		TTNaming result = new TTNaming();
		IRandomAccess random = data.getRandomAccess();
		try {
			random.seek(0);
			// skip format
			random.seekBy(2);
			// USHORT count Number of name records.
			int count = readUShort(random);
			// USHORT stringOffset Offset to start of string storage (from start
			// of
			// table).
			int stringTableOffset = readUShort(random);
			// Namerecords follow
			for (int i = 0; i < count; i++) {
				TTNameRecord record = readNameRecord(random, stringTableOffset);
				// filter for Microsoft-Plattform and Language=American
				if ((record.getPlatformID() == 3)
						&& (record.getLanguageID() == 1033)) {
					result.add(record.getNameID(), record);
				}
			}
			return result;
		} finally {
			StreamTools.close(random);
		}
	}

	/**
	 * <pre>
	 *  USHORT        version        0x0001
	 *  SHORT        xAvgCharWidth;
	 *  USHORT        usWeightClass;
	 *  USHORT        usWidthClass;
	 *  SHORT        fsType;
	 *  SHORT        ySubscriptXSize;
	 *  SHORT        ySubscriptYSize;
	 *  SHORT        ySubscriptXOffset;
	 *  SHORT        ySubscriptYOffset;
	 *  SHORT        ySuperscriptXSize;
	 *  SHORT        ySuperscriptYSize;
	 *  SHORT        ySuperscriptXOffset;
	 *  SHORT        ySuperscriptYOffset;
	 *  SHORT        yStrikeoutSize;
	 *  SHORT        yStrikeoutPosition;
	 *  SHORT        sFamilyClass;
	 *  PANOSE        panose;
	 *  ULONG        ulUnicodeRange1        Bits 0–31
	 *  ULONG        ulUnicodeRange2        Bits 32–63
	 *  ULONG        ulUnicodeRange3        Bits 64–95
	 *  ULONG        ulUnicodeRange4        Bits 96–127
	 *  CHAR        achVendID[4];
	 *  USHORT        fsSelection;
	 *  USHORT        usFirstCharIndex
	 *  USHORT        usLastCharIndex
	 *  USHORT        sTypoAscender
	 *  USHORT        sTypoDescender
	 *  USHORT        sTypoLineGap
	 *  USHORT        usWinAscent
	 *  USHORT        usWinDescent
	 *  ULONG        ulCodePageRange1        Bits 0-31
	 *  ULONG        ulCodePageRange2        Bits 32-63
	 *  SHORT                  sxHeight
	 *         SHORT                 sCapHeight
	 *         USHORT                 usDefaultChar
	 *         USHORT                 usBreakChar
	 *         USHORT                 usMaxContext
	 * </pre>
	 * 
	 * @param data
	 *            docme
	 * 
	 * @return docme
	 * 
	 * @throws IOException
	 *             docme
	 */
	public TTMetrics parseTable_os2(TTTable data) throws IOException {
		TTMetrics result = new TTMetrics();
		IRandomAccess random = data.getRandomAccess();
		try {
			random.seek(0);
			// skip version
			random.seekBy(2);
			result.setXAvgCharWidth(readShort(random));
			result.setUsWeightClass(readUShort(random));
			result.setUsWidthClass(readUShort(random));
			result.setFsType(readUShort(random));
			result.setYSubscriptXSize(readShort(random));
			result.setYSubscriptYSize(readShort(random));
			result.setYSubscriptXOffset(readShort(random));
			result.setYSubscriptYOffset(readShort(random));
			result.setYSuperscriptXSize(readShort(random));
			result.setYSuperscriptYSize(readShort(random));
			result.setYSuperscriptXOffset(readShort(random));
			result.setYSuperscriptYOffset(readShort(random));
			result.setYStrikeoutSize(readShort(random));
			result.setYStrikeoutPosition(readShort(random));
			result.setSFamilyClass(readShort(random));
			result.setPanose(readBytes(random, 10));
			// skip unicode ranges
			// skip vendor id
			random.seekBy(20);
			result.setFsSelection(readUShort(random));
			result.setUsFirstCharIndex(readUShort(random));
			result.setUsLastCharIndex(readUShort(random));
			result.setSTypoAscender(readShort(random));
			result.setSTypoDescender(readShort(random));
			result.setSTypoLineGap(readShort(random));
			result.setUsWinAscent(readUShort(random));
			result.setUsWinDescent(readUShort(random));
			result.setSxHeight(readShort(random));
			result.setSCapHeight(readShort(random));
			result.setUsDefaultChar(readUShort(random));
			result.setUsMaxContext(readUShort(random));
			// skip rest
			return result;
		} finally {
			StreamTools.close(random);
		}
	}

	/**
	 * <pre>
	 * Type         Name                 Description
	 * Fixed         Version         0x00010000 for version 1.0
	 *                                         0x00020000 for version 2.0
	 *                                         0x00025000 for version 2.5 (deprecated)
	 *                                         0x00030000 for version 3.0
	 * Fixed         italicAngle         Italic angle in counter-clockwise degrees from the vertical. Zero for upright text, negative for text that leans to the right (forward).
	 * FWord         underlinePosition         This is the suggested distance of the top of the underline from the baseline (negative values indicate below baseline).
	 *                                                 The PostScript definition of this FontInfo dictionary key (the y coordinate of the center of the stroke) is not used for historical reasons. The value of the PostScript key may be calculated by subtracting half the underlineThickness from the value of this field.
	 * FWord         underlineThickness         Suggested values for the underline thickness.
	 * ULONG         isFixedPitch         Set to 0 if the font is proportionally spaced, non-zero if the font is not proportionally spaced (i.e. monospaced).
	 * ULONG         minMemType42         Minimum memory usage when an OpenType font is downloaded.
	 * ULONG         maxMemType42         Maximum memory usage when an OpenType font is downloaded.
	 * ULONG         minMemType1         Minimum memory usage when an OpenType font is downloaded as a Type 1 font.
	 * ULONG         maxMemType1         Maximum memory usage when an OpenType font is downloaded as a Type 1 font.
	 * </pre>
	 * 
	 * @param data
	 *            docme
	 * 
	 * @return docme
	 * 
	 * @throws IOException
	 *             docme
	 */
	public TTPostScriptInformation parseTable_post(TTTable data)
			throws IOException {
		TTPostScriptInformation result = new TTPostScriptInformation();
		IRandomAccess random = data.getRandomAccess();
		try {
			random.seek(0);
			result.setVersion(readFixed(random));
			result.setItalicAngle(readFixed(random));
			result.setUnderlinePosition(readShort(random));
			result.setUnderlineThickness(readShort(random));
			return result;
		} finally {
			StreamTools.close(random);
		}
	}

	public TTTable[] parseTables(TTFont font) throws IOException {
		IRandomAccess random = null;
		try {
			random = font.getLocator().getRandomAccess();
			readTables(font, random);
			font.setTables(tables);
		} finally {
			StreamTools.close(random);
		}
		return tables;
	}

	public byte[] readBytes(IRandomAccess random, int count) throws IOException {
		byte[] bytes = new byte[count];
		int bytesRead = random.read(bytes);
		if (bytesRead < bytes.length) {
			return null;
		}
		return bytes;
	}

	protected Map readCMapFormat0(IRandomAccess random) throws IOException {
		Map result = new HashMap();

		// skip length
		// skip language
		random.seekBy(4);

		for (int i = 0; i < 256; i++) {
			int glyphCode = random.read();
			result.put(new Integer(i), new Integer(glyphCode));
		}

		return result;
	}

	protected Map readCMapFormat4(IRandomAccess random) throws IOException {
		Map result = new HashMap();
		int length = readUShort(random);

		// skip language
		random.seekBy(2);

		int segCount = readUShort(random) / 2;

		// skip hints
		random.seekBy(6);

		int[] endIndices = new int[segCount];

		for (int i = 0; i < segCount; i++) {
			endIndices[i] = readUShort(random);
		}

		// skip reserved
		random.seekBy(2);

		int[] startIndices = new int[segCount];

		for (int i = 0; i < segCount; i++) {
			startIndices[i] = readUShort(random);
		}

		int[] deltas = new int[segCount];

		for (int i = 0; i < segCount; i++) {
			deltas[i] = readUShort(random);
		}

		int[] rangeOffsets = new int[segCount];

		for (int i = 0; i < segCount; i++) {
			rangeOffsets[i] = readUShort(random);
		}

		int glyphIndexCount = (length / 2) - 8 - (segCount * 4);
		int[] glyphIndices = new int[glyphIndexCount];

		for (int i = 0; i < glyphIndexCount; i++) {
			glyphIndices[i] = readUShort(random);
		}

		// now construct map
		int glyphCode;

		for (int i = 0; i < segCount; i++) {
			for (int charCode = startIndices[i]; (charCode <= endIndices[i])
					&& (charCode != 0xffff); charCode++) {
				if (rangeOffsets[i] == 0) {
					glyphCode = (deltas[i] + charCode) & 0xffff;
				} else {
					// index as described in open font specification
					int index = (charCode - startIndices[i])
							+ (rangeOffsets[i] / 2);

					// try emulate c pointer arithmetic..
					index = index - segCount + i;
					glyphCode = glyphIndices[index];

					if (glyphCode != 0) {
						glyphCode = (deltas[i] + glyphCode) & 0xffff;
					}
				}

				result.put(new Integer(charCode), new Integer(glyphCode));
			}
		}

		return result;
	}

	protected Map readCMapFormat6(IRandomAccess random) throws IOException {
		Map result = new HashMap();

		// skip length
		// skip language
		random.seekBy(4);

		int firstIndex = readUShort(random);
		int count = readUShort(random);
		int lastIndex = firstIndex + count;

		for (int i = firstIndex; i < lastIndex; i++) {
			int glyphCode = readUShort(random);
			result.put(new Integer(i), new Integer(glyphCode));
		}

		return result;
	}

	public float readFixed(IRandomAccess random) throws IOException {
		int i = readInt(random);
		boolean negative = false;

		if (i < 0) {
			negative = true;
			i *= -1;
		}

		float hi = (i >> 16);
		float low = (i & 0x0000FFFF);

		while (low >= 1) {
			low = low / 10;
		}

		if (negative) {
			return -1 * (hi + low);
		}

		return hi + low;
	}

	public int readInt(IRandomAccess random) throws IOException {
		int b1 = random.read();
		int b2 = random.read();
		int b3 = random.read();
		int b4 = random.read();

		if ((b1 | b2 | b3 | b4) < 0) {
			throw new IOException("unexpected end of stream"); //$NON-NLS-1$
		}

		return ((b1 << 24) + (b2 << 16) + (b3 << 8) + b4);
	}

	protected TTNameRecord readNameRecord(IRandomAccess random,
			int stringTableOffset) throws IOException {
		TTNameRecord record = new TTNameRecord();

		// USHORT platformID Platform ID.
		record.setPlatformID(readUShort(random));

		// USHORT encodingID Platform-specific encoding ID.
		record.setEncodingID(readUShort(random));

		// USHORT languageID Language ID.
		record.setLanguageID(readUShort(random));

		// USHORT nameID Name ID.
		record.setNameID(readUShort(random));

		// USHORT length String length (in bytes).
		record.setLength(readUShort(random));

		// USHORT offset String offset from start of storage area (in bytes).
		int nameOffset = readUShort(random);
		byte[] value = new byte[record.getLength()];

		random.mark();
		random.seek(stringTableOffset + nameOffset);
		random.read(value, 0, record.getLength());
		record.setValue(new String(value, "UTF-16BE")); //$NON-NLS-1$
		random.reset();

		return record;
	}

	public short readShort(IRandomAccess random) throws IOException {
		int b1 = random.read();
		int b2 = random.read();

		if ((b1 | b2) < 0) {
			throw new IOException("unexpected end of stream"); //$NON-NLS-1$
		}

		return (short) ((b1 << 8) + b2);
	}

	public TTTable readTable(TTFont font, IRandomAccess random)
			throws IOException {
		byte[] name = readBytes(random, 4);
		int checksum = readInt(random);
		int offset = readInt(random);
		int length = readInt(random);

		TTTable result = new TTTable(font, offset, length);
		result.setName(name);
		result.setChecksum(checksum);
		return result;
	}

	public void readTables(TTFont font, IRandomAccess random)
			throws IOException {
		font.setSfntVersion(readInt(random));

		int tableCount = readUShort(random);

		// skip search range
		// skip entry selector
		// skip range shift
		random.seekBy(6);
		tables = new TTTable[tableCount];

		for (int i = 0; i < tableCount; i++) {
			tables[i] = readTable(font, random);
		}
	}

	public long readUInt(IRandomAccess random) throws IOException {
		int b1 = random.read();
		int b2 = random.read();
		int b3 = random.read();
		int b4 = random.read();

		if ((b1 | b2 | b3 | b4) < 0) {
			throw new IOException("unexpected end of stream"); //$NON-NLS-1$
		}

		return (((long) b1 << 24) + (b2 << 16) + (b3 << 8) + b4);
	}

	public int readUShort(IRandomAccess random) throws IOException {
		int b1 = random.read();
		int b2 = random.read();

		if ((b1 | b2) < 0) {
			throw new IOException("unexpected end of stream"); //$NON-NLS-1$
		}

		return (b1 << 8) + b2;
	}

}
