package se.jbee.jvm.checks;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static se.jbee.jvm.Check.assertThat;
import static se.jbee.jvm.Methods.isConstructor;
import static se.jbee.jvm.Methods.isPackageProtected;
import static se.jbee.jvm.Methods.isPrivate;
import static se.jbee.jvm.Methods.isProtected;
import static se.jbee.jvm.Methods.isPublic;
import static se.jbee.jvm.Methods.isStatic;
import static se.jbee.jvm.Methods.returnType;

import java.util.Optional;

import org.junit.Test;

import com.google.common.base.Preconditions;

import se.jbee.jvm.Classes;
import se.jbee.jvm.Method;
import se.jbee.jvm.graph.ClassGraph;
import se.jbee.jvm.graph.Edges;
import se.jbee.jvm.graph.MethodNode;
import se.jbee.sample.checks.MethodModifiers;

public class MethodModifierTest {

	@Test
	public void methods() {
		ClassGraph result = Classes.read(MethodModifiers.class);
		assertEquals(18, result.methods.count());
		
		Optional<Method> initMethod = result.methods.findOne(m -> m.name().equals("<init>") && m.declaringClass().canonicalName().equals(MethodModifiers.class.getCanonicalName()));
		assertTrue(initMethod.isPresent());
		assertThat(initMethod.get(), isPublic(), isConstructor(), returnType("void"));
		
		assertThat(byName(result.methods, "publicStaticVoid"), isStatic(), isPublic(), returnType("void"));
		assertThat(byName(result.methods, "protectedStaticVoid"), isStatic(), isProtected(), returnType("void"));
		assertThat(byName(result.methods, "packageProtectedStaticVoid"), isStatic(), isPackageProtected(), returnType("void"));
		assertThat(byName(result.methods, "privateStaticVoid"), isStatic(), isPrivate(), returnType("void"));
		
		assertThat(byName(result.methods, "publicVoid"), isStatic().negate(), isPublic(), returnType("void"));
		assertThat(byName(result.methods, "protectedVoid"), isStatic().negate(), isProtected(), returnType("void"));
		assertThat(byName(result.methods, "packageProtectedVoid"), isStatic().negate(), isPackageProtected(), returnType("void"));
		assertThat(byName(result.methods, "privateVoid"), isStatic().negate(), isPrivate(), returnType("void"));

		assertThat(byName(result.methods, "publicStaticInt"), isStatic(), isPublic(), returnType("int"));
		assertThat(byName(result.methods, "protectedStaticInt"), isStatic(), isProtected(), returnType("int"));
		assertThat(byName(result.methods, "packageProtectedStaticInt"), isStatic(), isPackageProtected(), returnType("int"));
		assertThat(byName(result.methods, "privateStaticInt"), isStatic(), isPrivate(), returnType("int"));
		
		assertThat(byName(result.methods, "publicInt"), isStatic().negate(), isPublic(), returnType("int"));
		assertThat(byName(result.methods, "protectedInt"), isStatic().negate(), isProtected(), returnType("int"));
		assertThat(byName(result.methods, "packageProtectedInt"), isStatic().negate(), isPackageProtected(), returnType("int"));
		assertThat(byName(result.methods, "privateInt"), isStatic().negate(), isPrivate(), returnType("int"));
}
	
	private static Method byName(Edges<Method,MethodNode> methods, String name) {
		return Preconditions.checkNotNull(methods.findOne(m -> m.name().equals(name)).orElse(null),"method not found: %s",name);
	}
}
