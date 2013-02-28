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
import java.security.NoSuchAlgorithmException;

/**
 * Factory methods for {@link IDigest} and {@link IDigester}.
 * 
 */
public interface IDigestEnvironment {

	/**
	 * An {@link IDigest} object from an algorithm name and the raw bytes.
	 * 
	 * @param algorithmName
	 * @param bytes
	 * @return An {@link IDigest} object from an algorithm name and the raw
	 *         bytes.
	 */
	public IDigest createDigest(String algorithmName, byte[] bytes);

	/**
	 * An {@link IDigester} for the given "algorithmName".
	 * 
	 * @param algorithmName
	 * @return An {@link IDigester} for the given "algorithmName".
	 * @throws NoSuchAlgorithmException
	 */
	public IDigester createDigester(String algorithmName)
			throws NoSuchAlgorithmException;

	/**
	 * Decode an DER encoded representation to an {@link IDigest}.
	 * 
	 * @param bytes
	 * @return The decoded {@link IDigest}
	 * @throws IOException
	 *             If no decoder installed or "bytes" are not a correct encoded
	 *             DER representation.
	 */
	public IDigest decode(byte[] bytes) throws IOException;

	/**
	 * Encode an {@link IDigest} to a DER representation.
	 * 
	 * @param digest
	 * @return The DER encoded IDigest
	 * @throws IOException
	 *             If no decoder installed.
	 */
	public byte[] encode(IDigest digest) throws IOException;

}
