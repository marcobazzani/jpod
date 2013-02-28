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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 
 */
public class TTNaming {
	private List nameRecords;

	public TTNaming() {
		nameRecords = new ArrayList(20);
	}

	public void add(int nameId, TTNameRecord record) {
		getNameRecords().add(record);
	}

	public TTNameRecord getNameRecord(int nameId) {
		for (Iterator it = nameRecords.iterator(); it.hasNext();) {
			TTNameRecord record = (TTNameRecord) it.next();
			if (record.getNameID() == nameId) {
				return record;
			}
		}
		return null;
	}

	public List getNameRecords() {
		return nameRecords;
	}

	public String getValue(int nameId) {
		TTNameRecord r = getNameRecord(nameId);
		if (r != null) {
			return r.getValue();
		}
		return null;
	}

	public void setNameRecords(List list) {
		nameRecords = list;
	}
}
