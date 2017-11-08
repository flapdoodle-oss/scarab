package de.flapdoodle.unravel;

import java.io.FileInputStream;

import com.google.common.base.Preconditions;

import de.flapdoodle.types.ThrowingSupplier;
import de.flapdoodle.types.Try;
import de.flapdoodle.unravel.signature.Signature2Text;

public class ScarabJarSignature {

	public static void main(String[] args) {
		Preconditions.checkArgument(args.length >= 1, "usage: <jarfile>");
		String jarFile = args[0];

		ThrowingSupplier<FileInputStream, RuntimeException> jarFileSupplier = Try.supplier(() -> new FileInputStream(jarFile)).mapCheckedException(RuntimeException::new);
		
		String signature = Signature2Text.toText(Stamp2Signature.asSignature(new ScarabJarStamper().stamp(jarFileSupplier::get)));
		
		System.out.println("------------------------------");
		System.out.println(signature);
		System.out.println("------------------------------");
	}
}
