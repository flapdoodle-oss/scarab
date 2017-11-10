package se.jbee.sample.modifiers;

import com.google.common.collect.ImmutableSet;

// not part of this sample
public class Exporter {

	public static ImmutableSet<Class<?>> classSet() {
		return ImmutableSet.of(AbstractClient.class, Api.class, Impl.class, ImplFactory.class);
	}
}
