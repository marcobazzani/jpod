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
package de.intarsys.tools.digest;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.intarsys.tools.provider.Providers;

/**
 * The standard implementation for {@link IDigestEnvironment}.
 * 
 */
public class StandardDigestEnvironment implements IDigestEnvironment {

	private IDigestCodec codec;

	protected IDigestCodec createCodec() {
		Iterator<IDigestCodec> it = Providers.get().lookupProviders(
				IDigestCodec.class);
		while (it.hasNext()) {
			try {
				return it.next();
			} catch (Throwable t) {
				Logger.getAnonymousLogger().log(Level.WARNING,
						"error creating codec", t);
			}
		}
		return null;
	}

	public IDigest createDigest(String algorithmName, byte[] bytes) {
		return new Digest(algorithmName, bytes);
	}

	public IDigester createDigester(String algorithmName)
			throws NoSuchAlgorithmException {
		try {
			MessageDigest digest = MessageDigest.getInstance(algorithmName,
					"BC"); //$NON-NLS-1$
			return new Digester(algorithmName, digest);
		} catch (NoSuchProviderException e) {
			MessageDigest digest = MessageDigest.getInstance(algorithmName);
			return new Digester(algorithmName, digest);
		}
	}

	@Override
	public IDigest decode(byte[] bytes) throws IOException {
		if (getCodec() == null) {
			throw new IOException("no digest codec installed");
		}
		return getCodec().decode(bytes);
	}

	@Override
	public byte[] encode(IDigest digest) throws IOException {
		if (getCodec() == null) {
			throw new IOException("no digest codec installed");
		}
		return getCodec().encode(digest);
	}

	protected IDigestCodec getCodec() {
		if (codec == null) {
			codec = createCodec();
		}
		return codec;
	}
}
