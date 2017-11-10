package de.flapdoodle.unravel;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.function.Supplier;

import se.jbee.jvm.Archive;
import se.jbee.jvm.Package;
import se.jbee.jvm.Packages;
import se.jbee.jvm.file.ClassFile;
import se.jbee.jvm.file.ClassInputStream;
import se.jbee.jvm.graph.ClassGraph;

public class ScarabClassesStamper implements ClassesStamper {

	@Override
	public Stamp stampOf(Collection<? extends Supplier<? extends InputStream>> classStreams) {
		Archive archive=Archive.archive("dummy");
		ClassGraph out=new ClassGraph(Packages.packages(Package.DEFAULT));
		
		classStreams.forEach(streamSupplier -> {
			try (InputStream is = streamSupplier.get())  {
				ClassFile.readClassfile(archive, new ClassInputStream(is), out);
			}
			catch (IOException e) {
				throw new RuntimeException(e);
			}
		});

		return StampFactory.stampOf(out, archive);
	}

}
