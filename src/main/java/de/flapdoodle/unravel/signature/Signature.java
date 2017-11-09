package de.flapdoodle.unravel.signature;

import org.immutables.value.Value.Check;
import org.immutables.value.Value.Immutable;
import org.immutables.vavr.encodings.VavrEncodingEnabled;

import com.google.common.base.Preconditions;

import io.vavr.collection.List;
import io.vavr.collection.Map;

@Immutable
@VavrEncodingEnabled
public abstract class Signature {
	public abstract List<VisibleClass> visibleClasses();

	public abstract List<UsedClass> usedClasses();

	@Check
	protected void check() {
		Map<ClassName, List<VisibleClass>> classCollisions = List.ofAll(visibleClasses())
				.groupBy(VisibleClass::name)
				.filter((cn, v) -> v.length() > 1);
		
		Map<ClassName, List<UsedClass>> usedClassCollisions = List.ofAll(usedClasses())
				.groupBy(UsedClass::name)
				.filter((cn, v) -> v.length() > 1);
		
		Preconditions.checkArgument(classCollisions.isEmpty(), "class collisions: %s", classCollisions);
		Preconditions.checkArgument(usedClassCollisions.isEmpty(), "class collisions: %s", usedClassCollisions);
	}

	public static ImmutableSignature.Builder builder() {
		return ImmutableSignature.builder();
	}
}
