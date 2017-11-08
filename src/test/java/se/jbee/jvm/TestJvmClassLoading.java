package se.jbee.jvm;

import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;

import com.google.common.io.Resources;

import se.jbee.jvm.file.ClassFile;
import se.jbee.jvm.file.ClassInputStream;
import se.jbee.jvm.graph.ClassGraph;
import se.jbee.sample.oneTwoThree.One;

public class TestJvmClassLoading {

	@Test
	public void byteCodeOfClass() throws IOException {
		try (InputStream is = Resources.asByteSource(One.class.getResource("One.class")).openStream()) {
			ClassGraph out=new ClassGraph(Packages.packages(Package.DEFAULT));
			ClassFile.readClassfile(Archive.NONE, new ClassInputStream(is), out);
			System.out.print(out.classes);
		}
	}
}
