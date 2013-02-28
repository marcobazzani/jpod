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
public class TTNameRecord {
	private int platformID;

	private int encodingID;

	private int languageID;

	private int nameID;

	private int length;

	private String value;

	public TTNameRecord() {
	}

	public TTNameRecord(int platformID, int encodingID, int languageID,
			int nameID, String value) {
		super();
		setPlatformID(platformID);
		setEncodingID(encodingID);
		setLanguageID(languageID);
		setNameID(nameID);
		setValue(value);
	}

	public int getEncodingID() {
		return encodingID;
	}

	public int getLanguageID() {
		return languageID;
	}

	public int getLength() {
		return length;
	}

	public int getNameID() {
		return nameID;
	}

	public int getPlatformID() {
		return platformID;
	}

	public String getValue() {
		return value;
	}

	public void setEncodingID(int i) {
		encodingID = i;
	}

	public void setLanguageID(int i) {
		languageID = i;
	}

	public void setLength(int i) {
		length = i;
	}

	public void setNameID(int i) {
		nameID = i;
	}

	public void setPlatformID(int i) {
		platformID = i;
	}

	public void setValue(String value) {
		/*
		 * byte[] bytes = null; try { bytes = value.getBytes("UTF-16BE"); }
		 * catch (Exception ignore) { } setData(bytes);
		 */
		this.value = value;
	}

	@Override
	public String toString() {
		return "ID:" + getNameID() + " Value:" + getValue(); //$NON-NLS-1$ //$NON-NLS-2$
	}
}
