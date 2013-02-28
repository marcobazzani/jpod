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
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import de.intarsys.tools.encoding.Base64;
import de.intarsys.tools.exception.ExceptionTools;
import de.intarsys.tools.string.StringTools;

/**
 * A tool class for handling en/decryption
 * 
 */
public class CryptoEnvironment {

	final private static CryptoEnvironment ACTIVE = new CryptoEnvironment();

	static public CryptoEnvironment get() {
		return ACTIVE;
	}

	private Map<String, ICryptdec> cryptdecs = new HashMap<String, ICryptdec>();

	private ICryptdec defaultCryptdecEncrypt;

	private ICryptdec defaultCryptdecDecrypt;

	private CryptoEnvironment() {
	}

	/**
	 * Decrypt a byte array which was previously encrypted using
	 * <code>encrypt</code>. For decryption the default {@link ICryptdec} is
	 * used.
	 * 
	 * @param bytes
	 * @return The decrypted representation of <code>bytes</code>
	 * @throws IOException
	 */
	public byte[] decrypt(byte[] bytes) throws IOException {
		if (defaultCryptdecDecrypt == null) {
			throw new IllegalStateException("default cryptdec not defined");
		}
		return defaultCryptdecDecrypt.decrypt(bytes);
	}

	public String decryptEncoded(String value) throws IOException {
		return decryptEncoded(value, defaultCryptdecDecrypt);
	}

	public String decryptEncoded(String value, ICryptdec cryptdec)
			throws IOException {
		try {
			String[] parts = value.split("#");
			String tempValue;
			ICryptdec tempCryptdec;
			if (parts.length == 1) {
				tempCryptdec = cryptdec;
				tempValue = parts[0];
			} else {
				tempCryptdec = lookupCryptdec(parts[0]);
				tempValue = parts[1];
			}
			if (tempCryptdec == null) {
				throw new IOException("unknown encryption scheme");
			}
			if (StringTools.isEmpty(tempValue)) {
				return tempValue;
			}
			byte[] bytes = Base64.decode(StringTools.toByteArray(tempValue));
			byte[] decrypted = tempCryptdec.decrypt(bytes);
			return new String(decrypted, "UTF8");
		} catch (UnsupportedEncodingException e) {
			throw ExceptionTools.createIOException(e.getMessage(), e);
		}
	}

	/**
	 * Decrypt a string which was previously encrypted using
	 * <code>encrypt</code>. Provided the same salt and passphrase are used for
	 * initialization, this method returns the original unencrypted input.
	 * 
	 * @param value
	 * @return The decrypted representation of <code>value</code>
	 * @throws IOException
	 */
	public String decryptRaw(String value) throws IOException {
		try {
			if (StringTools.isEmpty(value)) {
				return value;
			}
			byte[] bytes = Base64.decode(StringTools.toByteArray(value));
			byte[] decrypted = decrypt(bytes);
			return new String(decrypted, "UTF8");
		} catch (UnsupportedEncodingException e) {
			throw ExceptionTools.createIOException(e.getMessage(), e);
		}
	}

	/**
	 * Encrypt a clear text array of bytes. The result is the plain encrypted
	 * byte array.
	 * 
	 * @param bytes
	 * @return The encrypted representation of <code>bytes</code>
	 * @throws IOException
	 */
	public byte[] encrypt(byte[] bytes) throws IOException {
		if (defaultCryptdecEncrypt == null) {
			throw new IllegalStateException("default cryptdec not defined");
		}
		return defaultCryptdecEncrypt.encrypt(bytes);
	}

	public String encryptEncoded(char[] value) throws IOException {
		if (defaultCryptdecEncrypt == null) {
			throw new IllegalStateException("default cryptdec not defined");
		}
		return encryptEncoded(value, defaultCryptdecEncrypt);
	}

	public String encryptEncoded(char[] value, ICryptdec cryptdec)
			throws IOException {
		try {
			if (cryptdec == null) {
				throw new NullPointerException("cryptdec not defined");
			}
			byte[] bytes = new String(value).getBytes("UTF8");
			byte[] encrypted = encrypt(bytes);
			return cryptdec.getId() + "#"
					+ new String(Base64.encode(encrypted));
		} catch (UnsupportedEncodingException e) {
			throw ExceptionTools.createIOException(e.getMessage(), e);
		}
	}

	public String encryptEncoded(String value) throws IOException {
		if (defaultCryptdecEncrypt == null) {
			throw new IllegalStateException("default cryptdec not defined");
		}
		return encryptEncoded(value, defaultCryptdecEncrypt);
	}

	public String encryptEncoded(String value, ICryptdec cryptdec)
			throws IOException {
		try {
			if (cryptdec == null) {
				throw new NullPointerException("cryptdec not defined");
			}
			byte[] bytes = value.getBytes("UTF8");
			byte[] encrypted = encrypt(bytes);
			return cryptdec.getId() + "#"
					+ new String(Base64.encode(encrypted));
		} catch (UnsupportedEncodingException e) {
			throw ExceptionTools.createIOException(e.getMessage(), e);
		}
	}

	/**
	 * Encrypt a clear text array of chars. The result is a Base64 encoded
	 * string version of the encrypted UTF-8 encoded input bytes.
	 * 
	 * @param value
	 * @return An encrypted, invertible representation of <code>value</code>
	 * @throws IOException
	 */
	public String encryptRaw(String value) throws IOException {
		try {
			byte[] bytes = value.getBytes("UTF8");
			byte[] encrypted = encrypt(bytes);
			return new String(Base64.encode(encrypted));
		} catch (UnsupportedEncodingException e) {
			throw ExceptionTools.createIOException(e.getMessage(), e);
		}
	}

	public ICryptdec getDefaultCryptdecDecrypt() {
		return defaultCryptdecDecrypt;
	}

	public ICryptdec getDefaultCryptdecEncrypt() {
		return defaultCryptdecEncrypt;
	}

	public ICryptdec lookupCryptdec(String id) {
		return cryptdecs.get(id);
	}

	public void registerCryptdec(ICryptdec cryptdec) {
		if (cryptdecs.containsKey(cryptdec.getId())) {
			throw new IllegalStateException("can't redefine cryptdecs");
		}
		cryptdecs.put(cryptdec.getId(), cryptdec);
	}

	public void setDefaultCryptdecDecrypt(ICryptdec defaultCryptdec) {
		if (this.defaultCryptdecDecrypt != null) {
			throw new IllegalStateException("can't redefine cryptdecs");
		}
		this.defaultCryptdecDecrypt = defaultCryptdec;
	}

	public void setDefaultCryptdecEncrypt(ICryptdec defaultCryptdec) {
		if (this.defaultCryptdecEncrypt != null) {
			throw new IllegalStateException("can't redefine cryptdecs");
		}
		this.defaultCryptdecEncrypt = defaultCryptdec;
	}
}
