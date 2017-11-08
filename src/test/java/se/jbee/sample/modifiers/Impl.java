package se.jbee.sample.modifiers;

import java.util.List;

import com.google.common.collect.ImmutableList;

class Impl implements Api {

	@Override
	public List<String> allNames() {
		return ImmutableList.of("Jan","Susi");
	}
}
