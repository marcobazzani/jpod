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
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;

import de.intarsys.tools.converter.ConversionException;
import de.intarsys.tools.converter.ConverterRegistry;
import de.intarsys.tools.encoding.Base64;
import de.intarsys.tools.functor.ArgTools;
import de.intarsys.tools.functor.IArgs;
import de.intarsys.tools.locator.ILocator;
import de.intarsys.tools.locator.ILocatorSupport;
import de.intarsys.tools.locator.LocatorTools;
import de.intarsys.tools.string.StringTools;

/**
 * Tools for dealing with digests.
 * 
 */
public class DigestTools {

	public static IDigest createDigest(Object value) throws IOException {
		if (value == null) {
			return null;
		}
		if (value instanceof IDigest) {
			return (IDigest) value;
		}
		if (value instanceof String) {
			if (StringTools.isEmpty((String) value)) {
				return null;
			}
			value = Base64.decode((String) value);
		}
		if (value instanceof ILocator) {
			value = LocatorTools.getBytes((ILocator) value);
		}
		if (value instanceof ILocatorSupport) {
			value = LocatorTools.getBytes(((ILocatorSupport) value)
					.getLocator());
		}
		if (value instanceof byte[]) {
			return DigestEnvironment.get().decode((byte[]) value);
		}
		if (value instanceof IArgs) {
			IArgs tempArgs = (IArgs) value;
			byte[] bytes = ArgTools.getByteArray(tempArgs, "der", null);
			if (bytes == null) {
				bytes = ArgTools.getByteArray(tempArgs, "raw", null);
				String hashAlgorithm = ArgTools.getString(tempArgs,
						"algorithm", "SHA256");
				if (bytes != null) {
					return DigestEnvironment.get().createDigest(hashAlgorithm,
							bytes);
				}
			} else {
				return DigestEnvironment.get().decode(bytes);
			}
		}
		try {
			return ConverterRegistry.get().convert(value, IDigest.class);
		} catch (ConversionException e) {
			throw new IOException("can't convert " + value + " to digest");
		}
	}

	public static IDigester createDigesterSHA1() {
		try {
			return DigestEnvironment.get().createDigester("SHA1");
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalStateException("SHA1 digest not supported");
		}
	}

	public static IDigest digest(IDigester digester, InputStream is)
			throws IOException {
		return digest(digester, is, 1024);
	}

	public static IDigest digest(IDigester digester, InputStream is,
			int bufferSize) throws IOException {
		byte[] buffer = new byte[bufferSize];
		int i = is.read(buffer);
		while (i != -1) {
			digester.update(buffer, 0, i);
			i = is.read(buffer);
		}
		return digester.digest();
	}

	public static int suggestBufferSize(long totalSize) {
		int bufferSize;
		if (totalSize < (32 * 1024)) { // < 32kb
			bufferSize = 1024; // 1kb
		} else if (totalSize < (1 * 1024 * 1024)) { // < 1 MB
			bufferSize = 32 * 1024; // 32kb
		} else { // > 1 MB
			bufferSize = 96 * 1024; // 96kb
		}
		return bufferSize;
	}

}
