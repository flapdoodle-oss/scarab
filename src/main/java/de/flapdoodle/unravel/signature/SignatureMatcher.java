package de.flapdoodle.unravel.signature;

import org.immutables.value.Value.Immutable;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

public class SignatureMatcher {

	public static void match(Signature a, Signature b) {
		match(a.usedClasses(), b.visibleClasses());
		match(b.usedClasses(), a.visibleClasses());
	}

	private static void match(ImmutableList<UsedClass> usedClasses, ImmutableList<VisibleClass> visibleClasses) {
		ImmutableMap<ClassName, VisibleClass> classMap = visibleClasses.stream()
			.collect(ImmutableMap.toImmutableMap(VisibleClass::name, noop -> noop));
		
		usedClasses.forEach(usedClass -> {
			VisibleClass clazz = classMap.get(usedClass.name());
			if (clazz!=null) {
				matchMethods(usedClass.methods(), clazz.methods());
			}
		});
	}

	private static void matchMethods(ImmutableList<UsedMethod> usedMethods, ImmutableList<VisibleMethod> providedMethods) {
		ImmutableMap<MethodKey, VisibleMethod> methodMap = providedMethods.stream()
			.collect(ImmutableMap.toImmutableMap((VisibleMethod vm) -> MethodKey.of(vm.name(), vm.parameterTypes()), noop -> noop));
		
		usedMethods.forEach(method -> {
			VisibleMethod usedMethod = methodMap.get(MethodKey.of(method.name(), method.parameterTypes()));
			if (usedMethod!=null) {
				if (usedMethod.isStatic()!=method.isStatic()) {
					System.out.println("static does not match: "+method+" - "+usedMethod);
				}
				if (!usedMethod.returnType().equals(method.returnType())) {
					System.out.println("return type not match: "+method+" - "+usedMethod);
				}
			} else {
				System.out.println("Method missing: "+method);
			}
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
