package de.intarsys.tools.functor;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.cert.Certificate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.intarsys.tools.encoding.Base64;
import de.intarsys.tools.string.StringTools;

public class ArgsSigner {

	/**
	 * Create an {@link IArgs} structure defining the hash over args, using
	 * digest.
	 * 
	 * @param digest
	 * @param args
	 * @return
	 * @throws GeneralSecurityException
	 */
	protected IArgs createHashArgs(MessageDigest digest, IArgs args)
			throws GeneralSecurityException {
		IArgs hashArgs = Args.create();
		try {
			updateHash(digest, args);
		} catch (Exception e) {
			Throwable cause = e.getCause() == null ? e : e.getCause();
			if (cause instanceof GeneralSecurityException) {
				throw (GeneralSecurityException) cause;
			}
			throw new GeneralSecurityException(cause);
		}
		//
		hashArgs.put("algorithm", digest.getAlgorithm());
		byte[] hashValue = digest.digest();
		hashArgs.put("value", new String(Base64.encode(hashValue)));
		return hashArgs;
	}

	/**
	 * Compute the signature value on args. the result is the Base64 encoded
	 * signature.
	 * 
	 * @param signature
	 * @param privateKey
	 * @param args
	 * @return
	 * @throws GeneralSecurityException
	 */
	protected String createSignatureValue(Signature signature,
			PrivateKey privateKey, IArgs args) throws GeneralSecurityException {
		signature.initSign(privateKey);
		try {
			updateSignature(signature, args);
		} catch (Exception e) {
			Throwable cause = e.getCause() == null ? e : e.getCause();
			if (cause instanceof GeneralSecurityException) {
				throw (GeneralSecurityException) cause;
			}
			throw new GeneralSecurityException(cause);
		}
		byte[] signatureValue = signature.sign();
		return new String(Base64.encode(signatureValue));
	}

	/**
	 * Create the {@link IArgs} structure containing the signed properties of
	 * the signature.
	 * 
	 * @param args
	 * @param signature
	 * @param fields
	 * @param digest
	 * @return
	 * @throws GeneralSecurityException
	 */
	protected IArgs createSignedArgs(IArgs args, Signature signature,
			List<String> fields, MessageDigest digest)
			throws GeneralSecurityException {
		IArgs signedArgs = Args.create();
		signedArgs.put("version", "1.0");
		signedArgs.put("algorithm", signature.getAlgorithm());
		String selectString = StringTools.join(fields, ";");
		signedArgs.put("select", selectString);
		IArgs signedContent = createSignedContentArgs(args, fields);
		IArgs hash = createHashArgs(digest, signedContent);
		signedArgs.put("hash", hash);
		return signedArgs;
	}

	/**
	 * Create the {@link IArgs} structure containing the signed part of the
	 * overall args, filtered using the path tokens from fields.
	 * 
	 * @param args
	 * @param fields
	 * @return
	 */
	protected IArgs createSignedContentArgs(IArgs args, List<String> fields) {
		IArgs signedContent = Args.create();
		for (String field : fields) {
			updateSubtree(signedContent, args, field);
		}
		return signedContent;
	}

	/**
	 * Create an {@link IArgs} structure defining the signer principal.
	 * 
	 * @param publicKey
	 * @return
	 */
	protected IArgs createSignerArgs(PublicKey publicKey,
			List<Certificate> certificates) {
		IArgs signerArgs = Args.create();
		if (publicKey != null) {
			IArgs keyArgs = Args.create();
			signerArgs.put("key", keyArgs);
			keyArgs.put("type", publicKey.getFormat());
			byte[] value = publicKey.getEncoded();
			keyArgs.put("value", new String(Base64.encode(value)));
		}
		if (certificates != null) {
			// todo
		}
		return signerArgs;
	}

	/**
	 * The {@link IArgs} structure containing the signature on args, restricted
	 * to the subtree defined by selected.
	 * 
	 * @param args
	 * @param fields
	 * @param digest
	 * @param signature
	 * @param privateKey
	 * @param publicKey
	 * @return The {@link IArgs} structure containing the signature on args,
	 *         restricted to the subtree defined by selected.
	 * @throws GeneralSecurityException
	 */
	public IArgs sign(IArgs args, List<String> fields, MessageDigest digest,
			Signature signature, PrivateKey privateKey, PublicKey publicKey,
			List<Certificate> certificates) throws GeneralSecurityException {
		IArgs signatureArgs = Args.create();
		signatureArgs.put("type", "ArgDSig");
		IArgs signatureValueArgs = Args.create();
		signatureArgs.put("value", signatureValueArgs);
		IArgs signedArgs = createSignedArgs(args, signature, fields, digest);
		signatureValueArgs.put("signed", signedArgs);
		IArgs signerArgs = createSignerArgs(publicKey, certificates);
		signatureValueArgs.put("signer", signerArgs);
		String signatureValue = createSignatureValue(signature, privateKey,
				signedArgs);
		signatureValueArgs.put("value", signatureValue);
		return signatureArgs;
	}

	/**
	 * Update the {@link MessageDigest} with the args content.
	 * 
	 * @param digest
	 * @param args
	 * @throws IOException
	 */
	protected void updateHash(final MessageDigest digest, IArgs args)
			throws IOException {
		OutputStream os = new OutputStream() {
			@Override
			public void write(byte[] b, int off, int len) throws IOException {
				digest.update(b, off, len);
			}

			@Override
			public void write(int b) throws IOException {
				digest.update((byte) b);
			}
		};
		writeArgs(os, args);
	}

	/**
	 * Update the {@link Signature} with the args content.
	 * 
	 * @param signature
	 * @param args
	 * @throws IOException
	 */
	protected void updateSignature(final Signature signature, IArgs args)
			throws IOException {
		OutputStream os = new OutputStream() {
			@Override
			public void write(byte[] b, int off, int len) throws IOException {
				try {
					signature.update(b, off, len);
				} catch (SignatureException e) {
					throw new IOException(e);
				}
			}

			@Override
			public void write(int b) throws IOException {
				try {
					signature.update((byte) b);
				} catch (SignatureException e) {
					throw new IOException(e);
				}
			}
		};
		writeArgs(os, args);
	}

	/**
	 * Update the {@link IArgs} subtree of args, according to the field
	 * specification.
	 * 
	 * @param subtree
	 * @param args
	 * @param field
	 */
	protected void updateSubtree(IArgs subtree, IArgs args, String field) {
		if ("*".equals(field)) {
			ArgTools.putAll(subtree, args);
		} else if (field.startsWith("!")) {
			field = field.substring(1);
			ArgTools.putPath(subtree, field, null);
		} else {
			Object value = ArgTools.getObject(args, field, null);
			ArgTools.putPath(subtree, field, value);
		}
	}

	protected void writeArg(OutputStream os, String key, Object value)
			throws IOException, UnsupportedEncodingException {
		os.write(key.getBytes("UTF-8"));
		os.write("=".getBytes("UTF-8"));
		if (value instanceof IArgs) {
			os.write("{\n".getBytes("UTF-8"));
			writeArgs(os, (IArgs) value);
			os.write("\n}".getBytes("UTF-8"));
		} else {
			String string = String.valueOf(value);
			os.write(string.getBytes("UTF-8"));
		}
		os.write("\n".getBytes("UTF-8"));
	}

	/**
	 * Write a canonical form of args to the output stream.
	 * 
	 * @param os
	 * @param args
	 * @throws IOException
	 */
	protected void writeArgs(OutputStream os, IArgs args) throws IOException {
		List<String> names = new ArrayList<String>(args.names());
		if (names.isEmpty()) {
			for (int i = 0; i < args.size(); i++) {
				String key = "" + i;
				Object value = args.get(i);
				writeArg(os, key, value);
			}
		} else {
			Collections.sort(names);
			for (String name : names) {
				String key = name;
				Object value = args.get(name);
				writeArg(os, key, value);
			}
		}
	}

}
