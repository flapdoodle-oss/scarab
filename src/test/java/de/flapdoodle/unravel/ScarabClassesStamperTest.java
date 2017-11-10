package de.flapdoodle.unravel;

import org.junit.Ignore;
import org.junit.Test;

import de.flapdoodle.unravel.signature.Signature2Text;
import se.jbee.jvm.Classes;
import se.jbee.sample.egdecases.PrimitivyTypes;
import se.jbee.sample.modifiers.AbstractClient;
import se.jbee.sample.oneTwoThree.One;

public class ScarabClassesStamperTest {

	@Test
	@Ignore
	public void oneTwoThreeSample() {
		Stamp stamp = new ScarabClassesStamper()
				.stampOf(Classes.byteCodeOfClasses(One.class.getPackage()));
		
		signatureOf(stamp);
	}


	@Test
	@Ignore
	public void modifiersSample() {
		Stamp stamp = new ScarabClassesStamper()
			.stampOf(Classes.byteCodeOfClasses(AbstractClient.class.getPackage()));
		
//		stamp.visibleMethods().forEach((c,m) -> System.out.println(c+" "+m));
//		
//		assertEquals(2, stamp.visibleMethods().size());
		
		signatureOf(stamp);
	}
	
	@Test
	@Ignore
	public void primitives() {
		Stamp stamp = new ScarabClassesStamper()
				.stampOf(Classes.byteCodeOfClasses(PrimitivyTypes.class.getPackage()));
		
		signatureOf(stamp);
	}

	private static void signatureOf(Stamp stamp) {
		String asText = Signature2Text.toText(Stamp2Signature.asSignature(stamp));
		System.out.println("------------------------");
		System.out.println(asText);
		System.out.println("------------------------");
	}
}
