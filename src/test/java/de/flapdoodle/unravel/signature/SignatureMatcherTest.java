package de.flapdoodle.unravel.signature;

import org.junit.Test;

public class SignatureMatcherTest extends AbstractSignatureTest {

	@Test
	public void allOutgoingMethodsAreMatching() {
		
		ClassName fooClass = className("foo", "Foo");
		
		visibleClass(fooClass,	visibleMethod(typeOf(String.class), "hello", typeOf(int.class)));
		
		ImmutableSignature a = Signature.builder()
				.addVisibleClasses(visibleClass(fooClass,	visibleMethod(typeOf(String.class), "hello", typeOf(int.class))))
				.addUsedClasses(UsedClass.builder()
						.isArray(false)
						.name(ClassName.of("bar","Bar"))
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
						.name(ClassName.of("bar","Bar"))
						.addMethods(VisibleMethod.builder()
								.isStatic(false)
								.visibility(Visibility.Protected)
								.returnType(SimpleType.of("java.lang", "String"))
								.name("world")
								.build())
						.build())
				.build();
		
		SignatureMatcher.match(a, b);
	}
	
}
