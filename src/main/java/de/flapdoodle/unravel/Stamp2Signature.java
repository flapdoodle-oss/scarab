package de.flapdoodle.unravel;

import java.util.stream.Stream;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;

import de.flapdoodle.unravel.signature.ClassName;
import de.flapdoodle.unravel.signature.ImmutableSignature.Builder;
import de.flapdoodle.unravel.signature.Signature;
import de.flapdoodle.unravel.signature.SimpleType;
import de.flapdoodle.unravel.signature.UsedClass;
import de.flapdoodle.unravel.signature.UsedField;
import de.flapdoodle.unravel.signature.UsedMethod;
import de.flapdoodle.unravel.signature.Visibility;
import de.flapdoodle.unravel.signature.VisibleClass;
import de.flapdoodle.unravel.signature.VisibleField;
import de.flapdoodle.unravel.signature.VisibleMethod;
import io.vavr.collection.List;
import se.jbee.jvm.Class;
import se.jbee.jvm.Method;
import se.jbee.jvm.Modifiers;
import se.jbee.jvm.Parameter;
import se.jbee.jvm.graph.FieldNode;
import se.jbee.jvm.graph.MethodNode;

public class Stamp2Signature {

	public static Signature asSignature(Stamp stamp) {
		Builder builder = Signature.builder();

		Sets.union(stamp.visibleMethods().asMap().keySet(), stamp.visibleFields().asMap().keySet())
				.stream()
				.forEach(c -> {
					ImmutableCollection<MethodNode> methods = stamp.visibleMethods().get(c);
					ImmutableCollection<FieldNode> fields = stamp.visibleFields().get(c);
					builder.addVisibleClasses(asVisibleClass(c, methods, fields));
				});
		
		Sets.union(stamp.usedMethods().asMap().keySet(), stamp.usedFields().asMap().keySet())
			.stream()
			.filter(c -> !c.pkg().canonicalName().startsWith("java."))
			.forEach(c -> {
				ImmutableCollection<MethodNode> methods = stamp.usedMethods().get(c);
				ImmutableCollection<FieldNode> fields = stamp.usedFields().get(c);
				builder.addUsedClasses(asUsedClass(c, methods, fields));
			});

		return builder.build();
	}

	private static UsedClass asUsedClass(Class c, ImmutableCollection<MethodNode> methods, ImmutableCollection<FieldNode> fields) {
		return UsedClass.builder()
				.isArray(c.isArray())
				.name(ClassName.of(c.canonicalName()))
				.addAllMethods(asUsedMethods(methods))
				.addAllFields(asUsedFields(fields))
				.build();
	}

	private static Iterable<UsedField> asUsedFields(ImmutableCollection<FieldNode> fields) {
		return fields.stream().map(f -> asUsedField(f)).collect(ImmutableList.toImmutableList());
	}

	private static UsedField asUsedField(FieldNode f) {
		return UsedField.builder()
				.isStatic(!f.isInstanceField())
				.valueType(asType(f.type.id()))
				.name(f.id().name())
				.build();
	}

	private static VisibleClass asVisibleClass(Class c, ImmutableCollection<MethodNode> methods, ImmutableCollection<FieldNode> fields) {
		return VisibleClass.builder()
				.isArray(c.isArray())
				.visibility(visibilityOf(c))
				.name(ClassName.of(c.canonicalName()))
				.addAllMethods(asMethods(methods))
				.addAllFields(asFields(fields))
				.build();
	}

	private static Iterable<VisibleField> asFields(ImmutableCollection<FieldNode> fields) {
		return fields.stream().map(f -> asField(f)).collect(ImmutableList.toImmutableList());
	}

	private static VisibleField asField(FieldNode f) {
		return VisibleField.builder()
				.isStatic(!f.isInstanceField())
				.visibility(visibilityOf(f.id().modifiers()))
				.valueType(asType(f.type.id()))
				.name(f.id().name())
				.build();
	}

	private static Iterable<VisibleMethod> asMethods(ImmutableCollection<MethodNode> methods) {
		return methods.stream().map(m -> asMethod(m)).collect(ImmutableList.toImmutableList());
	}

	private static Iterable<UsedMethod> asUsedMethods(ImmutableCollection<MethodNode> methods) {
		return methods.stream().map(m -> asUsedMethod(m)).collect(List.collector());
	}

	private static VisibleMethod asMethod(MethodNode m) {
		return VisibleMethod.builder()
				.isStatic(m.isStaticMethod())
				.visibility(visibilityOf(m.id()))
				.returnType(asType(m.id().returnType))
				.name(m.id().name())
				.addAllParameterTypes(asTypes(m.id().parameters()))
				// TODO: throwing not implemented
				.build();
	}

	private static Visibility visibilityOf(Class c) {
		if (c.isPackageProtected()) return Visibility.PackageProtected;
		if (c.isPublic()) return Visibility.Public;
		if (c.isProtected()) return Visibility.Protected;
		if (c.isPrivate()) return Visibility.Private;
		
		throw new IllegalArgumentException("mapping failed: "+c);
	}


	private static Visibility visibilityOf(Method id) {
		if (id.isPackageProtected()) return Visibility.PackageProtected;
		if (id.isPublic()) return Visibility.Public;
		if (id.isProtected()) return Visibility.Protected;
		if (id.isPrivate()) return Visibility.Private;
		
		throw new IllegalArgumentException("mapping failed: "+id);
	}

	private static UsedMethod asUsedMethod(MethodNode m) {
		return UsedMethod.builder()
				.isStatic(m.isStaticMethod())
				.returnType(asType(m.id().returnType))
				.name(m.id().name())
				.addAllParameterTypes(asTypes(m.id().parameters()))
				// TODO: throwing not implemented
				.build();
	}

	private static Iterable<SimpleType> asTypes(Parameter[] parameters) {
		return Stream.of(parameters).map(p -> asType(p)).collect(ImmutableList.toImmutableList());
	}

	private static SimpleType asType(Parameter p) {
		return SimpleType.of(p.type().pkg().canonicalName(), p.type().simpleName());
	}

	private static SimpleType asType(Class c) {
		return SimpleType.of(c.pkg().canonicalName(), c.simpleName());
	}

	@Deprecated
	private static Visibility visibilityOf(Modifiers modifiers) {
		return modifiers.isProtected()
				? Visibility.Protected
				: Visibility.Public;
	}
}
