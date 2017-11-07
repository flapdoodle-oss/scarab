package se.jbee.sample;

import java.io.IOException;
import java.util.stream.Stream;

import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.junit.Test;

import com.google.common.io.Resources;

import de.flapdoodle.graph.GraphAsDot;
import de.flapdoodle.graph.Graphs;
import de.flapdoodle.graph.Graphs.GraphBuilder;
import se.jbee.jvm.Archive;
import se.jbee.jvm.Method;
import se.jbee.jvm.Package;
import se.jbee.jvm.Packages;
import se.jbee.jvm.file.ClassFile;
import se.jbee.jvm.file.ClassInputStream;
import se.jbee.jvm.graph.ClassGraph;

public class TestDependencyGraph {

	@Test
	public void dependencyGraph() throws IOException {
		
		Archive archive=Archive.NONE;
		
		Packages basePackages=Packages.packages(Package.pkg("se"));
		ClassGraph out=new ClassGraph(basePackages);
		
		Stream.of("One","Two","Three").forEach(c -> {
			try {
				ClassFile.readClassfile(archive, new ClassInputStream(Resources.getResource("packages/sample/one-two-three/"+c+".class").openStream()), out);
			}
			catch (IOException e) {
				throw new RuntimeException(e);
			}
		});
		
		out.methods.forEach(node  -> {
			if (node.calledBy.isEmpty()) {
				System.out.println("id: "+node.id());
				node.calls.forEach(calls -> {
					System.out.println(" -> "+calls.id());
				});
			}
		});
		
		GraphBuilder<Method, DefaultEdge, DefaultDirectedGraph<Method, DefaultEdge>> builder = Graphs.graphBuilder(Graphs.directedGraph(Method.class,		DefaultEdge.class)).get();
		
		System.out.println("----------------------");
		
		out.methods.forEach(node -> {
			System.out.println(" "+node.id());
			node.calls.forEach(dest -> {
				System.out.println(" -> "+dest.id());
				builder.addVertices(node.id(), dest.id());
				builder.addEdge(node.id(), dest.id());
			});
		});
		
		DefaultDirectedGraph<Method, DefaultEdge> graph = builder.build();
		
		String graphAsDot = GraphAsDot.builder(Method::toString).build().asDot(graph);
		
		System.out.println("----------------------");
		
		System.out.println(graphAsDot);
		
	}
}
