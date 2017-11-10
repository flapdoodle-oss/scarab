package se.jbee.sample.checks;

public class Escaper {
	
	public static Class<?> nonPublicClass() {
		return NonPublicClass.class;
	}
	public static Class<?> nonPublicInterface() {
		return NonPublicInterface.class;
	}
}
