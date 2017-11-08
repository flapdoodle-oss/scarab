package de.flapdoodle.unravel.signature;

import org.immutables.value.Value.Immutable;

@Immutable
public interface VisibleField extends CommonField {
	Visibility visibility();

	public static ImmutableVisibleField.Builder builder() {
		return ImmutableVisibleField.builder();
	}

}
