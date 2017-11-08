package se.jbee.sample.egdecases;

public class PrimitivyTypes {

	public byte[] foo() {
		Integer i=12;
		long l=i.longValue();
		
		Class<? extends String> clazz = String.class;
		clazz.cast("");
		
		int a=i;
		
		byte[] x=new byte[1];
		byte[] result = x.clone();
		return result;
	}
	
	public static void bar() {
		int x = new PrimitivyTypes[0].length;
	}
}
