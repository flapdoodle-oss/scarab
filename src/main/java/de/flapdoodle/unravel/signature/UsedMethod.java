package de.flapdoodle.unravel.signature;

import org.immutables.value.Value.Immutable;

@Immutable
public interface UsedMethod extends CommonMethod {
	
	public static ImmutableUsedMethod.Builder builder() {
		return ImmutableUsedMethod.builder();
	}

}
