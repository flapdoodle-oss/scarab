package de.flapdoodle.unravel.signature;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Ignore;
import org.junit.Test;

import de.flapdoodle.unravel.signature.SignatureMatcher.Match;
import de.flapdoodle.unravel.signature.SignatureMatcher.MismatchType;
import io.vavr.Tuple;
import io.vavr.collection.HashMap;
import io.vavr.collection.List;

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
	
	@Test
	public void missingMethod() {
		ClassName fooClass = className("foo", "Foo");
		ClassName barClass = className("bar", "Bar");
		
		UsedMethod missingMethod = usedMethod(typeOf(String.class), "world", typeOf(int.class));
		
		ImmutableSignature a = Signature.builder()
			.addVisibleClasses(visibleClass(fooClass,	visibleMethod(typeOf(String.class), "hello", typeOf(int.class))))
			.addUsedClasses(usedClass(barClass,	usedMethod(typeOf(String.class), "world"), missingMethod))
			.build();
		
		ImmutableSignature b = Signature.builder()
				.addVisibleClasses(visibleClass(barClass,	visibleMethod(typeOf(String.class), "world")))
				.build();
		
		Match match = SignatureMatcher.match(a, b);
		
		assertEquals("matching", 0, match.matching().size());
		assertEquals("failed", 1, match.failed().size());
//		assertEquals("failed", Tuple.of(barClass, HashMap.of(missingMethod, List.of(MismatchType.NOT_FOUND))), match.failed().get());
		assertTrue("no duplicates", match.duplicateClasses().isEmpty());
	}

	@Test
	public void returnTypeMismatch() {
		ClassName fooClass = className("foo", "Foo");
		ClassName barClass = className("bar", "Bar");
		
		UsedMethod call = usedMethod(typeOf(int.class), "world");
		
		ImmutableSignature a = Signature.builder()
			.addVisibleClasses(visibleClass(fooClass,	visibleMethod(typeOf(String.class), "hello", typeOf(int.class))))
			.addUsedClasses(usedClass(barClass,	call))
			.build();
		
		ImmutableSignature b = Signature.builder()
				.addVisibleClasses(visibleClass(barClass,	visibleMethod(typeOf(String.class), "world")))
				.build();
		
		Match match = SignatureMatcher.match(a, b);
		
		assertEquals("matching", 0, match.matching().size());
		assertEquals("failed", 1, match.failed().size());
//		assertEquals("failed", Tuple.of(barClass, Mismatch.builder(). HashMap.of(call, List.of(MismatchType.RETURN_TYPE))), match.failed().get());
		assertTrue("no duplicates", match.duplicateClasses().isEmpty());
	}

	@Test
	@Ignore
	public void staticMismatch() {
		ClassName fooClass = className("foo", "Foo");
		ClassName barClass = className("bar", "Bar");
		
		UsedMethod call = usedMethod(true, typeOf(String.class), "world");
		
		ImmutableSignature a = Signature.builder()
			.addVisibleClasses(visibleClass(fooClass,	visibleMethod(typeOf(String.class), "hello", typeOf(int.class))))
			.addUsedClasses(usedClass(barClass,	call))
			.build();
		
		ImmutableSignature b = Signature.builder()
				.addVisibleClasses(visibleClass(barClass,	visibleMethod(typeOf(String.class), "world")))
				.build();
		
		Match match = SignatureMatcher.match(a, b);
		
		assertEquals("matching", 0, match.matching().size());
		assertEquals("failed", 1, match.failed().size());
		assertEquals("failed", Tuple.of(barClass, HashMap.of(call, List.of(MismatchType.STATIC_NON_STATIC))), match.failed().get());
		assertTrue("no duplicates", match.duplicateClasses().isEmpty());
	}
}
