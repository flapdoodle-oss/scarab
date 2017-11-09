package de.flapdoodle.unravel.signature;

import org.immutables.value.Value.Immutable;
import org.immutables.vavr.encodings.VavrEncodingEnabled;

@Immutable
@VavrEncodingEnabled
public interface VisibleMethod extends CommonMethod {
	Visibility visibility();
	
	public static ImmutableVisibleMethod.Builder builder() {
		return ImmutableVisibleMethod.builder();
	}

}
