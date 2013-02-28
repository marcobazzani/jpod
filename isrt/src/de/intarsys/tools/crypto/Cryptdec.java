/*
 * Copyright (c) 2012, intarsys consulting GmbH
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
package de.intarsys.tools.crypto;

import java.io.IOException;
import java.util.Arrays;

import javax.crypto.Cipher;

import de.intarsys.tools.exception.ExceptionTools;

/**
 * 
 */
abstract public class Cryptdec implements ICryptdec {

	final private String id;

	private byte[] initializationVector;

	private Cipher ecipher;

	private Cipher dcipher;

	public Cryptdec(String id, byte[] iv) {
		this.id = id;
		this.initializationVector = Arrays.copyOf(iv, iv.length);
	}

	/**
	 * Decrypt a byte array.
	 * 
	 * @param bytes
	 * @return The decrypted representation of <code>bytes</code>
	 * @throws IOException
	 */
	public byte[] decrypt(byte[] bytes) throws IOException {
		try {
			return dcipher.doFinal(bytes);
		} catch (Exception e) {
			throw ExceptionTools.createIOException(e.getMessage(), e);
		}
	}

	/**
	 * Encrypt a byte array.
	 * 
	 * @param bytes
	 * @return The encrypted representation of <code>bytes</code>
	 * @throws IOException
	 */
	public byte[] encrypt(byte[] bytes) throws IOException {
		try {
			return ecipher.doFinal(bytes);
		} catch (Exception e) {
			throw ExceptionTools.createIOException(e.getMessage(), e);
		}
	}

	protected Cipher getDcipher() {
		return dcipher;
	}

	protected Cipher getEcipher() {
		return ecipher;
	}

	public String getId() {
		return id;
	}

	public byte[] getInitializationVector() {
		return initializationVector;
	}

	protected void setDcipher(Cipher dcipher) {
		this.dcipher = dcipher;
	}

	protected void setEcipher(Cipher ecipher) {
		this.ecipher = ecipher;
	}

	protected void setInitializationVector(byte[] initializationVector) {
		this.initializationVector = initializationVector;
	}

}
