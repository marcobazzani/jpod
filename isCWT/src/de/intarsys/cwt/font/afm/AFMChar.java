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

import java.util.StringTokenizer;

import de.intarsys.tools.string.StringTools;

/**
 * A simple implementation for an AFM char metric object
 * 
 * <p>
 * This implementation will hold the information needed for implementing a
 * simple pdf field layout mechanism. All attributes but name, code, and with
 * are ignored.
 * </p>
 * 
 * <p>
 * See the "Adobe Font Metrics File Format Specification"
 * </p>
 */
public class AFMChar {
	/*
	 * todo under construction
	 */

	//
	public static String AFM_TOKEN_WX = "WX"; //$NON-NLS-1$

	public static String AFM_TOKEN_W = "W"; //$NON-NLS-1$

	public static String AFM_TOKEN_W0X = "W0X"; //$NON-NLS-1$

	public static String AFM_TOKEN_W0 = "W0"; //$NON-NLS-1$

	public static String AFM_TOKEN_N = "N"; //$NON-NLS-1$

	public static String AFM_TOKEN_CH = "CH"; //$NON-NLS-1$

	public static String AFM_TOKEN_C = "C"; //$NON-NLS-1$

	/**
	 * Create a new glyph representation from the definition parsed from
	 * <code>glyph</code>.
	 * 
	 * @param glyph
	 *            A string containing a definition for the glyph.
	 * 
	 * @return The new glyph representation.
	 */
	public static AFMChar create(String glyph) {
		int code = -1;
		int width = -1;
		String name = StringTools.EMPTY;
		StringTokenizer to = new StringTokenizer(glyph, ";"); //$NON-NLS-1$
		while (to.hasMoreTokens()) {
			StringTokenizer tf = new StringTokenizer(to.nextToken());
			if (!tf.hasMoreTokens()) {
				continue;
			}

			String field = tf.nextToken();
			if (field.equals(AFM_TOKEN_C)) {
				code = Integer.parseInt(tf.nextToken());
			} else if (field.equals(AFM_TOKEN_CH)) {
				String value = tf.nextToken();
				code = Integer.parseInt(value.substring(1, value.length() - 1),
						16);
			} else if (field.equals(AFM_TOKEN_WX)) {
				width = Integer.parseInt(tf.nextToken());
			} else if (field.equals(AFM_TOKEN_W0X)) {
				width = Integer.parseInt(tf.nextToken());
			} else if (field.equals(AFM_TOKEN_W)) {
				width = Integer.parseInt(tf.nextToken());
			} else if (field.equals(AFM_TOKEN_W0)) {
				width = Integer.parseInt(tf.nextToken());
			} else if (field.equals(AFM_TOKEN_N)) {
				name = tf.nextToken();
			}
		}
		return new AFMChar(code, width, name);
	}

	// the char name
	private String name;

	// the char code or -1
	private int code;

	// the char width or 0
	private int width;

	/**
	 * AFMChar constructor comment.
	 * 
	 * @param code
	 *            docme
	 * @param width
	 *            docme
	 * @param name
	 *            docme
	 */
	protected AFMChar(int code, int width, String name) {
		super();
		setCode(code);
		setWidth(width);
		setName(name);
	}

	/**
	 * The glyphs code.
	 * 
	 * @return The glyphs code.
	 */
	public int getCode() {
		return code;
	}

	/**
	 * The glyphs name.
	 * 
	 * @return The glyphs name.
	 */
	public java.lang.String getName() {
		return name;
	}

	/**
	 * The glyphs width.
	 * 
	 * @return The glyphs width.
	 */
	public int getWidth() {
		return width;
	}

	private void setCode(int newCode) {
		code = newCode;
	}

	private void setName(java.lang.String newName) {
		name = newName;
	}

	private void setWidth(int newWidth) {
		width = newWidth;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "<" + getCode() + ", " + getWidth() + ", " + getName() + ">"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
	}
}
