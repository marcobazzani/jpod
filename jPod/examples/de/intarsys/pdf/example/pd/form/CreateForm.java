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

import de.intarsys.pdf.cds.CDSRectangle;
import de.intarsys.pdf.example.common.CommonJPodExample;
import de.intarsys.pdf.font.PDFontType1;
import de.intarsys.pdf.pd.PDAFTextField;
import de.intarsys.pdf.pd.PDAcroForm;
import de.intarsys.pdf.pd.PDAppearanceCharacteristics;
import de.intarsys.pdf.pd.PDPage;
import de.intarsys.pdf.pd.PDWidgetAnnotation;

/**
 * Create a new document, an AcroForm and add a text field.
 */
public class CreateForm extends CommonJPodExample {

	public static void main(String[] args) {
		CreateForm client = new CreateForm();
		try {
			client.run(args);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void run(String[] args) throws Exception {
		if (args.length < 1) {
			usage();
			return;
		}
		try {
			String outputFileName = args[0];
			create();
			PDPage page = addPage();
			addFormField(page);
			save(outputFileName);
		} finally {
			close();
		}
	}

	public PDPage addPage() {
		// Then create the first page.
		PDPage page = (PDPage) PDPage.META.createNew();
		// add page to the document.
		getDoc().addPageNode(page);
		return page;
	}

	public void addFormField(PDPage page) {
		PDAcroForm form = getDoc().createAcroForm();
		// creating appearances is a hard task.
		// for now let the viewer do the work
		form.setNeedAppearances(true);

		// an annotation is the physical presentation of a field
		PDWidgetAnnotation annot = (PDWidgetAnnotation) PDWidgetAnnotation.META.createNew();
		// make it visible on our page
		page.addAnnotation(annot);
		// at this position.
		CDSRectangle rect = new CDSRectangle(50, 500, 500, 550);
		annot.setRectangle(rect);
		annot.setBorderStyleWidth(4);
		PDAppearanceCharacteristics apc = (PDAppearanceCharacteristics) PDAppearanceCharacteristics.META.createNew();
		apc.setBorderColor(new float[] { 1f, 0, 0 });
		apc.setBackgroundColor(new float[] { 0, 1, 0 });
		annot.setAppearanceCharacteristics(apc);

		// a text input field with this annotation.
		PDAFTextField field = (PDAFTextField) PDAFTextField.META.createNew();
		// add the field to the form immediately - maybe we propagate
		// information
		form.addField(field);
		// add the visible field
		field.addAnnotation(annot);
		field.setLocalName("field1"); //$NON-NLS-1$
		field.setDefaultAppearanceFont(PDFontType1.createNew(PDFontType1.FONT_Helvetica));
		field.setDefaultAppearanceFontSize(12f);
		field.setDefaultAppearanceFontColor(new float[] { 0, 0, 1 });
		field.setValueString("hello, world"); //$NON-NLS-1$
	}

	/**
	 * Help the user.
	 */
	public void usage() {
		System.out.println("usage: java.exe " + getClass().getName() //$NON-NLS-1$
				+ " <output-filename>"); //$NON-NLS-1$
	}
}
