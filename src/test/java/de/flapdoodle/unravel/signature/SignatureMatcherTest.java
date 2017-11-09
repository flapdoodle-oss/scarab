package de.flapdoodle.unravel.signature;

import org.junit.Test;

public class SignatureMatcherTest {

	@Test
	public void allOutgoingMethodsAreMatching() {
		ImmutableSignature a = Signature.builder()
				.addVisibleClasses(VisibleClass.builder()
						.isArray(false)
						.visibility(Visibility.Public)
						.name("foo","Foo")
						.addMethods(VisibleMethod.builder()
								.isStatic(false)
								.visibility(Visibility.Protected)
								.returnType(SimpleType.of("java.lang", "String"))
								.name("hello")
								.addParameterTypes(SimpleType.of("", "int"))
								.build())
						.build())
				.addUsedClasses(UsedClass.builder()
						.isArray(false)
						.name("bar","Bar")
						.addMethods(UsedMethod.builder()
								.isStatic(false)
								.returnType(SimpleType.of("java.lang", "String"))
								.name("world")
								.build())
						.build())
				.build();

		ImmutableSignature b = Signature.builder()
				.addVisibleClasses(VisibleClass.builder()
						.isArray(false)
						.visibility(Visibility.Public)
						.name("bar","Bar")
						.addMethods(VisibleMethod.builder()
								.isStatic(false)
								.visibility(Visibility.Protected)
								.returnType(SimpleType.of("java.lang", "String"))
								.name("world")
								.addParameterTypes()
								.build())
						.build())
				.build();
		
		SignatureMatcher.match(a, b);
	}
}
