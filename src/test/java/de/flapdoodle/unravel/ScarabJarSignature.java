package de.flapdoodle.unravel;

import com.google.common.base.Preconditions;

import de.flapdoodle.unravel.io.Jars;
import de.flapdoodle.unravel.signature.Signature;
import de.flapdoodle.unravel.signature.Signature2Text;

public class ScarabJarSignature {

	public static void main(String[] args) {
		Preconditions.checkArgument(args.length >= 1, "usage: <jarfile>");
		String jarFile = args[0];

		Signature signature = Jars.signatureOfJar(jarFile);
		String signatureAsString = Signature2Text.toText(signature);
		
		System.out.println("------------------------------");
		System.out.println(signatureAsString);
		System.out.println("------------------------------");
	}

}
