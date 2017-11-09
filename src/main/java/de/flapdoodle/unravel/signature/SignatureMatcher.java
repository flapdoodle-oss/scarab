package de.flapdoodle.unravel.signature;

import org.immutables.value.Value.Immutable;

import com.google.common.collect.ImmutableList;

import io.vavr.collection.List;
import io.vavr.collection.Map;

public class SignatureMatcher {

	public static void match(Signature a, Signature b) {
		match(a.usedClasses(), b.visibleClasses());
		match(b.usedClasses(), a.visibleClasses());
	}

	private static void match(List<UsedClass> usedClasses, List<VisibleClass> visibleClasses) {
		Map<ClassName, VisibleClass> classMap = visibleClasses.toMap(VisibleClass::name, noop -> noop);

		usedClasses.forEach(usedClass -> {
			classMap.get(usedClass.name())
					.peek(clazz -> matchMethods(usedClass.methods(), clazz.methods()));
		});
	}

	private static void matchMethods(List<UsedMethod> usedMethods, List<VisibleMethod> providedMethods) {
		Map<MethodKey, VisibleMethod> methodMap = providedMethods.toMap((VisibleMethod vm) -> MethodKey.of(vm.name(), vm.parameterTypes()), noop -> noop);

		usedMethods.forEach(method -> {
			methodMap.get(MethodKey.of(method.name(), method.parameterTypes()))
					.onEmpty(() -> System.out.println("Method missing: " + method))
					.peek(usedMethod -> {
						if (usedMethod.isStatic() != method.isStatic()) {
							System.out.println("static does not match: " + method + " - " + usedMethod);
						}
						if (!usedMethod.returnType().equals(method.returnType())) {
							System.out.println("return type not match: " + method + " - " + usedMethod);
						}
					});
		});
	}

	@Immutable
	static interface MethodKey {
		String name();

		ImmutableList<SimpleType> parameterTypes();

		public static MethodKey of(String name, Iterable<? extends SimpleType> parameterTypes) {
			return ImmutableMethodKey.builder()
					.name(name)
					.addAllParameterTypes(parameterTypes)
					.build();
		}
	}
}
