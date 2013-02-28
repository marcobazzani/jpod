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

import de.intarsys.pdf.content.CSContent;
import de.intarsys.pdf.content.common.CSCreator;
import de.intarsys.pdf.cos.COSObject;
import de.intarsys.pdf.example.common.CommonJPodExample;
import de.intarsys.pdf.pd.PDDocument;
import de.intarsys.pdf.pd.PDForm;
import de.intarsys.pdf.pd.PDPage;
import de.intarsys.pdf.pd.PDResources;

/**
 * Use a PDF document as an overlay for another document. The first page of the
 * overlay document is "printed" over the content stream of every page of the
 * input document.
 * 
 */
public class Overlay extends CommonJPodExample {

	public static void main(String[] args) {
		Overlay client = new Overlay();
		try {
			client.run(args);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void run(String[] args) throws Exception {
		if (args.length < 3) {
			usage();
			return;
		}
		try {
			String inputFileName = args[0];
			String overlayFileName = args[1];
			String outputFileName = args[2];
			open(inputFileName);
			PDDocument overlay = basicOpen(overlayFileName);
			PDForm form = createForm(overlay);
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
		CSContent content = CSContent.createNew();
		CSCreator creator = CSCreator.createFromContent(content, page);
		creator.saveState();
		// maybe you should ensure here, that current graphics state is suitable
		// to draw form
		creator.doXObject(null, form);
		creator.restoreState();
		creator.close();
		page.cosAddContents(content.createStream());
	}

	protected PDForm createForm(PDDocument document) {
		PDForm form = (PDForm) PDForm.META.createNew();
		PDPage pdPage = document.getPageTree().getFirstPage();
		CSContent content = pdPage.getContentStream();
		if (pdPage.getResources() != null) {
			COSObject cosResourcesCopy = pdPage.getResources().cosGetObject()
					.copyDeep();
			PDResources pdResourcesCopy = (PDResources) PDResources.META
					.createFromCos(cosResourcesCopy);
			form.setResources(pdResourcesCopy);
		}
		form.setBytes(content.toByteArray());
		form.setBoundingBox(pdPage.getCropBox().copy());
		return form;
	}

	/**
	 * Help the user.
	 */
	public void usage() {
		System.out.println("usage: java.exe " + getClass().getName() //$NON-NLS-1$
				+ " <input-filename> <overlay-filename> <output-filename>"); //$NON-NLS-1$
	}
}
