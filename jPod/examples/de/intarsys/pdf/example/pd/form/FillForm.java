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
package de.intarsys.pdf.example.pd.form;

import de.intarsys.pdf.app.acroform.IFormHandler;
import de.intarsys.pdf.example.common.CommonJPodExample;
import de.intarsys.pdf.pd.PDAcroForm;
import de.intarsys.pdf.pd.PDAcroFormField;

/**
 * Open an document and fill a field.
 * <p>
 * This is the most basic way to do this. Remember that there is a lot around to
 * do if you want to do "right".
 * 
 * <ul>
 * <li>Format values (using patterns or JavaScript)</li>
 * <li>Calculate other fields (in JavaScript)</li>
 * <li>Create appearances</li>
 * </ul>
 * 
 * There is another class {@link IFormHandler} that will do a lot of work for
 * you, handling form details transparently.
 */
public class FillForm extends CommonJPodExample {

	public static void main(String[] args) {
		FillForm client = new FillForm();
		try {
			client.run(args);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void run(String[] args) throws Exception {
		if (args.length < 4) {
			usage();
			return;
		}
		try {
			String inputFileName = args[0];
			String outputFileName = args[1];
			String fieldName = args[2];
			String fieldValue = args[3];
			open(inputFileName);
			fillFormField(fieldName, fieldValue);
			save(outputFileName);
		} finally {
			close();
		}
	}

	public void fillFormField(String fieldName, String fieldValue) {
		PDAcroForm form = getDoc().getAcroForm();
		if (form == null) {
			return;
		}
		PDAcroFormField field = form.getField(fieldName);
		if (field == null) {
			return;
		}
		field.setValueString(fieldValue);
	}

	/**
	 * Help the user.
	 */
	public void usage() {
		System.out.println("usage: java.exe " + getClass().getName() //$NON-NLS-1$
				+ " <input-filename> <output-filename> <field name> <field value>"); //$NON-NLS-1$
	}
}
