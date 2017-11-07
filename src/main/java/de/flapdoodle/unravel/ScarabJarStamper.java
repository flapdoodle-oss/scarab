package de.flapdoodle.unravel;

import java.io.InputStream;
import java.util.function.Supplier;

public class ScarabJarStamper implements JarStamper {

	@Override
	public Stamp stamp(Supplier<InputStream> jarStream) {
		return null;
	}

}
