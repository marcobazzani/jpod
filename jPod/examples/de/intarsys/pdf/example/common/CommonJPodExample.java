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
package de.intarsys.pdf.example.common;

import java.io.IOException;

import de.intarsys.pdf.parser.COSLoadException;
import de.intarsys.pdf.pd.PDDocument;
import de.intarsys.tools.locator.FileLocator;

/**
 * Common superclass for jPod examples.
 */
public class CommonJPodExample {
	private PDDocument doc;

	protected PDDocument basicOpen(String pathname) throws IOException,
			COSLoadException {
		FileLocator locator = new FileLocator(pathname);
		return PDDocument.createFromLocator(locator);
	}

	protected void basicSave(PDDocument doc, String outputFileName)
			throws IOException {
		FileLocator locator = new FileLocator(outputFileName);
		doc.save(locator, null);
	}

	/**
	 * Close the current document.
	 * 
	 * @throws IOException
	 */
	public void close() throws IOException {
		if (getDoc() != null) {
			getDoc().close();
		}
	}

	/**
	 * Create a new document.
	 */
	public void create() {
		// First create a new document.
		setDoc(PDDocument.createNew());
		// You could add more information about the environment:
		getDoc().setAuthor("intarsys consulting GmbH"); //$NON-NLS-1$
		getDoc().setCreator("intarsys PDF API"); //$NON-NLS-1$
	}

	/**
	 * The current document.
	 * 
	 * @return The current document.
	 */
	public PDDocument getDoc() {
		return doc;
	}

	/**
	 * Open a document.
	 * 
	 * @param pathname
	 *            The path name to the document.
	 * @throws COSLoadException
	 * @throws IOException
	 */
	public void open(String pathname) throws IOException, COSLoadException {
		setDoc(basicOpen(pathname));
	}

	/**
	 * Save current document to path.
	 * 
	 * @param outputFileName
	 *            The destination path for the document.
	 * @throws IOException
	 */
	public void save(String outputFileName) throws IOException {
		basicSave(getDoc(), outputFileName);
	}

	/**
	 * Set the current document.
	 * 
	 * @param doc
	 *            The new current document.
	 */
	protected void setDoc(PDDocument doc) {
		this.doc = doc;
	}
}
