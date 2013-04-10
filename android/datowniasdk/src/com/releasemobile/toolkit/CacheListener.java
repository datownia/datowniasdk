package com.releasemobile.toolkit;

public interface CacheListener<K,V> {

	/**
	 * @param item
	 */
	public void willRemoveOldItem(K key, V value);
}
