package de.flapdoodle.unravel.signature;

import java.util.Comparator;

import org.immutables.value.Value.Immutable;
import org.immutables.value.Value.Style;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Ordering;

@Immutable
@Style(deepImmutablesDetection=true)
public interface UsedClass extends CommonClass {
	
	ImmutableList<UsedMethod> methods();
	ImmutableList<UsedField> fields();
	
	public static ImmutableUsedClass.Builder builder() {
		return ImmutableUsedClass.builder();
	}

	public static Comparator<UsedClass> defaultOrdering() {
		return Ordering.natural().onResultOf(UsedClass::name);
	}

}
