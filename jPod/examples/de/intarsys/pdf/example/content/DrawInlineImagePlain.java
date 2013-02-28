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
import de.intarsys.pdf.cos.COSString;
import de.intarsys.pdf.example.common.CommonJPodExample;
import de.intarsys.pdf.pd.PDPage;

/**
 * Create a document and draw a simple plain raster inline image.
 */
public class DrawInlineImagePlain extends CommonJPodExample {

	public static void main(String[] args) {
		DrawInlineImagePlain client = new DrawInlineImagePlain();
		try {
			client.run(args);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private byte[] data = new byte[] { (byte) 170, (byte) 170, (byte) 170,
			(byte) 170, (byte) 170, (byte) 170, (byte) 170, (byte) 170,
			(byte) 170, (byte) 170, (byte) 170, (byte) 170, (byte) 170,
			(byte) 170, (byte) 170, (byte) 170, (byte) 170, (byte) 170,
			(byte) 170, (byte) 170, (byte) 170, (byte) 170, (byte) 170,
			(byte) 170, (byte) 170, (byte) 170, (byte) 170, (byte) 170,
			(byte) 170, (byte) 170, (byte) 170, (byte) 170 };

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

		creator.transform(400, 0, 0, 400, 100, 100);
		creator.perform("BI");
		creator.perform("/W 16 /H 16 /BPC 1 /CS /G ID");
		COSString stringData = COSString.create(data);
		creator.perform("EI", stringData);

		// don't forget to flush the content.
		creator.close();
	}

	public void run(String[] args) throws Exception {

		try {
			String outputFileName = "sdfk.pdf";
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
