package de.flapdoodle.unravel.signature;

import java.util.Comparator;

import org.immutables.value.Value.Immutable;
import org.immutables.vavr.encodings.VavrEncodingEnabled;

import com.google.common.collect.Ordering;

import io.vavr.collection.List;

@Immutable
@VavrEncodingEnabled
public interface VisibleClass extends CommonClass{
	Visibility visibility();
	List<VisibleMethod> methods();
	List<VisibleField> fields();
	
	public static ImmutableVisibleClass.Builder builder() {
		return ImmutableVisibleClass.builder();
	}
	
	public static Comparator<VisibleClass> defaultOrdering() {
		return Ordering.natural().onResultOf(VisibleClass::name);
	}
}
