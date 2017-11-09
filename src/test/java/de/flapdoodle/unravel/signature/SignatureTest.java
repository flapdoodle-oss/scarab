package de.flapdoodle.unravel.signature;

import org.junit.Test;

public class SignatureTest extends AbstractSignatureTest {

	@Test(expected=IllegalArgumentException.class)
	public void aClassMoreThanOnceShouldGiveAnError() {
		Signature.builder()
				.addVisibleClasses(visibleClass(className("foo", "Bar")))
				.addVisibleClasses(visibleClass(className("foo", "Bar")))
				.build();
	}
}
