package de.flapdoodle.unravel;

import com.google.common.base.Preconditions;

import de.flapdoodle.unravel.io.Jars;
import de.flapdoodle.unravel.signature.Signature;
import de.flapdoodle.unravel.signature.SignatureMatcher;

public class ScarabJarMatcher {

	public static void main(String[] args) {
		Preconditions.checkArgument(args.length >= 2, "usage: <jarfile> <jarFile2>");
		String jarFileA = args[0];
		String jarFileB = args[0];

		Signature signatureA = Jars.signatureOfJar(jarFileA);
		Signature signatureB = Jars.signatureOfJar(jarFileB);
		
		SignatureMatcher.match(signatureA, signatureB);
	}

}
