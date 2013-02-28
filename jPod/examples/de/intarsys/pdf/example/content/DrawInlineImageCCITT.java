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
 * Create a document and draw fax encoded inline images.
 */
public class DrawInlineImageCCITT extends CommonJPodExample {

	public static void main(String[] args) {
		DrawInlineImageCCITT client = new DrawInlineImageCCITT();
		try {
			client.run(args);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private byte[] data1 = new byte[] { -109, 84, 71, 68, 116, 71, 68, 116, 71,
			68, 116, 18, -120, -76, -45, 82, 106, -127, 2, 72, 16, 36, -127, 2,
			72, 37, 17, 17, 17, 17, -109, 84, 71, 68, 116, 71, 68, 116, 71, 68,
			116, 18, -76, -45, 77, 82, 4, 9, 32, 64, -110, 4, 9, 32, -108, 68,
			68, 68, 70, 77, 81, 29, 17, -47, 29, 17, -47, 29, 17, -48, 74, -45,
			66, 45, 82, 4, 9, 34, 58, 35, -96, 64, -110, 9, 68, 68, 68, 68,
			100, -43, 17, -47, 29, 17, -47, 29, 17, -47, 29, 4, -83, 52, -45,
			84, -127, 2, 72, 16, 36, -127, 2, 72, 37, 0, 16, 1 };

	private byte[] data2 = new byte[] { 38, -88, -114, -120, -24, -114, -120,
			-24, -114, -120, -24, 37, -120, -120, -120, -117, -108, 57, 67,
			-108, 57, 67, -108, 57, 67, -126, 9, 98, 34, 34, 34, -27, 14, 80,
			-27, 14, 80, -27, 14, 80, -32, -126, 88, -120, -120, -120, -71, 67,
			-108, 57, 67, -108, 57, 67, -108, 56, 32, -106, 34, 34, 34, 46, 80,
			-27, 14, 80, -27, 14, 80, -27, 14, 8, 37, -120, -120, -120, -117,
			-108, 57, 67, -108, 57, 67, -108, 57, 67, -126, 9, 98, 34, 34, 34,
			-27, 14, 80, -27, 14, 80, -27, 14, 80, -32, -126, 88, -120, -120,
			-120, -71, 67, -108, 57, 67, -108, 57, 67, -108, 56, 32, -106, 34,
			34, 34, 44, 0, 64, 4 };

	private byte[] data3 = new byte[] { 35, -93, 8, -62, 4, 21, 32, 64, -110,
			4, 9, 32, 64, -110, 9, 69, -90, -102, 73, 65, 2, 72, 16, 36, -127,
			2, 73, 36, -83, 52, -45, 84, -127, 2, 72, 16, 36, -127, 2, 72, 37,
			105, 38, -110, 73, -46, 72, 16, 36, -110, 72, 16, 36, -83, 52, -45,
			84, -127, 2, 72, 16, 36, -127, 2, 72, 37, 22, -110, 73, -92, -108,
			16, 36, -110, 72, 16, 36, -110, 74, -45, 77, 53, 72, 16, 36, -127,
			2, 72, 16, 36, -126, 86, -110, 105, 36, -99, 36, -127, 2, 73, 36,
			-127, 2, 74, 0, 32, 2 };

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

		creator.saveState();
		creator.transform(200, 0, 0, 200, 50, 50);
		creator.perform("BI");
		creator
				.perform("/W 16 /H 16 /BPC 1 /CS /G /F /CCF /DP << /K -1 /Columns 16>> ID");
		creator.perform("EI", COSString.create(data1));
		creator.restoreState();

		creator.saveState();
		creator.transform(200, 0, 0, 200, 300, 50);
		creator.perform("BI");
		creator
				.perform("/W 16 /H 16 /BPC 1 /CS /G /F /CCF /DP << /K -1 /Columns 16>> ID");
		creator.perform("EI", COSString.create(data2));
		creator.restoreState();

		creator.saveState();
		creator.transform(200, 0, 0, 200, 50, 300);
		creator.perform("BI");
		creator
				.perform("/W 16 /H 16 /BPC 1 /CS /G /F /CCF /DP << /K -1 /Columns 16>> ID");
		creator.perform("EI", COSString.create(data3));
		creator.restoreState();

		// don't forget to flush the content.
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
