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
package de.intarsys.cwt.font.afm;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.intarsys.tools.locator.ILocator;
import de.intarsys.tools.randomaccess.IRandomAccess;
import de.intarsys.tools.stream.StreamTools;

/**
 * A simple implementation for an adobe font metric object.
 * 
 * <p>
 * This implementation will hold all afm attributes in a generic collection. The
 * char metrics are read explicitly and held in a list of AFMChar's. No other
 * information (like kerning) is extracted.
 * </p>
 * 
 * <p>
 * See the "Adobe Font Metrics File Format Specification"
 * </p>
 */
public class AFM {

	public static final String ATTR_FontName = "FontName"; //$NON-NLS-1$

	public static AFM createFromLocator(ILocator locator) throws IOException {
		AFM result = new AFM();
		result.setLocator(locator);
		result.initializeFromLocator();
		return result;
	}

	public static AFM createNew() {
		return new AFM();
	}

	private ILocator locator;

	// the chars described in the font metric file
	// ... organized by codePoint
	private Map<Integer, AFMChar> charsByCode = new HashMap<Integer, AFMChar>();

	// ... organized by name
	private Map<String, AFMChar> charsByName = new HashMap<String, AFMChar>();

	// attribute name/value pairs parsed form the font metric file
	private Map<String, String> attributes = new HashMap<String, String>();

	protected AFM() {
		super();
	}

	protected void addChar(AFMChar c) {
		if (c.getCode() != -1) {
			charsByCode.put(new Integer(c.getCode()), c);
		}

		if (c.getName() != null) {
			charsByName.put(c.getName(), c);
		}
	}

	/**
	 * The string value for a generic attribute designated by <code>name</code>.
	 * 
	 * @param name
	 *            The name of the attribute to lookup.
	 * 
	 * @return The string value for a generic attribute designated by
	 *         <code>name</code>.
	 */
	public String getAttribute(String name) {
		return attributes.get(name);
	}

	/**
	 * The character representation for a byte code point given in
	 * <code>codePoint</code>.
	 * 
	 * @param codePoint
	 *            The byte code point.
	 * 
	 * @return The character representation for a byte code point given in
	 *         <code>codePoint</code>.
	 */
	public AFMChar getCharByCode(int codePoint) {
		return charsByCode.get(new Integer(codePoint));
	}

	/**
	 * The character representation for an adobe glyph name given in
	 * <code>name</code>.
	 * 
	 * @param name
	 *            The adobe glyph name.
	 * 
	 * @return The character representation for an adobe glyph name given in
	 *         <code>name</code>.
	 */
	public AFMChar getCharByName(String name) {
		return charsByName.get(name);
	}

	public String getFontName() {
		return getAttribute(ATTR_FontName);
	}

	public ILocator getLocator() {
		return locator;
	}

	protected void initializeFromLocator() throws IOException {
		IRandomAccess random = null;
		try {
			random = getLocator().getRandomAccess();
			AFMParser parser = new AFMParser(this);
			parser.parse(random);
		} finally {
			StreamTools.close(random);
		}
	}

	protected void setAttribute(String name, String value) {
		attributes.put(name, value);
	}

	protected void setLocator(ILocator locator) {
		this.locator = locator;
	}
}
