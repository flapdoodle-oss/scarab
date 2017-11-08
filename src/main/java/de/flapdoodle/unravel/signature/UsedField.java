package de.flapdoodle.unravel.signature;

import org.immutables.value.Value.Immutable;

@Immutable
public interface UsedField extends CommonField {

	public static ImmutableUsedField.Builder builder() {
		return ImmutableUsedField.builder();
	}

}
