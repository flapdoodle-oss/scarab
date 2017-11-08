package de.flapdoodle.unravel;

import java.io.IOException;
import java.io.InputStream;
import java.util.function.Supplier;
import java.util.zip.ZipInputStream;

import se.jbee.jvm.Archive;
import se.jbee.jvm.Package;
import se.jbee.jvm.Packages;
import se.jbee.jvm.graph.ClassGraph;
import se.jbee.jvm.io.ArchiveFilter;
import se.jbee.jvm.io.JarScanner;

public class ScarabJarStamper implements JarStamper {

	@Override
	public Stamp stamp(Supplier<InputStream> jarStream) {
		Archive archive = Archive.NONE;
		ClassGraph out = new ClassGraph(Packages.packages(Package.DEFAULT));

		try {
			try (InputStream is = jarStream.get()) {
				new JarScanner(out, ArchiveFilter.ALL).scan("dummy", new ZipInputStream(is));
			}
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}

		return ScarabClassesStamper.stampOf(out);
	}

}
