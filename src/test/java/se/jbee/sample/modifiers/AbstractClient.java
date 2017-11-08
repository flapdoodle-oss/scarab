package se.jbee.sample.modifiers;

import java.util.List;

import com.google.common.collect.Lists;

public abstract class AbstractClient {
	
	private final Api api;

	private AbstractClient() {
		this.api=null;
	}
	
	protected AbstractClient(Api api) {
		this.api = api;
	}

	public AbstractClient(String name) {
		this.api = new Api() {
			@Override
			public List<String> allNames() {
				return Lists.newArrayList(name);
			}
		};
	}
	
	public void printAllNames() {
		for (String name : api.allNames()) {
			System.out.println(name);
		}
	}
}
