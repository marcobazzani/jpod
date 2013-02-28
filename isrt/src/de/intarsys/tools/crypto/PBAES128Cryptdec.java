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

import java.security.GeneralSecurityException;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class PBAES128Cryptdec extends PBCryptdec {

	public PBAES128Cryptdec(String id, byte[] iv, char[] passphrase,
			byte[] salt, int iterationCount) throws GeneralSecurityException {
		super(id, iv, passphrase, salt);
		init(passphrase, salt, iterationCount);
	}

	protected String getCipherAlgorithmId() {
		return "AES";
	}

	protected String getCipherId() {
		return "AES/CBC/PKCS5Padding";
	}

	protected int getKeyLength() {
		return 128;
	}

	protected String getPBKeyFactoryId() {
		return "PBKDF2WithHmacSHA1";
	}

	private void init(char[] passphrase, byte[] salt, int iterationCount)
			throws GeneralSecurityException {
		KeySpec pbKeySpec = new PBEKeySpec(passphrase, salt, iterationCount,
				getKeyLength());
		SecretKeyFactory pbKeyFactory = SecretKeyFactory
				.getInstance(getPBKeyFactoryId());
		SecretKey pbSecret = pbKeyFactory.generateSecret(pbKeySpec);

		SecretKey key = new SecretKeySpec(pbSecret.getEncoded(),
				getCipherAlgorithmId());

		setEcipher(Cipher.getInstance(getCipherId()));
		setDcipher(Cipher.getInstance(getCipherId()));

		if (getInitializationVector() != null) {
			IvParameterSpec ips = new IvParameterSpec(getInitializationVector());
			getEcipher().init(Cipher.ENCRYPT_MODE, key, ips);
			getDcipher().init(Cipher.DECRYPT_MODE, key, ips);
		} else {
			getEcipher().init(Cipher.ENCRYPT_MODE, key);
			setInitializationVector(getEcipher().getIV());
			IvParameterSpec ips = new IvParameterSpec(getInitializationVector());
			getDcipher().init(Cipher.DECRYPT_MODE, key, ips);
		}
	}
}
