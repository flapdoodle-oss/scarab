package se.jbee.jvm;

import java.util.function.Predicate;

public abstract class Methods {

	public static Predicate<Method> returnType(String name) {
		return Check.check("returnType is "+name, (Method m) -> m.returnType.canonicalName().equals(name));
	}
	
	public static Predicate<Method> isStatic() {
		return Check.check("isStatic", (Method m) -> m.modifiers().isStatic());
	}
	
	public static Predicate<Method> isConstructor() {
		return Check.check("isConstructor", (Method m) -> m.isConstructor());
	}

	public static Predicate<Method> isStaticInitialization() {
		return Check.check("isStaticInitialization", (Method m) -> m.isStaticInitialization());
	}

	public static Predicate<Method> isProtected() {
		return Check.check("isProtected", (Method m) -> m.isProtected());
	}

	public static Predicate<Method> isPublic() {
		return Check.check("isPublic", (Method m) -> m.isPublic());
	}

	public static Predicate<Method> isPrivate() {
		return Check.check("isPrivate", (Method m) -> m.isPrivate());
	}

	public static Predicate<Method> isPackageProtected() {
		return Check.check("isPackageProtected", (Method m) -> m.isPackageProtected());
	}

}
