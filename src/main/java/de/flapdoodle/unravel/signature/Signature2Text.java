package de.flapdoodle.unravel.signature;

import java.util.stream.Collectors;

public class Signature2Text {

	public static String toText(Signature sign) {
		StringBuilder sb = new StringBuilder();
		sign.visibleClasses()
				.stream()
				.sorted(VisibleClass.defaultOrdering())
				.forEach(c -> {
					render(sb, c);
				});
		sb.append("---\n");
		sign.usedClasses()
				.stream()
				.sorted(UsedClass.defaultOrdering())
				.forEach(c -> {
					render(sb, c);
				});
		return sb.toString();
	}

	private static void render(StringBuilder sb, VisibleClass c) {
		sb.append(asString(c.visibility())).append(asString(c)).append("\n");
		c.fields()
				.stream()
				.sorted(CommonField.defaultOrdering())
				.forEach(field -> {
					renderField(sb, field);
				});
		c.methods()
				.stream()
				.sorted(CommonMethod.defaultOrdering())
				.forEach(method -> {
					renderMethod(sb, method);
				});
	}

	private static void render(StringBuilder sb, UsedClass c) {
		sb.append(asString(c)).append("\n");
		c.fields()
				.stream()
				.sorted(CommonField.defaultOrdering())
				.forEach(field -> {
					renderField(sb, field);
				});
		c.methods()
				.stream()
				.sorted(CommonMethod.defaultOrdering())
				.forEach(method -> {
					renderMethod(sb, method);
				});
	}

	private static void renderMethod(StringBuilder sb, CommonMethod method) {
		sb.append(" ");
		if (method.isStatic()) {
			sb.append("static ");
		}
		sb.append(asString(method.returnType()));
		sb.append(" .").append(method.name()).append("(");
		sb.append(method.parameterTypes().stream()
				.map(pt -> asString(pt))
				.collect(Collectors.joining(",")));
		sb.append(")");
		sb.append("\n");
	}

	private static void renderField(StringBuilder sb, CommonField field) {
		sb.append(" ");
		if (field.isStatic()) {
			sb.append("static ");
		}
		sb.append(asString(field.valueType()));
		sb.append(" ").append(field.name());
		sb.append("\n");
	}

	private static String asString(Visibility visibility) {
		if (visibility == Visibility.Protected) {
			return "protected ";
		}
		return "";
	}

	private static String asString(CommonClass type) {
		String post="";
		if (type.isArray()) {
			post="[]";
		}
		if (type.packageName().isEmpty()) {
			return type.name()+post;
		}
		return type.packageName() + "." + type.name()+post;
	}

}
