package de.flapdoodle.unravel.signature;

import org.immutables.value.Value.Immutable;
import org.immutables.vavr.encodings.VavrEncodingEnabled;

@Immutable
@VavrEncodingEnabled
public interface UsedMethod extends CommonMethod {
	
	public static ImmutableUsedMethod.Builder builder() {
		return ImmutableUsedMethod.builder();
	}

}
