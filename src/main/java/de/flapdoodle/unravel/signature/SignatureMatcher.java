package de.flapdoodle.unravel.signature;

import org.immutables.value.Value.Default;
import org.immutables.value.Value.Immutable;
import org.immutables.vavr.encodings.VavrEncodingEnabled;

import com.google.common.collect.ImmutableList;

import de.flapdoodle.unravel.signature.ImmutableMatch.Builder;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.collection.Multimap;
import io.vavr.collection.Set;
import io.vavr.control.Option;

public class SignatureMatcher {

	public static Match match(Signature a, Signature b) {
		Builder builder = Match.builder();

		builder.duplicateClasses(a.visibleClasses()
				.map(VisibleClass::name)
				.toSet()
				.intersect(b.visibleClasses()
						.map(VisibleClass::name)
						.toSet()));

		match(builder, a.usedClasses(), b.visibleClasses());
		match(builder, b.usedClasses(), a.visibleClasses());

		return builder.build();
	}

	private static void match(Builder builder, List<UsedClass> usedClasses, List<VisibleClass> visibleClasses) {
		Map<ClassName, VisibleClass> classMap = visibleClasses.toMap(VisibleClass::name, noop -> noop);

		usedClasses.forEach(usedClass -> {
			classMap.get(usedClass.name())
					.peek(clazz -> matchMethods(builder, usedClass, clazz))
					.onEmpty(() -> {
						builder.addUnused(usedClass);
					});
		});

	}

	private static void matchMethods(Builder builder, UsedClass usedClass, VisibleClass visibleClass) {
		Map<MethodKey, VisibleMethod> methodMap = visibleClass.methods()
				.toMap((VisibleMethod vm) -> MethodKey.of(vm.name(), vm.parameterTypes()), noop -> noop);

		List<Tuple2<UsedMethod,String>> mappingErrors = usedClass.methods()
			.flatMap(method -> mappingErrorOf(method, methodMap.get(MethodKey.of(method.name(), method.parameterTypes()))));
		
		if (!mappingErrors.isEmpty()) {
			mappingErrors.forEach(error -> {
				builder.putFailed(usedClass.name(), error);
			});
		} else {
			builder.putMatching(usedClass, visibleClass);
		}
	}

	private static Option<Tuple2<UsedMethod, String>> mappingErrorOf(UsedMethod method, Option<VisibleMethod> matchingMethod) {
		if (matchingMethod.isEmpty()) {
			return Option.of(Tuple.of(method,"not found"));
		}
		return matchingMethod.flatMap(usedMethod -> {
				if (false) {
					// TODO fix this
					if (usedMethod.isStatic() != method.isStatic()) {
						return Option.of(Tuple.of(method,"static missmatch"));
					}
				}
				if (!usedMethod.returnType().equals(method.returnType())) {
					return Option.of(Tuple.of(method,"return type missmatch"));
				}
				return Option.<Tuple2<UsedMethod,String>>none();
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

	@Immutable
	@VavrEncodingEnabled
	public static interface Match {
		Map<UsedClass, VisibleClass> matching();

		List<UsedClass> unused();

		Multimap<ClassName, Tuple2<UsedMethod,String>> failed();

		Set<ClassName> duplicateClasses();

		public static ImmutableMatch.Builder builder() {
			return ImmutableMatch.builder();
		}

		@Default
		default boolean noConflicts() {
			return duplicateClasses().isEmpty() && failed().isEmpty();
		}
	}
}
