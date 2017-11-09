package de.flapdoodle.unravel.signature;

import org.immutables.value.Value.Immutable;
import org.immutables.value.Value.Parameter;

@Immutable
public interface ClassName extends Comparable<ClassName> {
	@Parameter
	String packageName();

	@Parameter
	String name();

	@Override
	default int compareTo(ClassName o) {
		int packageCompare = packageName().compareTo(o.packageName());
		return packageCompare == 0
				? name().compareTo(o.name())
				: packageCompare;
	}

	public static ClassName of(String packageName, String name) {
		return ImmutableClassName.of(packageName, name);
	}
	
	public static ClassName of(String canonicalName) {
		int idx=canonicalName.lastIndexOf('.');
		if (idx!=-1) {
			return of(canonicalName.substring(0, idx), canonicalName.substring(idx+1));
		}
		return of("", canonicalName);
	}
}
