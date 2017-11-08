package se.jbee.sample.modifiers;

public class ImplFactory {

	public Api defaultImpl() {
		return new Impl();
	}
}
