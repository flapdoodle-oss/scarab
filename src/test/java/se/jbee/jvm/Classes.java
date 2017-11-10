package se.jbee.jvm;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.function.Predicate;
import java.util.function.Supplier;

import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.io.Resources;

import se.jbee.jvm.file.ClassFile;
import se.jbee.jvm.file.ClassInputStream;
import se.jbee.jvm.graph.ClassGraph;

public class Classes {

	public static Predicate<Class> classWithName(String name) {
		return c -> c.simpleName().equals(name);
	}
	
	public static ClassGraph read(java.lang.Class<?> clazz) {
		try (InputStream is = byteCodeInputStream(clazz)) {
			ClassGraph out=new ClassGraph(Packages.packages(Package.DEFAULT));
			ClassFile.readClassfile(Archive.archive("dummy"), new ClassInputStream(is), out);
			return out;
		}
		catch (IOException e) {
			throw new RuntimeException("could not inspect "+clazz, e);
		}
	}
	
	public static Supplier<InputStream> byteCodeOf(java.lang.Class<?> clazz) {
		return () -> byteCodeInputStream(clazz);
	}
	
	private static InputStream byteCodeInputStream(java.lang.Class<?> clazz) {
		try {
			URL resource = clazz.getResource(clazz.getSimpleName()+".class");
			return Resources.asByteSource(Preconditions.checkNotNull(resource,"could not get resource of %s",clazz)).openStream();
		}
		catch (IOException e) {
			throw new RuntimeException("could get bytecode of "+clazz, e);
		}
	}
	
	public static ImmutableList<Supplier<InputStream>> byteCodeOfClasses(java.lang.Package pkg) {
		return classFilesOfPackage(pkg).stream()
			.map(resourceName -> streamSupplier(resourceName))
			.collect(ImmutableList.toImmutableList());
	}

	
	public static ImmutableSet<String> classFilesOfPackage(java.lang.Package basePackage) {
		Reflections ref = new Reflections(new ConfigurationBuilder()
		     .setUrls(ClasspathHelper.forPackage(basePackage.getName()))
		     .setScanners(new ResourcesScanner(), new SubTypesScanner()/*, 
		                  new TypeAnnotationsScanner().filterResultsBy(optionalFilter)*/)
		     .filterInputsBy(new FilterBuilder().includePackage(basePackage.getName())));
		
		
		return ImmutableSet.copyOf(ref.getResources(name -> name.endsWith(".class")));
	}

	private static Supplier<InputStream> streamSupplier(String resourceName)
	{
		return () -> {
			try {
				return Resources.getResource(resourceName).openStream();
			}
			catch (IOException e) {
				throw new RuntimeException("could not load "+resourceName,e);
			}
		};
	}
	
}
