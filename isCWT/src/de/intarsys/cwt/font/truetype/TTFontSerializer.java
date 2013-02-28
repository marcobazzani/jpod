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

import de.intarsys.tools.randomaccess.IRandomAccess;
import de.intarsys.tools.stream.StreamTools;

/**
 * 
 */
public class TTFontSerializer {

	private TTFont font;

	private int tableOffset = 0;

	public TTFontSerializer() {
		super();
	}

	public TTFont getFont() {
		return font;
	}

	public void write(IRandomAccess random, TTFont pFont) throws IOException {
		this.font = pFont;
		writeFontHeader(random);
		writeTableDirectory(random);
		writeTables(random);
	}

	public void write_loca(IRandomAccess random, int[] locations,
			boolean shortFormat) throws IOException {
		for (int i = 0; i < locations.length; i++) {
			if (shortFormat) {
				writeShort(random, locations[i] >> 1);
			} else {
				writeInt(random, locations[i]);
			}
		}
	}

	public void write_name(IRandomAccess random, TTNameRecord[] names)
			throws IOException {
		int offset = 6 + (names.length * 12);
		writeShort(random, 0);
		writeShort(random, names.length);
		writeShort(random, offset);
		int nameOffset = 0;
		for (int i = 0; i < names.length; i++) {
			writeShort(random, names[i].getPlatformID());
			writeShort(random, names[i].getEncodingID());
			writeShort(random, names[i].getLanguageID());
			writeShort(random, names[i].getNameID());
			writeShort(random, names[i].getLength());
			writeShort(random, nameOffset);
			nameOffset += names[i].getLength();
		}
		for (int i = 0; i < names.length; i++) {
			writeBytes(
					random,
					names[i].getValue().getBytes("UTF-16BE"), 0, names[i].getLength()); //$NON-NLS-1$
		}
	}

	public void writeByte(IRandomAccess random, int value) throws IOException {
		random.write(value);
	}

	public void writeBytes(IRandomAccess random, byte[] value, int off, int len)
			throws IOException {
		random.write(value, off, len);
	}

	/**
	 * <pre>
	 * Fixed sfnt version 0x00010000 for version 1.0.
	 * USHORT numTables Number of tables.
	 * USHORT searchRange (Maximum power of 2 &lt;= numTables) x 16.
	 * USHORT entrySelector Log2(maximum power of 2 &lt;= numTables).
	 * USHORT rangeShift NumTables x 16-searchRange.
	 * </pre>
	 * 
	 * @throws IOException
	 */
	protected void writeFontHeader(IRandomAccess random) throws IOException {
		int numTables = getFont().getTables().length;
		int maxPower = 1;
		int log2MaxPower = 0;
		while (maxPower <= numTables) {
			maxPower = maxPower << 1;
			log2MaxPower += 1;
		}
		if (log2MaxPower > 0) {
			// we are 1 step to far...
			maxPower = maxPower >> 1;
			log2MaxPower--;
		}
		writeInt(random, 0x00010000);
		writeShort(random, numTables);
		writeShort(random, maxPower << 4);
		writeShort(random, log2MaxPower);
		writeShort(random, (numTables - maxPower) << 4);
	}

	public void writeInt(IRandomAccess random, int value) throws IOException {
		random.write((byte) (value >> 24));
		random.write((byte) (value >> 16));
		random.write((byte) (value >> 8));
		random.write((byte) (value));
	}

	public void writeShort(IRandomAccess random, int value) throws IOException {
		random.write((byte) (value >> 8));
		random.write((byte) (value));
	}

	protected void writeTableDirectory(IRandomAccess random) throws IOException {
		// todo recompute table directory information
		int tableCount = getFont().getTables().length;
		tableOffset = (tableCount * 16) + 12;
		for (int i = 0; i < tableCount; i++) {
			TTTable table = getFont().getTables()[i];
			writeTableDirectoryTable(random, table);
		}
	}

	protected void writeTableDirectoryTable(IRandomAccess random, TTTable table)
			throws IOException {
		int length = (int) table.getLength();
		writeBytes(random, table.getName(), 0, table.getName().length);
		writeInt(random, table.getChecksum());
		writeInt(random, tableOffset);
		writeInt(random, length);
		// table.setOffset(tableOffset);
		tableOffset = (tableOffset + length + 3) & (~3);
	}

	protected void writeTables(IRandomAccess random) throws IOException {
		int tableCount = getFont().getTables().length;

		/**
		 * java.util.Arrays.sort(getFont().getTables(), new Comparator() {
		 * public int compare(Object o1, Object o2) { return (int) (((TTTable)
		 * o1).getOffset() - ((TTTable) o2).getOffset()); }});
		 */
		for (int i = 0; i < tableCount; i++) {
			TTTable table = getFont().getTables()[i];
			writeTablesTable(random, table);
		}
	}

	protected void writeTablesTable(IRandomAccess random, TTTable table)
			throws IOException {
		IRandomAccess tableRandom = table.getRandomAccess();
		try {
			tableRandom.seek(0);
			int length = (int) table.getLength();
			for (int i = 0; i < length; i++) {
				writeByte(random, tableRandom.read());
			}
			// pad with zeros
			// todo make faster
			for (int i = length; i < ((length + 3) & (~3)); i++) {
				writeByte(random, (byte) 0);
			}
		} finally {
			StreamTools.close(tableRandom);
		}
	}
}
