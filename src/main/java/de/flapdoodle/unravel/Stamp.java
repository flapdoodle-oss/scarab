package de.flapdoodle.unravel;

import java.util.function.Function;

import org.immutables.value.Value.Immutable;

import com.google.common.collect.ImmutableMultimap;

import se.jbee.jvm.Class;
import se.jbee.jvm.Field;
import se.jbee.jvm.Method;

@Immutable
public abstract class Stamp {
	public abstract ImmutableMultimap<Class, Method> visibleMethods();
	public abstract ImmutableMultimap<Class, Field> visibleFields();
	public abstract ImmutableMultimap<Class, Method> usedMethods();
	public abstract ImmutableMultimap<Class, Field> usedFields();
	
	public String prettyPrinted() {
		return prettyPrint(this);
	}
	
	public static ImmutableStamp.Builder builder() {
		return ImmutableStamp.builder();
	}
	
	public static String prettyPrint(Stamp stamp) {
		StringBuilder sb=new StringBuilder();
		sb.append("Methods:\n");
		print(sb, stamp.visibleMethods(), Method::toString);
		sb.append("Fields:\n");
		print(sb, stamp.visibleFields(), Field::toString);
		sb.append("used Methods:\n");
		print(sb, stamp.usedMethods(), Method::toString);
		sb.append("used Fields:\n");
		print(sb, stamp.usedFields(), Field::toString);
		return sb.toString();
	}
	
	private static <T> void print(StringBuilder sb, ImmutableMultimap<Class, T> map, Function<T, String> toString) {
		map.asMap().forEach((clazz, values) -> {
			sb.append(" ").append(clazz).append("\n");
			values.forEach(value -> {
				sb.append(" - ").append(toString.apply(value)).append("\n");
			});
		});
	}
}
