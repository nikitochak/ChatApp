package com.sirma.itt.javacourse.server;

/**
 * Represents a pair of two objects which can be accessed.
 * 
 * @author Nikolay
 * 
 */
public class Pair<K, V> {
	private K key;
	private V value;

	/**
	 * Initializes the fields.
	 * 
	 * @param key
	 *            the key
	 * @param value
	 *            the value
	 */
	public Pair(K key, V value) {
		this.key = key;
		this.value = value;
	}

	/**
	 * Getter for the first field.
	 * 
	 * @return the first field.
	 */
	public K getKey() {
		return key;
	}

	/**
	 * Getter for the second field.
	 * 
	 * @return the second field
	 */
	public V getValue() {
		return value;
	}
}
