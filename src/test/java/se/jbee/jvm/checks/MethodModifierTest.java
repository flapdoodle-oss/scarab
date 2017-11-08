package se.jbee.jvm.checks;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import se.jbee.jvm.Classes;
import se.jbee.jvm.graph.ClassGraph;
import se.jbee.sample.checks.MethodModifiers;

public class MethodModifierTest {

	@Test
	public void methods() {
		ClassGraph result = Classes.read(MethodModifiers.class);
		assertEquals(18, result.methods.count());
		// TODO more checks
	}
}
