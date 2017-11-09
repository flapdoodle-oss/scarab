package de.flapdoodle.unravel.signature;

import org.immutables.value.Value.Default;
import org.immutables.value.Value.Immutable;
import org.immutables.value.Value.Parameter;

@Immutable
public interface SimpleType extends CommonClass {
	@Override
	@Parameter
	ClassName name();
	
	@Override
	@Default
	default boolean isArray() {
		return false;
	}
	
	public static SimpleType of(String packageName, String name) {
		return ImmutableSimpleType.of(ClassName.of(packageName, name));
	}
	
	public static SimpleType arrayOf(String packageName, String name) {
		return ImmutableSimpleType.of(ClassName.of(packageName, name)).withIsArray(true);
	}
}
