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
package de.intarsys.pdf.example.pd.misc;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import de.intarsys.pdf.cos.COSInfoDict;
import de.intarsys.pdf.example.common.CommonJPodExample;
import de.intarsys.tools.stream.StreamTools;

/**
 * Dump the document met information from the info dict.
 */
public class DumpInfo extends CommonJPodExample {

	public static void main(String[] args) {
		DumpInfo client = new DumpInfo();
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
			dumpInfo(outputFileName);
		} finally {
			close();
		}
	}

	/**
	 * Help the user.
	 */
	public void usage() {
		System.out.println("usage: java.exe " + getClass().getName() //$NON-NLS-1$
				+ " <input-pdf> <output-text>"); //$NON-NLS-1$
	}

	public void dumpInfo(String filename) {
		Writer w = null;
		try {
			w = new FileWriter(filename);
			COSInfoDict info = getDoc().getInfoDict();
			if (info == null) {
				return;
			}
			w.write("Title: \t"); //$NON-NLS-1$
			w.write(info.getTitle() == null ? "<na>" : info.getTitle()); //$NON-NLS-1$
			w.write(System.getProperty("line.separator")); //$NON-NLS-1$
			w.write("Subject: \t"); //$NON-NLS-1$
			w.write(info.getSubject() == null ? "<na>" : info.getSubject()); //$NON-NLS-1$
			w.write(System.getProperty("line.separator")); //$NON-NLS-1$
			w.write("Author: \t"); //$NON-NLS-1$
			w.write(info.getAuthor() == null ? "<na>" : info.getAuthor()); //$NON-NLS-1$
			w.write(System.getProperty("line.separator")); //$NON-NLS-1$
			w.write("Creation: \t"); //$NON-NLS-1$
			w.write(info.getCreationDateString() == null ? "<na>" : info.getCreationDateString()); //$NON-NLS-1$
			w.write(System.getProperty("line.separator")); //$NON-NLS-1$
			w.write("Modified: \t"); //$NON-NLS-1$
			w.write(info.getModDateString() == null ? "<na>" : info.getModDateString()); //$NON-NLS-1$
			w.write(System.getProperty("line.separator")); //$NON-NLS-1$
			w.write("Creator: \t"); //$NON-NLS-1$
			w.write(info.getCreator() == null ? "<na>" : info.getCreator()); //$NON-NLS-1$
			w.write(System.getProperty("line.separator")); //$NON-NLS-1$
			w.write("Producer: \t"); //$NON-NLS-1$
			w.write(info.getProducer() == null ? "<na>" : info.getProducer()); //$NON-NLS-1$
			w.write(System.getProperty("line.separator")); //$NON-NLS-1$
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			StreamTools.close(w);
		}
	}
}
