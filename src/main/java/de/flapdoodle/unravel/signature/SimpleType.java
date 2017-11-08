package de.flapdoodle.unravel.signature;

import org.immutables.value.Value.Immutable;
import org.immutables.value.Value.Parameter;

@Immutable
public interface SimpleType extends CommonClass {
	@Override
	@Parameter
	String packageName();

	@Override
	@Parameter
	String name();
	
	public static SimpleType of(String packageName, String name) {
		return ImmutableSimpleType.of(packageName, name);
	}
}
