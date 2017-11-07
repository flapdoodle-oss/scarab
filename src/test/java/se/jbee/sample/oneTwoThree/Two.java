package se.jbee.sample.oneTwoThree;

public class Two {

	Hidden hidden=new Hidden();
	
	public String howToGreet() {
		if (hidden.hasFoo()) {
			return new Three().hello();
		}
		if (hidden.hasBar()) {
			return "bar";
		}
		return "noop";
	}
}
