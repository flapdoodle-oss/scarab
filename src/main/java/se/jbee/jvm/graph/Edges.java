package se.jbee.jvm.graph;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.common.base.Preconditions;

public final class Edges<K, T extends Node<K>>
		implements Iterable<T> {

	private final Hashtable<K, T> nodes = new Hashtable<K, T>();

	public boolean contains( K key ) {
		return nodes.containsKey( key );
	}

	public Set<K> nodeIds() {
		return nodes.keySet();
	}

	void add( T node ) {
		nodes.put( node.id(), node );
	}
	
	public void addAll(Edges<K, T> others) {
		nodes.putAll(others.nodes);
	}	

	void put( K key, T node ) {
		nodes.put( key, node );
	}

	@Override
	public String toString() {
		return nodes.values().toString();
	}

	public int count() {
		return nodes.size();
	}

	public T node( K key ) {
		return nodes.get( key );
	}
	
	public T nodeBySimpleName( String name ) {
		for (Entry<K,T> e : nodes.entrySet()) {
			String key = e.getKey().toString();
			int i = key.lastIndexOf('.');
			if (name.equals(key.substring(i+1))) {
				return e.getValue();
			}
		}
		return null;
	}

	@Override
	public Iterator<T> iterator() {
		return nodes.values().iterator();
	}

	public boolean isEmpty() {
		return nodes.isEmpty();
	}

	public Stream<T> stream() {
		return nodes.values().stream();
	}
	
	public void forEach(BiConsumer<? super K, ? super T> action) {
		nodes.forEach(action);
	}
	
	public Set<K> keySet() {
		return nodes.keySet();
	}
	
	public Optional<K> findOne(Predicate<K> matcher) {
		List<K> matching = nodes.keySet().stream()
			.filter(matcher)
			.limit(2)
			.collect(Collectors.toList());
		Preconditions.checkArgument(matching.size()<2,"more than one matching: %s", matching);
		return matching.isEmpty() ? Optional.empty() : Optional.of(matching.get(0));
	}
}
