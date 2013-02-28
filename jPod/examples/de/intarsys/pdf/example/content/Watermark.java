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

import de.intarsys.pdf.cds.CDSRectangle;
import de.intarsys.pdf.content.CSContent;
import de.intarsys.pdf.content.TextState;
import de.intarsys.pdf.content.common.CSCreator;
import de.intarsys.pdf.example.common.CommonJPodExample;
import de.intarsys.pdf.font.PDFont;
import de.intarsys.pdf.font.PDFontType1;
import de.intarsys.pdf.pd.PDExtGState;
import de.intarsys.pdf.pd.PDForm;
import de.intarsys.pdf.pd.PDPage;

/**
 * Create a text watermark on every page of input document.
 */
public class Watermark extends CommonJPodExample {

	public static void main(String[] args) {
		Watermark client = new Watermark();
		try {
			client.run(args);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void run(String[] args) throws Exception {
		if (args.length < 2) {
			usage();
			return;
		}
		try {
			String inputFileName = args[0];
			String outputFileName = args[1];
			open(inputFileName);
			PDForm form = createForm();
			markPages(form);
			save(outputFileName);
		} finally {
			close();
		}
	}

	protected void markPages(PDForm form) {
		PDPage page = getDoc().getPageTree().getFirstPage();
		while (page != null) {
			markPage(page, form);
			page = page.getNextPage();
		}
	}

	protected void markPage(PDPage page, PDForm form) {
		float formWidth = form.getBoundingBox().getWidth();
		CDSRectangle rect = page.getCropBox();
		float scale = 1f;
		float offsetY = (rect.getHeight() - (formWidth * scale)) / 2;
		float offsetX = (rect.getWidth() - (formWidth * scale)) / 2;

		CSContent content = CSContent.createNew();
		CSCreator creator = CSCreator.createFromContent(content, page);

		creator.saveState();
		creator.transform(scale, 0, 0, scale, offsetX, offsetY);
		creator.doXObject(null, form);
		creator.restoreState();
		creator.close();

		page.cosAddContents(content.createStream());
	}

	private static final float halfSqrt2 = (float) (0.5 * Math.sqrt(2));

	protected PDForm createForm() {
		PDForm form = (PDForm) PDForm.META.createNew();
		PDFont font = PDFontType1.createNew(PDFontType1.FONT_Helvetica);
		PDExtGState extGState = (PDExtGState) PDExtGState.META.createNew();
		extGState.setStrokingAlphaConstant(0.6f);
		extGState.setNonStrokingAlphaConstant(0.2f);

		CSCreator creator = CSCreator.createNew(form);
		creator.textBegin();
		creator.setExtendedState(null, extGState);
		creator.setNonStrokeColorRGB(1.0f, 204f / 256f, 0); // orange
		creator.setStrokeColorRGB(1.0f, 204f / 256f, 0);
		creator.textSetFont(null, font, 100);
		creator.setLineWidth(2.0f);
		creator.textSetTransform(halfSqrt2, halfSqrt2, -halfSqrt2, halfSqrt2,
				80, 0);
		creator.textSetRenderingMode(TextState.RENDERING_MODE_FILL_STROKE);
		creator.textShow("Watermark"); //$NON-NLS-1$
		creator.textEnd();
		creator.close();

		form.setBoundingBox(new CDSRectangle(0, 0, 500, 600));

		return form;
	}

	/**
	 * Help the user.
	 */
	public void usage() {
		System.out.println("usage: java.exe " + getClass().getName() //$NON-NLS-1$
				+ " <input-filename> <output-filename>"); //$NON-NLS-1$
	}
}
