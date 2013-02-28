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
package de.intarsys.pdf.example.annotation.link;

import de.intarsys.pdf.cds.CDSRectangle;
import de.intarsys.pdf.cos.COSName;
import de.intarsys.pdf.example.common.CommonJPodExample;
import de.intarsys.pdf.pd.PDAction;
import de.intarsys.pdf.pd.PDActionJavaScript;
import de.intarsys.pdf.pd.PDBorderStyle;
import de.intarsys.pdf.pd.PDLinkAnnotation;
import de.intarsys.pdf.pd.PDPage;

/**
 * Create a new document and a /Link Annotation.
 */
public class CreateLink extends CommonJPodExample {

	public static void main(String[] args) {
		CreateLink client = new CreateLink();
		try {
			client.run(args);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void addLink(PDPage page) {

		// create and add a link
		PDLinkAnnotation annot = (PDLinkAnnotation) PDLinkAnnotation.META
				.createNew();
		page.addAnnotation(annot);

		// at this position.
		CDSRectangle rect = new CDSRectangle(50, 500, 500, 550);
		annot.setRectangle(rect);

		// with this look & feel
		// attention: with some acrobat versions the look & feel,
		// even conform to the spec, may not be honored!
		PDBorderStyle bs = (PDBorderStyle) PDBorderStyle.META.createNew();
		bs.setWidth(4);
		// bug with Acrobat 7.0, must explicitly state default
		bs.setStyle(COSName.create("S"));
		annot.setBorderStyle(bs);
		annot.setColor(new float[] { 0.5f, 0.5f, 0.5f });
		annot.setHighlightingMode(COSName.create("O"));

		// and this action
		PDAction action = PDActionJavaScript.createNew("app.alert('hello')");
		annot.setAction(action);
	}

	public PDPage addPage() {
		// Then create the first page.
		PDPage page = (PDPage) PDPage.META.createNew();
		// add page to the document.
		getDoc().addPageNode(page);
		return page;
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
			addLink(page);
			save(outputFileName);
		} finally {
			close();
		}
	}

	/**
	 * Help the user.
	 */
	public void usage() {
		System.out.println("usage: java.exe " + getClass().getName() //$NON-NLS-1$
				+ " <output-filename>"); //$NON-NLS-1$
	}
}
