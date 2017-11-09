package de.flapdoodle.unravel.signature;

import java.util.Comparator;

import org.immutables.value.Value.Immutable;
import org.immutables.vavr.encodings.VavrEncodingEnabled;

import com.google.common.collect.Ordering;

import io.vavr.collection.List;

@Immutable
@VavrEncodingEnabled
public interface UsedClass extends CommonClass {
	
	List<UsedMethod> methods();
	List<UsedField> fields();
	
	public static ImmutableUsedClass.Builder builder() {
		return ImmutableUsedClass.builder();
	}

	public static Comparator<UsedClass> defaultOrdering() {
		return Ordering.natural().onResultOf(UsedClass::name);
	}

}
