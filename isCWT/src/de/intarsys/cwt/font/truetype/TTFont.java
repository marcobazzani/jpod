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
import java.util.Arrays;
import java.util.Map;

import de.intarsys.cwt.font.FontStyle;
import de.intarsys.tools.locator.ILocator;
import de.intarsys.tools.randomaccess.IRandomAccess;
import de.intarsys.tools.stream.StreamTools;
import de.intarsys.tools.string.StringTools;

/**
 * This class represents a true type font. Currently only single font files are
 * supported.
 * <p>
 * This class is under construction and not meant for a complete TT
 * implementation.
 */
public class TTFont {
	public static final int ARG_1_AND_2_ARE_WORDS = 1;

	public static final int MORE_COMPONENTS = 32;

	public static final int SFNT_TRUETYPE = 0x00010000;

	public static final byte[] TABLE_CMAP = "cmap".getBytes(); //$NON-NLS-1$

	public static final byte[] TABLE_CVT = "cvt ".getBytes(); //$NON-NLS-1$

	public static final byte[] TABLE_FGPM = "fpgm".getBytes(); //$NON-NLS-1$

	public static final byte[] TABLE_GLYF = "glyf".getBytes(); //$NON-NLS-1$

	public static final byte[] TABLE_HEAD = "head".getBytes(); //$NON-NLS-1$

	public static final byte[] TABLE_HHEA = "hhea".getBytes(); //$NON-NLS-1$

	public static final byte[] TABLE_HMTX = "hmtx".getBytes(); //$NON-NLS-1$

	public static final byte[] TABLE_LOCA = "loca".getBytes(); //$NON-NLS-1$

	public static final byte[] TABLE_MAXP = "maxp".getBytes(); //$NON-NLS-1$

	public static final byte[] TABLE_NAME = "name".getBytes(); //$NON-NLS-1$

	public static final byte[] TABLE_OS2 = "OS/2".getBytes(); //$NON-NLS-1$

	public static final byte[] TABLE_POST = "post".getBytes();//$NON-NLS-1$

	public static final byte[] TABLE_PREP = "prep".getBytes(); //$NON-NLS-1$

	public static final int WE_HAVE_A_SCALE = 8;

	public static final int WE_HAVE_A_TWO_BY_TWO = 128;

	public static final int WE_HAVE_AN_X_AND_Y_SCALE = 64;

	static public int align(int newLength) {
		return (newLength + 3) & (~3);
	}

	public static TTFont createFromLocator(ILocator locator) throws IOException {
		TTFont result = new TTFont();
		result.setLocator(locator);
		result.initializeFromLocator();
		return result;
	}

	/** objectified versions of the "cmap" tables and subtables */
	private Map cmaps;

	// Font names in Postscript notation
	private String fontFamilyName = null;

	/** "objectified" version of the "head" table */
	private TTFontHeader fontHeader;

	/** The style of this font */
	private FontStyle fontStyle = FontStyle.REGULAR;

	/** objectified version of the "hmtx" table */
	private int[] glyphWidths;

	/** objectified version of the "hhea" table */
	private TTHorizontalHeader horizontalHeader;

	private ILocator locator;

	/** objectified version of the "os/2" table */
	private TTMetrics metrics;

	/** objectified version of the "naming" table */
	private TTNaming naming;

	/** objectified versions of the "post" table */
	private TTPostScriptInformation postScriptInformation;

	private String psName = null;

	private int sfntVersion;

	/** the parsed table directory information */
	private TTTable[] tables;

	/**
	 * Create an empty true type font.
	 */
	protected TTFont() {
		super();
	}

	public Map getCMaps() throws TrueTypeException {
		if (cmaps == null) {
			TTFontParser parser = new TTFontParser();

			try {
				cmaps = parser.parseTable_cmap(getTable(TABLE_CMAP));
			} catch (IOException e) {
				throw new TrueTypeException(e.getMessage());
			}
		}

		return cmaps;
	}

	public Map getCMapsAt(int platformID, int platformSpecificID)
			throws TrueTypeException {
		String key = StringTools.EMPTY + platformID + ":" + platformSpecificID; //$NON-NLS-1$
		Object result = getCMaps().get(key);

		if (result instanceof TTTable) {
			// not yet parsed
			TTFontParser parser = new TTFontParser();

			try {
				Map submap = parser.parseTable_cmap_subtable((TTTable) result);
				getCMaps().put(key, submap);
				result = submap;
			} catch (IOException e) {
				throw new TrueTypeException(e.getMessage());
			}
		}

		return (Map) result;
	}

	public String getFontFamilyName() {
		return fontFamilyName;
	}

	public TTFontHeader getFontHeader() throws TrueTypeException {
		if (fontHeader == null) {
			TTFontParser parser = new TTFontParser();

			try {
				fontHeader = parser.parseTable_head(getTable(TABLE_HEAD));
			} catch (IOException e) {
				throw new TrueTypeException(e.getMessage());
			}
		}

		return fontHeader;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.intarsys.font.IFont#getFontName()
	 */
	public String getFontName() {
		return getPsName();
	}

	public FontStyle getFontStyle() {
		return fontStyle;
	}

	public int getGlyphWidth(int codePoint) throws TrueTypeException {
		if (codePoint < getGlyphWidths().length) {
			return getGlyphWidths()[codePoint];
		}
		return getGlyphWidths()[getGlyphWidths().length - 1];
	}

	protected int[] getGlyphWidths() throws TrueTypeException {
		if (glyphWidths == null) {
			TTFontParser parser = new TTFontParser();

			try {
				glyphWidths = parser.parseTable_hmtx(getTable(TABLE_HMTX),
						getHorizontalHeader().getNumberOfHMetrics());
			} catch (IOException e) {
				throw new TrueTypeException(e.getMessage());
			}
		}

		return glyphWidths;
	}

	public TTHorizontalHeader getHorizontalHeader() throws TrueTypeException {
		if (horizontalHeader == null) {
			TTFontParser parser = new TTFontParser();

			try {
				horizontalHeader = parser.parseTable_hhea(getTable(TABLE_HHEA));
			} catch (IOException e) {
				throw new TrueTypeException(e.getMessage());
			}
		}

		return horizontalHeader;
	}

	public ILocator getLocator() {
		return locator;
	}

	public TTMetrics getMetrics() throws TrueTypeException {
		if (metrics == null) {
			TTFontParser parser = new TTFontParser();

			try {
				metrics = parser.parseTable_os2(getTable(TABLE_OS2));
			} catch (IOException e) {
				throw new TrueTypeException(e.getMessage());
			}
		}

		return metrics;
	}

	public TTNaming getNaming() throws TrueTypeException {
		if (naming == null) {
			TTFontParser parser = new TTFontParser();

			try {
				TTTable table = getTable(TABLE_NAME);
				if (table != null) {
					naming = parser.parseTable_name(table);
				}
			} catch (IOException e) {
				throw new TrueTypeException(e.getMessage());
			}
		}
		return naming;
	}

	public TTPostScriptInformation getPostScriptInformation()
			throws TrueTypeException {
		if (postScriptInformation == null) {
			TTFontParser parser = new TTFontParser();

			try {
				postScriptInformation = parser
						.parseTable_post(getTable(TABLE_POST));
			} catch (IOException e) {
				throw new TrueTypeException(e.getMessage());
			}
		}

		return postScriptInformation;
	}

	public String getPsName() {
		return psName;
	}

	public int getSfntVersion() {
		return sfntVersion;
	}

	public TTTable getTable(byte[] name) {
		for (int i = 0; i < getTables().length; i++) {
			TTTable current = tables[i];

			if (Arrays.equals(current.getName(), name)) {
				return current;
			}
		}

		return null;
	}

	public TTTable[] getTables() {
		return tables;
	}

	protected void initializeFromLocator() throws IOException {
		IRandomAccess random = null;
		try {
			random = getLocator().getRandomAccess();
			TTFontParser parser = new TTFontParser();
			parser.parseTables(this);
			try {
				setFontName(this);
			} catch (TrueTypeException e) {
				throw new IOException(e.getMessage());
			}

		} finally {
			StreamTools.close(random);
		}
	}

	public void removeCMapsAt(int platformID, int platformSpecificID)
			throws TrueTypeException, IOException {
		TTTable data = getTable(TTFont.TABLE_CMAP);
		if (data == null) {
			return;
		}
		IRandomAccess random = data.getRandomAccess();
		TTFontSerializer serializer = new TTFontSerializer();
		TTFontParser parser = new TTFontParser();
		try {
			random.seek(0);
			int version = parser.readShort(random);
			int count = parser.readUShort(random);
			int i = 0;
			long readPos = random.getOffset();
			long writePos = random.getOffset();
			while (i < count) {
				random.seek(readPos);
				int tempPlatformID = parser.readUShort(random);
				int tempPlatformSpecificID = parser.readUShort(random);
				int offset = parser.readInt(random);
				if (platformID != tempPlatformID
						|| platformSpecificID != tempPlatformSpecificID) {
					if (readPos != writePos) {
						random.seek(writePos);
						serializer.writeShort(random, tempPlatformID);
						serializer.writeShort(random, tempPlatformSpecificID);
						serializer.writeInt(random, offset);
					}
					writePos = writePos + 8;
				}
				readPos = readPos + 8;
				i++;
			}
			if (readPos != writePos) {
				// clear removed entry
				random.seek(writePos);
				serializer.writeShort(random, 0);
				serializer.writeShort(random, 0);
				serializer.writeInt(random, 0);
				//
				random.seek(0);
				// skip version
				random.seekBy(2);
				// decrement count
				serializer.writeShort(random, count - 1);
			}
		} finally {
			StreamTools.close(random);
		}
		cmaps = null;
	}

	protected void setFontFamilyName(String string) {
		fontFamilyName = string;
	}

	protected void setFontName(TTFont font) throws TrueTypeException {
		TTNaming tempNaming = font.getNaming();
		if (tempNaming != null) {
			font.setFontFamilyName(tempNaming
					.getValue(ITTNamingIDs.FontFamilyName));
			String styleName = tempNaming
					.getValue(ITTNamingIDs.FontSubfamilyName);
			font.setFontStyle(FontStyle.getFontStyle(styleName));
			font.setPsName(tempNaming.getValue(ITTNamingIDs.PSName));
			if (font.getPsName() == null) {
				// Here we should use operating system specific information
				// which can't be done by Java. So we use our own
				// implementation and hope we it works.
				// todo postscript name
				font.setPsName(font.getFontFamilyName() + "-" //$NON-NLS-1$
						+ font.getFontStyle().getId());
			}
		}
	}

	protected void setFontStyle(FontStyle fontStyle) {
		this.fontStyle = fontStyle;
	}

	protected void setLocator(ILocator locator) {
		this.locator = locator;
	}

	public void setPsName(String string) {
		psName = string;
	}

	protected void setSfntVersion(int sfntVersion) {
		// this is not true for all valid font files, skip version check
		// if (sfntVersion != SFNT_TRUETYPE) {
		// throw new IOException("not a valid TTF file.");
		// }

		this.sfntVersion = sfntVersion;
	}

	protected void setTables(TTTable[] tables) {
		this.tables = tables;
	}

}
