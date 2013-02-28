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
import java.util.Arrays;

import de.intarsys.tools.collection.ByteArrayTools;
import de.intarsys.tools.randomaccess.IRandomAccess;
import de.intarsys.tools.stream.FastByteArrayOutputStream;

/**
 * A simple parser for AFM type definition files.
 * 
 * <p>
 * See the "Adobe Font Metrics File Format Specification"
 * </p>
 */
public class AFMParser {
	protected static final byte[] characterClass = new byte[256];

	protected static final byte CHARCLASS_ANY = 0;

	protected static final byte CHARCLASS_DELIMITER = 1;

	protected static final byte CHARCLASS_DIGIT = 4;

	protected static final byte CHARCLASS_NUMBERSPECIAL = 5;

	protected static final byte CHARCLASS_TOKEN = 3;

	protected static final byte CHARCLASS_WHITESPACE = 2;

	private static final byte[] TOKEN_Comment = "Comment".getBytes(); //$NON-NLS-1$

	private static final byte[] TOKEN_EndCharMetrics = "EndCharMetrics".getBytes(); //$NON-NLS-1$

	private static final byte[] TOKEN_EndFlag = "End".getBytes(); //$NON-NLS-1$

	private static final byte[] TOKEN_EndFontMetrics = "EndFontMetrics".getBytes(); //$NON-NLS-1$

	private static final byte[] TOKEN_StartCharMetrics = "StartCharMetrics".getBytes(); //$NON-NLS-1$

	private static final byte[] TOKEN_StartFlag = "Start".getBytes(); //$NON-NLS-1$

	private static final byte[] TOKEN_StartFontMetrics = "StartFontMetrics".getBytes(); //$NON-NLS-1$

	public static char CHAR_BS = '\b';

	public static char CHAR_CR = '\r';

	public static char CHAR_FF = '\f';

	public static char CHAR_HT = '\t';

	public static char CHAR_LF = '\n';

	static {
		for (int i = 0; i < 256; i++) {
			characterClass[i] = CHARCLASS_ANY;
		}
		// delimiters
		characterClass['('] = CHARCLASS_DELIMITER;
		characterClass[')'] = CHARCLASS_DELIMITER;
		characterClass['<'] = CHARCLASS_DELIMITER;
		characterClass['>'] = CHARCLASS_DELIMITER;
		characterClass['['] = CHARCLASS_DELIMITER;
		characterClass[']'] = CHARCLASS_DELIMITER;
		characterClass['{'] = CHARCLASS_DELIMITER;
		characterClass['}'] = CHARCLASS_DELIMITER;
		characterClass['/'] = CHARCLASS_DELIMITER;
		characterClass['%'] = CHARCLASS_DELIMITER;

		// whitespace
		characterClass[' '] = CHARCLASS_WHITESPACE;
		characterClass['\t'] = CHARCLASS_WHITESPACE;
		characterClass['\r'] = CHARCLASS_WHITESPACE;
		characterClass['\n'] = CHARCLASS_WHITESPACE;
		characterClass[12] = CHARCLASS_WHITESPACE;
		characterClass[0] = CHARCLASS_WHITESPACE;

		// digits
		characterClass['0'] = CHARCLASS_DIGIT;
		characterClass['1'] = CHARCLASS_DIGIT;
		characterClass['2'] = CHARCLASS_DIGIT;
		characterClass['3'] = CHARCLASS_DIGIT;
		characterClass['4'] = CHARCLASS_DIGIT;
		characterClass['5'] = CHARCLASS_DIGIT;
		characterClass['6'] = CHARCLASS_DIGIT;
		characterClass['7'] = CHARCLASS_DIGIT;
		characterClass['8'] = CHARCLASS_DIGIT;
		characterClass['9'] = CHARCLASS_DIGIT;

		// number special
		characterClass['.'] = CHARCLASS_NUMBERSPECIAL;
		characterClass['-'] = CHARCLASS_NUMBERSPECIAL;
		characterClass['+'] = CHARCLASS_NUMBERSPECIAL;

		// alpha
		for (int i = 'a'; i <= 'z'; i++) {
			characterClass[i] = CHARCLASS_TOKEN;
		}
		for (int i = 'A'; i <= 'Z'; i++) {
			characterClass[i] = CHARCLASS_TOKEN;
		}
	}

	/**
	 * evaluate to true if i is a valid line terminator.
	 * 
	 * @param i
	 *            i a byte representation
	 * 
	 * @return true if i is a valid line terminator
	 */
	public static final boolean isEOL(int i) {
		return (i == CHAR_CR) || (i == CHAR_LF) || (i == 12);
	}

	/**
	 * evaluate to true if i is a valid whitespace.
	 * 
	 * @param i
	 *            i a byte representation
	 * 
	 * @return true if i is a valid whitespace
	 */
	public static final boolean isWhitespace(int i) {
		return characterClass[i] == CHARCLASS_WHITESPACE;
	}

	private AFM afm;

	private FastByteArrayOutputStream localStream = new FastByteArrayOutputStream();

	/**
	 * AFMParser constructor comment.
	 */
	public AFMParser(AFM afm) {
		super();
		this.afm = afm;
	}

	/**
	 * Parse a {@link AFM} object from the input stream <code>is</code>.
	 * 
	 * @param is
	 *            The input stream containing the definition.
	 * 
	 * @return The {@link AFM } parsed.
	 * 
	 * @throws IOException
	 */
	public AFM parse(IRandomAccess random) throws IOException {
		read(random);
		return afm;
	}

	protected void read(IRandomAccess random) throws IOException {
		byte[] token = readToken(random);
		if (!Arrays.equals(token, TOKEN_StartFontMetrics)) {
			throw new IOException("afm stream does not start with " //$NON-NLS-1$
					+ new String(TOKEN_StartFontMetrics));
		}
		readToken(random);
		token = readFontMetrics(random);
		if (token == null || !Arrays.equals(token, TOKEN_EndFontMetrics)) {
			throw new IOException("afm stream does not end with " //$NON-NLS-1$
					+ new String(TOKEN_EndFontMetrics));
		}
	}

	/**
	 * ignore this and any nested Start/End pair
	 */
	protected byte[] readBlock(IRandomAccess random) throws IOException {
		byte[] line = readLine(random);
		while (!ByteArrayTools.startsWith(line, TOKEN_EndFlag)) {
			if (ByteArrayTools.startsWith(line, TOKEN_Comment)) {
				// ignore
			} else {
				if (ByteArrayTools.startsWith(line, TOKEN_StartFlag)) {
					line = readBlock(random);
				}
			}
			line = readLine(random);
		}
		return line;
	}

	protected byte[] readCharMetrics(IRandomAccess random) throws IOException {
		// read count
		readToken(random);
		byte[] line = readLine(random);
		while (!ByteArrayTools.startsWith(line, TOKEN_EndCharMetrics)) {
			afm.addChar(AFMChar.create(new String(line)));
			line = readLine(random);
		}
		return line;
	}

	protected byte[] readFontMetrics(IRandomAccess random) throws IOException {
		byte[] token = readToken(random);
		while ((token != null) && !Arrays.equals(token, TOKEN_EndFontMetrics)) {
			if (Arrays.equals(token, TOKEN_Comment)) {
				// ignore line?
			} else {
				if (Arrays.equals(token, TOKEN_StartCharMetrics)) {
					token = readCharMetrics(random);
				} else {
					if (ByteArrayTools.startsWith(token, TOKEN_StartFlag)) {
						token = readBlock(random);
					} else {
						String value = new String(readLine(random));
						afm.setAttribute(new String(token), value);
					}
				}
			}
			token = readToken(random);
		}
		return token;
	}

	/**
	 * read a single line.
	 * 
	 * @return the array of characters belonging to the line
	 * 
	 * @throws IOException
	 */
	public byte[] readLine(IRandomAccess input) throws IOException {
		int next;
		while (true) {
			next = input.read();
			if (next == -1) {
				return null;
			} else if ((next == ' ') || isWhitespace(next)) {
				continue;
			} else {
				break;
			}
		}
		return readLineElement(input, next);
	}

	protected byte[] readLineElement(IRandomAccess input, int next)
			throws IOException {
		localStream.reset();
		localStream.write(next);
		do {
			next = input.read();
			if (next == -1) {
				break;
			} else if (isEOL(next)) { // performance
				// shortcut
				break;
			}
			localStream.write(next);
		} while (true);
		return localStream.toByteArray();
	}

	/**
	 * read all characters until EOF or non space char appears. the first non
	 * space char is pushed back so the next char read is the first non space
	 * char.
	 * 
	 * @throws IOException
	 */
	public void readSpaces(IRandomAccess input) throws IOException {
		int next = 0;
		while (true) {
			next = input.read();
			if (next == -1) {
				break;
			}
			// performance shortcut for simple space
			if ((next == ' ') || isWhitespace(next)) {
				continue;
			}
			input.seekBy(-1);
			break;
		}
	}

	/**
	 * read a single token.
	 * 
	 * @return the array of characters belonging to the token
	 * 
	 * @throws IOException
	 */
	public byte[] readToken(IRandomAccess input) throws IOException {
		int next;
		while (true) {
			next = input.read();
			if (next == -1) {
				return null;
			} else if ((next == ' ') || isWhitespace(next)) {
				continue;
			} else {
				break;
			}
		}
		return readTokenElement(input, next);
	}

	protected byte[] readTokenElement(IRandomAccess input, int next)
			throws IOException {
		localStream.reset();
		localStream.write(next);
		do {
			next = input.read();
			if (next == -1) {
				break;
			} else if ((next == ' ') || isWhitespace(next)) { // performance
				// shortcut
				break;
			}
			localStream.write(next);
		} while (true);
		return localStream.toByteArray();
	}
}
