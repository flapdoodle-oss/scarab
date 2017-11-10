package se.jbee.jvm.checks;

import static se.jbee.jvm.Check.assertThat;

import java.util.function.Predicate;

import org.junit.Test;

import se.jbee.jvm.Check;
import se.jbee.jvm.Class;
import se.jbee.jvm.Classes;
import se.jbee.jvm.graph.ClassGraph;
import se.jbee.sample.checks.Escaper;
import se.jbee.sample.checks.InnerClasses;
import se.jbee.sample.checks.PublicClass;
import se.jbee.sample.checks.PublicInterface;

public class ClassVisibility {

	@Test
	public void publicClass() {
		ClassGraph result = Classes.read(PublicClass.class);
		Class clazz = result.classes.findOne(Classes.classWithName(PublicClass.class.getSimpleName())).get();
		assertThat(clazz, isTopLevel(), isPublic(), isProtected().negate(), isPrivate().negate());
	}

	@Test
	public void nonPublicClass() {
		ClassGraph result = Classes.read(Escaper.nonPublicClass());
		Class clazz = result.classes.findOne(Classes.classWithName("NonPublicClass")).get();
		assertThat(clazz, isTopLevel(), isProtected(), isPublic().negate(), isPrivate().negate());
	}

	@Test
	public void publicInterface() {
		ClassGraph result = Classes.read(PublicInterface.class);
		Class clazz = result.classes.findOne(Classes.classWithName(PublicInterface.class.getSimpleName())).get();
		assertThat(clazz, isTopLevel(), isPublic(), isProtected().negate(), isPrivate().negate());
	}

	@Test
	public void nonPublicInterface() {
		ClassGraph result = Classes.read(Escaper.nonPublicInterface());
		Class clazz = result.classes.findOne(Classes.classWithName("NonPublicInterface")).get();
		assertThat(clazz, isTopLevel(), isProtected(), isPublic().negate(), isPrivate().negate());
	}

	@Test
	public void innerClasses() {
		ClassGraph result = Classes.read(InnerClasses.class);

		Class clazz = result.classes.findOne(Classes.classWithName(InnerClasses.PublicStaticInnerClass.class.getSimpleName())).get();
		assertThat(clazz, isStatic(), isInner(), isPublic(), isProtected().negate(), isPrivate().negate());
	}

	private static Predicate<Class> isStatic() {
		return Check.check("isStatic", (Class c) -> c.modifiers.isStatic());
	}
	
	private static Predicate<Class> isPublic() {
		return Check.check("isPublic", Class::isPublic);
	}

	private static Predicate<Class> isPrivate() {
		return Check.check("isPrivate", Class::isPrivate);
	}

	private static Predicate<Class> isProtected() {
		return Check.check("isProtected", Class::isProtected);
	}

	private static Predicate<Class> isPackageProtected() {
		return Check.check("isPackageProtected", Class::isPackageProtected);
	}

	private static Predicate<Class> isTopLevel() {
		return Check.check("isTopLevel", Class::isTopLevel);
	}

	private static Predicate<Class> isInner() {
		return Check.check("isInner", Class::isInner);
	}

	private static Predicate<Class> isAnonymous() {
		return Check.check("isTopLevel", Class::isAnonymous);
	}
}
