package de.flapdoodle.unravel.signature;

import java.util.Comparator;

import org.immutables.value.Value.Immutable;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Ordering;

@Immutable
public interface VisibleClass extends CommonClass{
	Visibility visibility();
	ImmutableList<VisibleMethod> methods();
	ImmutableList<VisibleField> fields();
	
	public static ImmutableVisibleClass.Builder builder() {
		return ImmutableVisibleClass.builder();
	}
	
	public static Comparator<VisibleClass> defaultOrdering() {
		return Ordering.natural().onResultOf(VisibleClass::name);
	}
}
