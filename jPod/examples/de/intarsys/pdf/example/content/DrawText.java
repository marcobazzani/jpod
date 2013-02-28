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
package de.intarsys.pdf.example.content;

import de.intarsys.pdf.content.common.CSCreator;
import de.intarsys.pdf.encoding.WinAnsiEncoding;
import de.intarsys.pdf.example.common.CommonJPodExample;
import de.intarsys.pdf.font.PDFont;
import de.intarsys.pdf.font.PDFontType1;
import de.intarsys.pdf.pd.PDPage;

/**
 * Create a new document and its content using one of the builtin fonts.
 */
public class DrawText extends CommonJPodExample {

	public static void main(String[] args) {
		DrawText client = new DrawText();
		try {
			client.run(args);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public PDPage addPage() {
		// Then create the first page.
		PDPage page = (PDPage) PDPage.META.createNew();
		// add page to the document.
		getDoc().addPageNode(page);
		return page;
	}

	public void draw(PDPage page) {
		// open a device to the page content stream
		CSCreator creator = CSCreator.createNew(page);

		PDFont font = PDFontType1.createNew(PDFontType1.FONT_Helvetica);
		font.setEncoding(WinAnsiEncoding.UNIQUE);
		float fontSize = 20;
		creator.textSetFont(null, font, fontSize);
		creator.textLineMoveTo(100, 700);
		creator.textShow("Hello, äüößÄÖÜ"); //$NON-NLS-1$

		// Don't forget to flush the content.
		creator.close();
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
			draw(page);
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
