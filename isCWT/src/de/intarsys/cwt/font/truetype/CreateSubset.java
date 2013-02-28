package de.intarsys.cwt.font.truetype;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import de.intarsys.tools.randomaccess.IRandomAccess;
import de.intarsys.tools.randomaccess.RandomAccessByteArray;
import de.intarsys.tools.stream.StreamTools;

public class CreateSubset {

	public static byte[][] SubsetTables;

	static {
		// !!keep sorted!!
		SubsetTables = new byte[][] { //
		//
				TTFont.TABLE_CMAP, //
				TTFont.TABLE_CVT, //
				TTFont.TABLE_FGPM, //
				TTFont.TABLE_GLYF, //
				TTFont.TABLE_HEAD, //
				TTFont.TABLE_HHEA, //
				TTFont.TABLE_HMTX, //
				TTFont.TABLE_LOCA, //
				TTFont.TABLE_MAXP, //
				TTFont.TABLE_NAME, //
				TTFont.TABLE_OS2, //
				TTFont.TABLE_PREP //
		};
	}

	final private TTFont font;

	final private Set glyphIndices;

	private TTFont result;

	public CreateSubset(TTFont font, Set glyphIndices) {
		super();
		this.font = font;
		this.glyphIndices = glyphIndices;
	}

	/**
	 * Add all dependent glyphs not yet included for any composite glyphs in the
	 * set of glyphs.
	 * 
	 * @param glyfRandom
	 * @param locations
	 * @param glyphs
	 * @return
	 * @throws IOException
	 * @throws TrueTypeException
	 */
	protected Set addCompositeGlyphs(IRandomAccess glyfRandom, int[] locations,
			Set glyphs) throws IOException, TrueTypeException {
		Set allGlyphs = new HashSet();
		allGlyphs.addAll(glyphs);
		for (Iterator i = glyphs.iterator(); i.hasNext();) {
			int codePoint = ((Integer) i.next()).intValue();
			addCompositeGlyphs(glyfRandom, locations, allGlyphs, codePoint);
		}
		return allGlyphs;
	}

	/**
	 * Add dependent glyphs not yet included for composite glyphs.
	 * 
	 * @param glyfRandom
	 * @param locations
	 * @param glyphs
	 * @param codePoint
	 * @throws IOException
	 * @throws TrueTypeException
	 */
	protected void addCompositeGlyphs(IRandomAccess glyfRandom,
			int[] locations, Set glyphs, int codePoint) throws IOException,
			TrueTypeException {
		if (locations[codePoint] == locations[codePoint + 1]) {
			return;
		}
		glyfRandom.seek(locations[codePoint]);
		TTFontParser parser = new TTFontParser();
		int numContours = parser.readShort(glyfRandom);
		if (numContours >= 0) {
			return;
		}
		glyfRandom.seekBy(8);

		for (;;) {
			int flags = parser.readUShort(glyfRandom);
			int codePointRef = parser.readUShort(glyfRandom);
			glyphs.add(new Integer(codePointRef));

			if ((flags & TTFont.MORE_COMPONENTS) == 0) {
				return;
			}

			int skip;

			if ((flags & TTFont.ARG_1_AND_2_ARE_WORDS) != 0) {
				skip = 4;
			} else {
				skip = 2;
			}

			if ((flags & TTFont.WE_HAVE_A_SCALE) != 0) {
				skip += 2;
			} else if ((flags & TTFont.WE_HAVE_AN_X_AND_Y_SCALE) != 0) {
				skip += 4;
			}

			if ((flags & TTFont.WE_HAVE_A_TWO_BY_TWO) != 0) {
				skip += 8;
			}
			glyfRandom.seekBy(skip);
		}
	}

	public TTFont compute() throws IOException, TrueTypeException {
		TTTable loca = getFont().getTable(TTFont.TABLE_LOCA);
		int[] locations = new TTFontParser().parseTable_loca(loca, getFont()
				.getFontHeader().isShortLocationFormat());
		TTTable glyf = getFont().getTable(TTFont.TABLE_GLYF);
		IRandomAccess glyfRandom = glyf.getRandomAccess();
		try {
			result = copySubset();
			// always include glyph 0
			getGlyphIndices().add(new Integer(0));
			Set compositeGlyphs = addCompositeGlyphs(glyfRandom, locations,
					getGlyphIndices());
			createGlyphTable(loca, glyf, glyfRandom, locations,
					compositeGlyphs, getFont().getFontHeader()
							.isShortLocationFormat());
			return result;
		} finally {
			StreamTools.close(glyfRandom);
		}
	}

	protected TTFont copySubset() {
		TTFont resultFont = new TTFont();
		List newTables = new ArrayList();

		for (int i = 0; i < SubsetTables.length; i++) {
			TTTable table = getFont().getTable(SubsetTables[i]);

			if (table != null) {
				newTables.add(table);
			}
		}

		resultFont.setTables((TTTable[]) newTables.toArray(new TTTable[0]));

		return resultFont;
	}

	protected void createGlyphTable(TTTable loca, TTTable glyf,
			IRandomAccess glyfRandom, int[] oldLocations, Set glyphs,
			boolean shortFormat) throws IOException, TrueTypeException {
		// compute the total length for the new glyf table
		int newLength = 0;
		for (Iterator i = glyphs.iterator(); i.hasNext();) {
			int codePoint = ((Integer) i.next()).intValue();
			if ((codePoint + 1) >= oldLocations.length) {
				continue;
			}
			newLength += (oldLocations[codePoint + 1] - oldLocations[codePoint]);
		}
		newLength = TTFont.align(newLength);

		// rebuild new glyf table
		int[] newLocations = new int[oldLocations.length];
		byte[] newGlyfData = new byte[newLength];
		int ptr = 0;
		for (int i = 0; i < oldLocations.length; i++) {
			newLocations[i] = ptr;
			if (glyphs.contains(new Integer(i))) {
				int glyfstart = oldLocations[i];
				int glyflength = oldLocations[i + 1] - glyfstart;

				if (glyflength > 0) {
					glyfRandom.seek(glyfstart);
					glyfRandom.read(newGlyfData, ptr, glyflength);
					ptr += glyflength;
				}
			}
		}

		// store new loca table
		RandomAccessByteArray random = new RandomAccessByteArray(null);
		TTFontSerializer serializer = new TTFontSerializer();
		serializer.write_loca(random, newLocations, shortFormat);
		loca.setBytes(random.toByteArray());
		// store new glyf table
		glyf.setBytes(newGlyfData);
	}

	public TTFont getFont() {
		return font;
	}

	public Set getGlyphIndices() {
		return glyphIndices;
	}

	public TTFont getResult() {
		return result;
	}
}
