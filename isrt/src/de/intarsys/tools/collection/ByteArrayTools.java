/*
 * Copyright (c) 2007, intarsys consulting GmbH
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
package de.intarsys.tools.collection;

import java.util.Arrays;
import java.util.Random;

public class ByteArrayTools {

	public static byte[] concat(byte[] array1, byte[] array2) {
		byte[] concat = new byte[array1.length + array2.length];
		System.arraycopy(array1, 0, concat, 0, array1.length);
		System.arraycopy(array2, 0, concat, array1.length, array2.length);
		return concat;
	}

	/**
	 * Make a copy of <code>bytes</code>.
	 * 
	 * @param bytes
	 *            byte[] to be copied
	 * 
	 * @return A copy of <code>bytes</code>
	 */
	static public byte[] copy(byte[] bytes) {
		byte[] newbuf = new byte[bytes.length];
		System.arraycopy(bytes, 0, newbuf, 0, bytes.length);
		return newbuf;
	}

	/**
	 * Copy <code>length</code> bytes from <code>bytes</code> starting at
	 * <code>from</code>.
	 * 
	 * @param bytes
	 *            byte[] to be copied
	 * @param offset
	 *            starting position to copy from
	 * @param length
	 *            number of bytes
	 * 
	 * @return A copy of <code>bytes</code>
	 */
	static public byte[] copy(byte[] bytes, int offset, int length) {
		byte[] newbuf = new byte[length];
		System.arraycopy(bytes, offset, newbuf, 0, length);
		return newbuf;
	}

	/**
	 * A byte array with the defined length, filled with val.
	 * 
	 * @param length
	 * @param val
	 * @return A byte array with the defined length, filled with val.
	 */
	public static byte[] createBytes(int length, byte val) {
		byte[] bytes = new byte[length];
		Arrays.fill(bytes, val);
		return bytes;
	}

	/**
	 * A random byte sequence of length <code>length</code>.
	 * 
	 * @param length
	 * @return A random byte sequence of length <code>length</code>.
	 */
	static public byte[] createRandomBytes(int length) {
		Random rand = new Random(System.currentTimeMillis());
		byte[] bytes = new byte[length];
		rand.nextBytes(bytes);
		return bytes;
	}

	/**
	 * Checks two arrays or sections thereof for equality. If an array is
	 * <code>null</code> or it's section is shorter than the compared length,
	 * <code>false</code> is returned.
	 * 
	 * @param array1
	 * @param offset1
	 * @param array2
	 * @param offset2
	 * @param length
	 *            must have a value greater than 0. A value of 0 always returns
	 *            <code>false</code>.
	 * @return <code>true</code>, if the compared array sections match
	 */
	public static boolean equals(byte[] array1, int offset1, byte[] array2,
			int offset2, int length) {
		if (array1 == null || array2 == null) {
			return false;
		}
		if (array1.length - offset1 < length
				|| array2.length - offset2 < length) {
			return false;
		}
		if (length == 0) {
			return false;
		}

		for (int i = 0; i < length; i++) {
			if (array1[offset1 + i] != array2[offset2 + i]) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Search index of pattern in source. Algorithm from java.lang.String
	 * 
	 * @param source
	 * @param sourceOffset
	 * @param sourceLen
	 * @param pattern
	 * @param patternOffset
	 * @param patternLen
	 * @param fromIndex
	 * @return The index of the first occurrence of pattern or -1.
	 */
	static public int indexOf(byte[] source, int sourceOffset, int sourceLen,
			byte[] pattern, int patternOffset, int patternLen, int fromIndex) {
		if (fromIndex >= sourceLen) {
			return (patternLen == 0 ? sourceLen : -1);
		}
		if (fromIndex < 0) {
			fromIndex = 0;
		}
		if (patternLen == 0) {
			return fromIndex;
		}

		byte first = pattern[patternOffset];
		int max = sourceOffset + (sourceLen - patternLen);

		for (int i = sourceOffset + fromIndex; i <= max; i++) {
			/* Look for first byte . */
			if (source[i] != first) {
				while (++i <= max && source[i] != first) {
					;
				}
			}

			/* Found first byte, now look at the rest of v2 */
			if (i <= max) {
				int j = i + 1;
				int end = j + patternLen - 1;
				for (int k = patternOffset + 1; j < end
						&& source[j] == pattern[k]; j++, k++) {
					;
				}

				if (j == end) {
					/* Found whole pattern. */
					return i - sourceOffset;
				}
			}
		}
		return -1;
	}

	/**
	 * <code>true</code> if <code>bytes</code> starts with the byte sequence
	 * defined in <code>pattern</code>.
	 * 
	 * @param bytes
	 * @param pattern
	 * @return <code>true</code> if <code>bytes</code> starts with the byte
	 *         sequence defined in <code>pattern</code>.
	 * 
	 */
	static public boolean startsWith(byte[] bytes, byte[] pattern) {
		if (bytes == null) {
			return false;
		}
		if (pattern == null) {
			return true;
		}
		int length = pattern.length;
		for (int i = 0; i < length; i++) {
			if (bytes[i] != pattern[i]) {
				return false;
			}
		}
		return true;
	}

	public static void toBigEndianArray(byte[] bytes, int offset, int value,
			int size) {
		int bitShift = 0;
		for (int i = size - 1; i >= 0; i--) {
			bytes[i + offset] = (byte) ((value >>> bitShift) & 0xFF);
			bitShift += 8;
		}
	}

	public static byte[] toBigEndianArray(int value, int size) {
		byte[] result = new byte[size];
		int bitShift = 0;
		for (int i = size - 1; i >= 0; i--) {
			result[i] = (byte) ((value >>> bitShift) & 0xFF);
			bitShift += 8;
		}
		return result;
	}

	public static int toBigEndianInteger(byte[] buffer, int offset, int size) {
		int value = 0;
		for (int i = 0; i < size; i++) {
			int bitShift = 8 * (size - i - 1);
			value |= (buffer[offset + i] & 0xFF) << bitShift;
		}
		return value;
	}

	public static void toLittleEndianArray(byte[] bytes, int offset, int value,
			int size) {
		for (int i = 0; i < size; i++) {
			int bitShift = 8 * i;
			bytes[i + offset] = (byte) ((value >>> bitShift) & 0xFF);
		}
	}

	public static void toLittleEndianArray(byte[] bytes, int offset,
			long value, int size) {
		for (int i = 0; i < size; i++) {
			int bitShift = 8 * i;
			bytes[i + offset] = (byte) ((value >>> bitShift) & 0xFF);
		}
	}

	public static byte[] toLittleEndianArray(int value, int size) {
		byte[] result = new byte[size];
		for (int i = 0; i < size; i++) {
			int bitShift = 8 * i;
			result[i] = (byte) ((value >>> bitShift) & 0xFF);
		}
		return result;
	}

	public static byte[] toLittleEndianArray(long value, int size) {
		byte[] result = new byte[size];
		for (int i = 0; i < size; i++) {
			int bitShift = 8 * i;
			result[i] = (byte) ((value >>> bitShift) & 0xFF);
		}
		return result;
	}

	public static int toLittleEndianInteger(byte[] buffer, int offset, int size) {
		int value = 0;
		for (int i = 0; i < size; i++) {
			int bitShift = 8 * i;
			value |= (buffer[offset + i] & 0xFF) << bitShift;
		}
		return value;
	}

	public static long toLittleEndianLong(byte[] buffer, int offset, int size) {
		long value = 0;
		for (int i = 0; i < size; i++) {
			int bitShift = 8 * i;
			value |= (buffer[offset + i] & 0xFF) << bitShift;
		}
		return value;
	}
}
