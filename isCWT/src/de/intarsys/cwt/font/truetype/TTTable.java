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
import de.intarsys.tools.randomaccess.RandomAccessByteArray;
import de.intarsys.tools.randomaccess.RandomAccessViewport;
import de.intarsys.tools.stream.StreamTools;

/**
 * 
 */
public class TTTable {
	private byte[] bytes;

	private int checksum = 0;

	private TTFont font;

	private long length;

	private byte[] name;

	private long offset;

	protected TTTable(TTFont font, long offset, long length) {
		super();
		this.font = font;
		this.offset = offset;
		this.length = length;
	}

	protected int createChecksum() throws IOException {
		IRandomAccess random = getRandomAccess();
		try {
			random.seek(0);
			long len = (random.getLength() + 3) / 4;
			int sum = 0;
			for (int i = 0; i < len; i++) {
				int v = random.read();
				v = (v << 8) + random.read();
				v = (v << 8) + random.read();
				v = (v << 8) + random.read();
				sum += v;
			}
			return sum;
		} finally {
			StreamTools.close(random);
		}
	}

	public int getChecksum() {
		if (checksum == 0) {
			try {
				checksum = createChecksum();
			} catch (IOException e) {
				//
			}
		}
		return checksum;
	}

	public TTFont getFont() {
		return font;
	}

	public long getLength() {
		return length;
	}

	public byte[] getName() {
		return name;
	}

	public long getOffset() {
		return offset;
	}

	public IRandomAccess getRandomAccess() throws IOException {
		if (bytes != null) {
			return new RandomAccessByteArray(bytes);
		}
		IRandomAccess random = getFont().getLocator().getRandomAccess();
		IRandomAccess viewport = new RandomAccessViewport(random, getOffset(),
				getLength());
		return viewport;
	}

	protected void setBytes(byte[] pBytes) {
		bytes = pBytes;
		checksum = 0;
		length = pBytes.length;
		offset = 0;
	}

	protected void setChecksum(int i) {
		checksum = i;
	}

	public void setName(byte[] string) {
		name = string;
	}

	@Override
	public String toString() {
		return new String(getName());
	}
}
