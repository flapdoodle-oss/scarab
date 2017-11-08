package se.jbee.jvm.checks;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import se.jbee.jvm.Class;
import se.jbee.jvm.Classes;
import se.jbee.jvm.graph.ClassGraph;
import se.jbee.sample.checks.Escaper;
import se.jbee.sample.checks.PublicClass;

public class ClassVisibility {

	@Test
	public void publicClass() {
		ClassGraph result = Classes.read(PublicClass.class);
		Class clazz = result.classes.findOne(Classes.classWithName(PublicClass.class.getSimpleName())).get();
		assertTrue("public", clazz.isPublic());
		assertFalse("protected", clazz.isProtected());
		assertFalse("private", clazz.isPrivate());
		assertTrue("top level", clazz.isTopLevel());
	}

	@Test
	public void nonPublicClass() {
		ClassGraph result = Classes.read(Escaper.nonPublicClass());
		Class clazz = result.classes.findOne(Classes.classWithName("NonPublicClass")).get();
		assertFalse("public", clazz.isPublic());
		assertTrue("protected", clazz.isProtected());
		assertFalse("private", clazz.isPrivate());
		assertTrue("top level", clazz.isTopLevel());
	}
}
