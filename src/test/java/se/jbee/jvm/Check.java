package se.jbee.jvm;

import static org.junit.Assert.assertTrue;

import java.util.function.Predicate;

public class Check<T> implements Predicate<T> {
	
	private final String label;
	private final Predicate<T> test;

	public Check(String label, Predicate<T> test) {
		this.label = label;
		this.test = test;
		
	}

	@Override
	public boolean test(T t) {
		return test.test(t);
	}
	
	@Override
	public String toString() {
		return label;
	}
	
	@Override
	public Predicate<T> negate() {
		return new Check<>("not "+label, test.negate());
	}

	@SafeVarargs
	public static <T> void assertThat(T value, Predicate<T> firstCheck, Predicate<T> ... checks) {
		assertTrue(firstCheck.toString(), firstCheck.test(value));
		for (Predicate<T> check : checks) {
			assertTrue(check.toString(), check.test(value));
		}
		
	}

	public static <T> Predicate<T> check(String label, Predicate<T> test) {
		return new Check<>(label, test);
	}
}