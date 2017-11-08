package de.flapdoodle.unravel.signature;

import org.immutables.value.Value.Immutable;

import com.google.common.collect.ImmutableList;

@Immutable
public interface Signature {
	ImmutableList<VisibleClass> visibleClasses();
	ImmutableList<UsedClass> usedClasses();
	
	public static ImmutableSignature.Builder builder() {
		return ImmutableSignature.builder();
	}
}
