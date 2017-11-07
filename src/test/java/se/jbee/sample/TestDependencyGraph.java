package se.jbee.sample;

import java.io.IOException;

import org.junit.Test;

import se.jbee.jvm.Archive;
import se.jbee.jvm.Package;
import se.jbee.jvm.Packages;
import se.jbee.jvm.file.ClassFile;
import se.jbee.jvm.file.ClassInputStream;
import se.jbee.jvm.graph.ClassGraph;
import se.jbee.jvm.reflect.ClassProcessor;

public class TestDependencyGraph {

	@Test
	public void dependencyGraph() throws IOException {
		Archive archive=Archive.NONE;
		ClassInputStream in=new ClassInputStream(getClass().getResourceAsStream("/scarab/src/test/resources/packages/sample/one-two-three/One.class"));
		Packages basePackages=Packages.packages(Package.pkg("se"));
		ClassProcessor out=new ClassGraph(basePackages);
		
		ClassFile.readClassfile(archive, in, out);
	}
}
