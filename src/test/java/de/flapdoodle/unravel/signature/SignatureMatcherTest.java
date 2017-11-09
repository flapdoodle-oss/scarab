package de.flapdoodle.unravel.signature;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import de.flapdoodle.unravel.signature.SignatureMatcher.Match;
import io.vavr.Tuple;

public class SignatureMatcherTest extends AbstractSignatureTest {

	@Test
	public void allOutgoingMethodsAreMatching() {
		ClassName fooClass = className("foo", "Foo");
		ClassName barClass = className("bar", "Bar");
		
		ImmutableSignature a = Signature.builder()
			.addVisibleClasses(visibleClass(fooClass,	visibleMethod(typeOf(String.class), "hello", typeOf(int.class))))
			.addUsedClasses(usedClass(barClass,	usedMethod(typeOf(String.class), "world")))
			.build();
		
		ImmutableSignature b = Signature.builder()
				.addVisibleClasses(visibleClass(barClass,	visibleMethod(typeOf(String.class), "world")))
				.build();
		
		Match match = SignatureMatcher.match(a, b);
		
		assertEquals("matching", 1, match.matching().size());
		assertEquals("matching", Tuple.of(barClass, barClass), match.matching().get().map1(UsedClass::name).map2(VisibleClass::name));
		assertTrue("no failed", match.failed().isEmpty());
		assertTrue("no duplicates", match.duplicateClasses().isEmpty());
	}
	
}
