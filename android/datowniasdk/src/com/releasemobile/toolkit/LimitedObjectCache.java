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

	public LimitedObjectCache(int maximumSize)
	{
		this.maximumSize = maximumSize;
		
		
	}
	
	@Override
	protected boolean removeEldestEntry(Map.Entry<K,V> eldest)
	{
		return size() > maximumSize;
	}
}
