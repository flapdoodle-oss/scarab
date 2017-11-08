package de.flapdoodle.unravel;

import java.io.IOException;
import java.io.InputStream;
import java.util.function.Supplier;
import java.util.stream.Stream;

import org.junit.Ignore;
import org.junit.Test;

import com.google.common.collect.ImmutableList;
import com.google.common.io.Resources;

import de.flapdoodle.unravel.signature.Signature2Text;

public class ScarabClassesStamperTest {

	@Test
	@Ignore
	public void oneTwoThreeSample() {
		ImmutableList<Supplier<InputStream>> sampleClasses = Stream.of("One","Two","Three","Hidden")
			.map(c -> streamOf("packages/sample/one-two-three/"+c+".class"))
			.collect(ImmutableList.toImmutableList());
		
		Stamp stamp = new ScarabClassesStamper().stampOf(sampleClasses);
		
		signatureOf(stamp);
	}
	
	@Test
	public void modifiersSample() {
		ImmutableList<Supplier<InputStream>> sampleClasses = Stream.of("Api","Impl","ImplFactory","AbstractClient","AbstractClient$1")
			.map(c -> streamOf("packages/sample/modifiers/"+c+".class"))
			.collect(ImmutableList.toImmutableList());
		
		Stamp stamp = new ScarabClassesStamper().stampOf(sampleClasses);
		
		signatureOf(stamp);
	}
	
	private static Supplier<InputStream> streamOf(String resourceName) {
		return () -> {
			try {
				return Resources.getResource(resourceName).openStream();
			}
			catch (IOException e) {
				throw new RuntimeException("could not open "+resourceName);
			}
		};
	}
	
	private static void signatureOf(Stamp stamp) {
		String asText = Signature2Text.toText(Stamp2Signature.asSignature(stamp));
		System.out.println("------------------------");
		System.out.println(asText);
		System.out.println("------------------------");
	}
}
