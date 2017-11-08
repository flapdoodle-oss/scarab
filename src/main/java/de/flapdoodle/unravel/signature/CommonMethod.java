package de.flapdoodle.unravel.signature;

import java.util.Comparator;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Ordering;

public interface CommonMethod {
	boolean isStatic();
	SimpleType returnType();
	String name();
	ImmutableList<SimpleType> parameterTypes();
	ImmutableList<SimpleType> throwing();

	public static Comparator<? super CommonMethod> defaultOrdering() {
		return Ordering.natural().onResultOf(CommonMethod::name)
				.thenComparing(Ordering.natural().onResultOf((CommonMethod vm) -> vm.parameterTypes().size()))
				.thenComparing(Ordering.natural().onResultOf((CommonMethod vm) -> vm.parameterTypes().toString()));
	}

}
