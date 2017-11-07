package de.flapdoodle.unravel;

import java.io.InputStream;
import java.util.function.Supplier;

public interface JarStamper {
	Stamp stamp(Supplier<InputStream> jarStream);
}
