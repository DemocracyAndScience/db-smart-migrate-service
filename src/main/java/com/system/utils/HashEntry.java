package com.system.utils;

import java.util.Map;

public class HashEntry<K, V>  implements Map.Entry<K, V>{

	private final K key;
    private V value;
    
    public HashEntry(K key, V value) {
        this.key = key;
        this.value = value;
    }

	@Override
	public K getKey() {
		
		return key;
	}

	@Override
	public V getValue() {
		return value;
	}

	@Override
	public V setValue(V value) {
		return value;
	}

}
