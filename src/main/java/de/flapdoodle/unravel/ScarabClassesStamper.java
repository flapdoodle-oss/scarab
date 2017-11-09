package de.flapdoodle.unravel;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.function.Supplier;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

import de.flapdoodle.unravel.ImmutableStamp.Builder;
import se.jbee.jvm.Archive;
import se.jbee.jvm.Class;
import se.jbee.jvm.Package;
import se.jbee.jvm.Packages;
import se.jbee.jvm.file.ClassFile;
import se.jbee.jvm.file.ClassInputStream;
import se.jbee.jvm.graph.ClassGraph;
import se.jbee.jvm.graph.ClassNode;
import se.jbee.jvm.graph.FieldNode;
import se.jbee.jvm.graph.MethodNode;

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

		return stampOf(out, archive);
	}

	protected static Stamp stampOf(ClassGraph classGraph, Archive archive) {
		Builder builder = Stamp.builder();
		
		ImmutableSet<Class> classesOfBlob = classGraph.classes.stream()
			.filter(cn -> !cn.id().modifiers.isUnknown())
			.filter(cn -> !cn.id().isPrimitive())
			.filter(cn -> !cn.id().canonicalName().startsWith("java."))
			.filter(cn -> cn.archive.id().equals(archive))
			.map(ClassNode::id)
			.collect(ImmutableSet.toImmutableSet());
		
//		System.out.println("Classes: ");
//		classesOfBlob.forEach(c -> System.out.println(" -> "+c+" - "+c.modifiers.isUnknown()));

		ImmutableSet<Class> visibleClasses = classesOfBlob.stream()
			.filter(c -> c.isProtected() || c.isPublic())
			.collect(ImmutableSet.toImmutableSet());

//		System.out.println("visible Classes: ");
//		visibleClasses.forEach(c -> System.out.println(" -> "+c));

		{
			ImmutableList<MethodNode> publicMethods = classGraph.methods.stream()
				.filter(mn -> visibleClasses.contains(mn.declaringClass.id()))
				.collect(ImmutableList.toImmutableList());
	
//			System.out.println("Methods: ");
//			publicMethods.forEach(m -> System.out.println(" -> "+m));
			
			publicMethods.forEach(m -> builder.putVisibleMethods(m.declaringClass.id(), m));
	
			ImmutableList<MethodNode> outgoingMethods = classGraph.methods.stream()
				.filter(mn -> !mn.calledBy.isEmpty())
				.filter(mn -> !classesOfBlob.contains(mn.declaringClass.id()))
				.collect(ImmutableList.toImmutableList());
	
//			System.out.println("outgoing Methods: ");
//			outgoingMethods.forEach(m -> System.out.println(" -> "+m));
			
			outgoingMethods.forEach(m -> builder.putUsedMethods(m.declaringClass.id(), m));
		}
		{
			ImmutableList<FieldNode> publicFields = classGraph.fields.stream()
					.filter(fn -> fn.id().modifiers.isPublic() || fn.id().modifiers.isProtected())
					.filter(fn -> visibleClasses.contains(fn.declaringClass.id()))
					.collect(ImmutableList.toImmutableList());
				
//				System.out.println("Fields: ");
//				publicFields.forEach(m -> System.out.println(" -> "+m));
				publicFields.forEach(m -> builder.putVisibleFields(m.declaringClass.id(), m));
				
				ImmutableList<FieldNode> outgoingFields = classGraph.fields.stream()
						.filter(mn -> !mn.accessedBy.isEmpty())
						.filter(fn -> !classesOfBlob.contains(fn.declaringClass.id()))
						.collect(ImmutableList.toImmutableList());
					
//					System.out.println("outgoing Fields: ");
//					outgoingFields.forEach(m -> System.out.println(" -> "+m));
					outgoingFields.forEach(m -> builder.putUsedFields(m.declaringClass.id(), m));
		}

		return builder.build();
	}

}
