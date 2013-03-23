package se.jbee.cls.graph;

import se.jbee.cls.Class;
import se.jbee.cls.Field;

public final class FieldNode
		implements Node<Field> {

	private Field key;
	public final ClassNode declaringClass;
	public final ClassNode type;
	public final Edges<Class, ClassNode> accessedBy = new Edges<Class, ClassNode>();

	FieldNode( ClassGraph graph, Field field ) {
		super();
		this.key = field;
		this.declaringClass = graph.cls( field.declaringClass );
		this.type = graph.cls( field.type );
	}

	@Override
	public Field id() {
		return key;
	}

	void declaredAs( Field field ) {
		this.key = field;
	}

	@Override
	public String toString() {
		return key.toString();
	}

}