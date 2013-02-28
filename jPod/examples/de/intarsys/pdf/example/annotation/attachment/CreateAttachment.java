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
package de.intarsys.pdf.example.annotation.attachment;

import de.intarsys.pdf.example.common.CommonJPodExample;
import de.intarsys.pdf.pd.PDEmbeddedFile;
import de.intarsys.pdf.pd.PDFileAttachmentAnnotation;
import de.intarsys.pdf.pd.PDFileSpecification;
import de.intarsys.pdf.pd.PDPage;

/**
 * Create a new document and a file attachment.
 */
public class CreateAttachment extends CommonJPodExample {

	public static void main(String[] args) {
		CreateAttachment client = new CreateAttachment();
		try {
			client.run(args);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void addAttachment(PDPage page) {
		PDFileAttachmentAnnotation annot = (PDFileAttachmentAnnotation) PDFileAttachmentAnnotation.META
				.createNew();
		page.addAnnotation(annot);

		annot.setPrint(false);
		annot.setColor(new float[] { 1, 1, 0 });

		PDFileAttachmentAnnotation faAnnot = annot;
		PDFileSpecification fileSpec = (PDFileSpecification) PDFileSpecification.META
				.createNew();
		fileSpec.setFileSpecificationString(PDFileSpecification.DK_F,
				"test.txt");
		faAnnot.setFileSpecification(fileSpec);
		faAnnot.setContents("test.txt");
		faAnnot.setSubject("File Attachment");
		PDEmbeddedFile embeddedFile = (PDEmbeddedFile) PDEmbeddedFile.META
				.createNew();
		embeddedFile.setBytes("Hello, world!".getBytes());
		fileSpec.setEmbeddedFile(PDFileSpecification.DK_F, embeddedFile);
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
			addAttachment(page);
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
