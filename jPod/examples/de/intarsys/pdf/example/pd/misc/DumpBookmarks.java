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
import java.util.Iterator;
import de.intarsys.pdf.example.common.CommonJPodExample;
import de.intarsys.pdf.pd.PDOutline;
import de.intarsys.pdf.pd.PDOutlineItem;
import de.intarsys.pdf.pd.PDOutlineNode;
import de.intarsys.tools.stream.StreamTools;

/**
 * Dump all bookmarks of document to file.
 */
public class DumpBookmarks extends CommonJPodExample {

	public static void main(String[] args) {
		DumpBookmarks client = new DumpBookmarks();
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
			dumpBookmarks(outputFileName);
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

	public void dumpBookmarks(String filename) {
		Writer w = null;
		try {
			w = new FileWriter(filename);
			PDOutline outline = getDoc().getOutline();
			if (outline == null) {
				return;
			}
			dumpBookmarks(w, outline, 0);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			StreamTools.close(w);
		}
	}

	protected void dumpBookmarks(Writer w, PDOutlineNode parent, int level) throws IOException {
		for (Iterator it = parent.getChildren().iterator(); it.hasNext();) {
			PDOutlineItem item = (PDOutlineItem) it.next();
			for (int i = 0; i < level; i++) {
				w.write("    "); //$NON-NLS-1$
			}
			w.write(item.getTitle());
			w.write(System.getProperty("line.separator")); //$NON-NLS-1$
			dumpBookmarks(w, item, level + 1);
		}
	}
}
