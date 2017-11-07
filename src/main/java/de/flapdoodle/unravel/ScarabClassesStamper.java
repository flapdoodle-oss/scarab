package de.flapdoodle.unravel;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.function.Supplier;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

import se.jbee.jvm.Archive;
import se.jbee.jvm.Class;
import se.jbee.jvm.Package;
import se.jbee.jvm.Packages;
import se.jbee.jvm.file.ClassFile;
import se.jbee.jvm.file.ClassInputStream;
import se.jbee.jvm.graph.ClassGraph;
import se.jbee.jvm.graph.ClassNode;
import se.jbee.jvm.graph.MethodNode;

public class ScarabClassesStamper implements ClassesStamper {

	@Override
	public Stamp stampOf(Collection<? extends Supplier<? extends InputStream>> classStreams) {
		Archive archive=Archive.NONE;
		ClassGraph out=new ClassGraph(Packages.packages(Package.DEFAULT));
		
		classStreams.forEach(streamSupplier -> {
			try (InputStream is = streamSupplier.get())  {
				ClassFile.readClassfile(archive, new ClassInputStream(is), out);
			}
			catch (IOException e) {
				throw new RuntimeException(e);
			}
		});

		return stampOf(out);
	}

	private Stamp stampOf(ClassGraph classGraph) {
		ImmutableSet<Class> classesOfBlob = classGraph.classes.stream()
			.filter(cn -> !cn.modifiers().isUnknown())
			.map(ClassNode::id)
			.collect(ImmutableSet.toImmutableSet());
		
		System.out.println("Classes: ");
		classesOfBlob.forEach(c -> System.out.println(" -> "+c));

		ImmutableSet<Class> visibleClasses = classesOfBlob.stream()
			.filter(c -> c.modifiers.isPublic() || c.modifiers.isProtected())
			.collect(ImmutableSet.toImmutableSet());

		System.out.println("visible Classes: ");
		visibleClasses.forEach(c -> System.out.println(" -> "+c));

		ImmutableList<MethodNode> publicMethods = classGraph.methods.stream()
			.filter(mn -> visibleClasses.contains(mn.declaringClass.id()))
			.collect(ImmutableList.toImmutableList());

		System.out.println("Methods: ");
		publicMethods.forEach(m -> System.out.println(" -> "+m));

		ImmutableList<MethodNode> outgoingMethods = classGraph.methods.stream()
			.filter(mn -> !mn.calledBy.isEmpty())
			.filter(mn -> !classesOfBlob.contains(mn.declaringClass.id()))
			.collect(ImmutableList.toImmutableList());

		System.out.println("outgoing Methods: ");
		outgoingMethods.forEach(m -> System.out.println(" -> "+m));

//		classGraph.classes.forEach(cn -> {
//			if (!cn.modifiers().isUnknown()) {
//				System.out.println(cn);
//			}
//		});
//		System.out.println("----------------------");
//		
//		DefaultDirectedGraph<Method, DefaultEdge> graph = Graphs.with(Graphs.graphBuilder(Graphs.directedGraph(Method.class,		DefaultEdge.class)))
//			.build(builder -> {
//				classGraph.methods.forEach(node -> {
//					node.calls.forEach(dest -> {
//						builder.addVertices(node.id(), dest.id());
//						builder.addEdge(node.id(), dest.id());
//					});
//				});
//			})
//			;
//		
//		Collection<VerticesAndEdges<Method, DefaultEdge>> roots = Graphs.rootsOf(graph);
//		
//		roots.forEach(ve -> {
//			System.out.println(ve);
//		});
		
		return null;
	}

}
