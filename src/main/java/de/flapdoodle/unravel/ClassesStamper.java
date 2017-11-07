package de.flapdoodle.unravel;

import java.io.InputStream;
import java.util.Collection;
import java.util.function.Supplier;

public interface ClassesStamper {
	Stamp stampOf(Collection<? extends Supplier<? extends InputStream>> classStreams);
}
