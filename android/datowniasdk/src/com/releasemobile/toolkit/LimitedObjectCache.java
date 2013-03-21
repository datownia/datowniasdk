package com.releasemobile.toolkit;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

/**
 * A hash map which has a maximum number of items. If exceed the oldest one is removed
 * @author ian
 *
 * @param <K>
 * @param <V>
 */
public class LimitedObjectCache<K, V> extends LinkedHashMap<K,V> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8106385969997790597L;
	private int maximumSize;
	private CacheListener<K,V> listener;

	public LimitedObjectCache(int maximumSize)
	{
		this.maximumSize = maximumSize;
		
		
	}
	
	public LimitedObjectCache(int maximumSize, CacheListener<K,V> listener)
	{
		this.maximumSize = maximumSize;
		this.listener = listener;
		
	}
	
	@Override
	protected boolean removeEldestEntry(Map.Entry<K,V> eldest)
	{
		boolean remove = size() > maximumSize;
		
		if (remove && listener != null)
		{
			listener.willRemoveOldItem(eldest.getKey(), eldest.getValue());
		}
		
		return remove;
	}
}
