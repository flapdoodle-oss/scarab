package de.flapdoodle.unravel;

import com.google.common.base.Preconditions;

import de.flapdoodle.unravel.io.Jars;
import de.flapdoodle.unravel.signature.ClassName;
import de.flapdoodle.unravel.signature.Signature;
import de.flapdoodle.unravel.signature.SignatureMatcher;
import de.flapdoodle.unravel.signature.SignatureMatcher.Match;
import de.flapdoodle.unravel.signature.SimpleType;
import de.flapdoodle.unravel.signature.UsedClass;
import de.flapdoodle.unravel.signature.UsedMethod;

public class ScarabJarMatcher {

	public static void main(String[] args) {
		Preconditions.checkArgument(args.length >= 2, "usage: <jarfile> <jarFile2>");
		String jarFileA = args[0];
		String jarFileB = args[1];

		Signature signatureA = Jars.signatureOfJar(jarFileA);
		Signature signatureB = Jars.signatureOfJar(jarFileB);
		
		System.out.println(""+jarFileA+" <-> "+jarFileB);
		
		Match match = SignatureMatcher.match(signatureA, signatureB);
		if (match.noConflicts()) {
			System.out.println("no conflicts");
			System.out.println("-----------------------------");
			if (!match.matching().isEmpty()) {
				System.out.println("resolved types:");
				match.matching().forEach((s,d) -> {
					System.out.println(asString(s));
				});
			}
		} else {
			System.out.println("conflicts");
			System.out.println("-----------------------------");
			System.out.println("errors:");
			match.failed().toJavaMap().forEach((className, errors) -> {
				System.out.println(asString(className));
				errors.forEach(methodAndError -> {
					System.out.println("  "+asString(methodAndError._1())+" -> "+methodAndError._2());
				});
			});
			if (!match.duplicateClasses().isEmpty()) {
				System.out.println("- - - - - - - - - - - - - - - -");
				System.out.println("duplicated classes:");
				match.duplicateClasses().forEach(className -> {
					System.out.println(asString(className));
				});
			}
			if (!match.matching().isEmpty()) {
				System.out.println("- - - - - - - - - - - - - - - -");
				System.out.println("resolved types:");
				match.matching().forEach((s,d) -> {
					System.out.println(asString(s));
				});
			}
		}
	}

	private static String asString(UsedMethod method) {
		return asString(method.returnType())+" "+method.name()+"("+method.parameterTypes().map(s -> asString(s)).fold("", (a,b) -> a+","+b)+")";
	}

	private static String asString(UsedClass s) {
		return asString(s.name());
	}

	private static String asString(SimpleType s) {
		if (s.isArray()) {
			return asString(s.name())+"[]";
		}
		return asString(s.name());
	}

	private static String asString(ClassName className) {
		return className.packageName()+"."+className.name();
	}

}
