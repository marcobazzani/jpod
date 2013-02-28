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
package de.intarsys.pdf.example.crypt;

import de.intarsys.pdf.cos.COSDocument;
import de.intarsys.pdf.crypt.COSSecurityException;
import de.intarsys.pdf.crypt.PasswordProvider;
import de.intarsys.pdf.example.common.CommonJPodExample;
import de.intarsys.pdf.st.STDocument;
import de.intarsys.tools.authenticate.IPasswordProvider;

/**
 * Remove encryption from document using the commandline password.
 * <p>
 * Remember that you must use the owner password for this to work.
 * 
 */
public class DecryptDoc extends CommonJPodExample {

	public static void main(String[] args) {
		DecryptDoc client = new DecryptDoc();
		try {
			client.run(args);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void removeEncryption() throws COSSecurityException {
		COSDocument cosDoc = getDoc().cosGetDoc();
		STDocument stDoc = cosDoc.stGetDoc();
		stDoc.setSystemSecurityHandler(null);
	}

	public void run(String[] args) throws Exception {
		if (args.length < 3) {
			usage();
			return;
		}
		try {
			String inputFileName = args[0];
			String outputFileName = args[1];
			String password = args[2];

			// authentication is done upon opening the file
			// set up a system wide password provider before
			setUpAuthentication(password.toCharArray());

			open(inputFileName);
			removeEncryption();
			save(outputFileName);

		} finally {
			close();
		}
	}

	protected void setUpAuthentication(final char[] password) {
		PasswordProvider.set(new IPasswordProvider() {
			public char[] getPassword() {
				return password.clone();
			}
		});
	}

	/**
	 * Help the user.
	 */
	public void usage() {
		System.out.println("usage: java.exe " + getClass().getName() //$NON-NLS-1$
				+ " <input-filename> <output-filename> <password>"); //$NON-NLS-1$
	}
}
