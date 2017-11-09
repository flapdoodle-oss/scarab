package de.flapdoodle.unravel.io;

import java.io.FileInputStream;

import de.flapdoodle.types.ThrowingSupplier;
import de.flapdoodle.types.Try;
import de.flapdoodle.unravel.ScarabJarStamper;
import de.flapdoodle.unravel.Stamp2Signature;
import de.flapdoodle.unravel.signature.Signature;

public class Jars {

	public static Signature signatureOfJar(String jarFile) {
		ThrowingSupplier<FileInputStream, RuntimeException> jarFileSupplier = Try.supplier(() -> new FileInputStream(jarFile)).mapCheckedException(RuntimeException::new);
		Signature signature = Stamp2Signature.asSignature(new ScarabJarStamper().stamp(jarFileSupplier::get));
		return signature;
	}

}
