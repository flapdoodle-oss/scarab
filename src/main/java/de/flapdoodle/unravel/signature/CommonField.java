package de.flapdoodle.unravel.signature;

import java.util.Comparator;

import com.google.common.collect.Ordering;

public interface CommonField {
	boolean isStatic();
	SimpleType valueType();
	String name();

	public static Comparator<? super CommonField> defaultOrdering() {
		return Ordering.natural().onResultOf(CommonField::name);
	}

}
