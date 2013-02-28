package de.intarsys.tools.functor;

import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.util.List;

public class ArgsValidator {

	protected byte[] basicvalidate(IArgs args) throws GeneralSecurityException {
		IArgs signatureArgs = ArgTools
				.getArgs(args, "signature", Args.create());
		String signatureValue = ArgTools
				.getString(signatureArgs, "value", null);
		//
		IArgs keyArgs = ArgTools.getArgs(args, "key", Args.create());
		String keyType = ArgTools.getString(keyArgs, "type", null);
		String certificate = ArgTools.getString(keyArgs, "certificate", null);
		//
		IArgs signedArgs = ArgTools.getArgs(args, "signed", Args.create());
		String type = ArgTools.getString(signedArgs, "type", "plain");
		String version = ArgTools.getString(signedArgs, "version", "1.0");
		String signatureAlgorithm = ArgTools.getString(signedArgs, "algorithm",
				null);
		List<String> select = ArgTools.getList(signedArgs, "select", null);
		IArgs hashArgs = ArgTools.getArgs(signedArgs, "hash", Args.create());
		String hashAlgorithm = ArgTools.getString(hashArgs, "algorithm",
				"SHA256");
		String hashValue = ArgTools.getString(hashArgs, "value", null);
		MessageDigest digest = MessageDigest.getInstance(hashAlgorithm, "BC");
		return null;
	}

	public void validate(IArgs args, IArgs signatureArgs) {

	}
}
