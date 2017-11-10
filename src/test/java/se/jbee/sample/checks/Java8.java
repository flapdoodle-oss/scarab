package se.jbee.sample.checks;

import java.util.function.Consumer;

public class Java8 {

	public void lambdas() {
		Consumer<String> lambda=s -> consumeString(s);
		Consumer<String> methRef=Java8::consumeString;
		lambda.accept("foo");
		methRef.accept("bar");
	}
	
	private static void consumeString(String s) {
		System.out.println(s);
	}
}
