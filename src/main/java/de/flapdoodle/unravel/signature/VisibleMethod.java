package de.flapdoodle.unravel.signature;

import org.immutables.value.Value.Immutable;

@Immutable
public interface VisibleMethod extends CommonMethod {
	Visibility visibility();
	
	public static ImmutableVisibleMethod.Builder builder() {
		return ImmutableVisibleMethod.builder();
	}

}
