package com.releasemobile.toolkit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * @author ian
 *
 * 
 */
public class RotatingCache<T> {

	private int size;
	private int currentPosition = -1;
	private List<T> bitmaps;

	public RotatingCache(int size)
	{
		this.size = size;
		bitmaps = new ArrayList<T>(Collections.nCopies(size, (T)null));
		
	}
	
	/**
	 * @return the next item in the cache 
	 */
	public T getNext()
	{
		if (currentPosition >= bitmaps.size())
			throw new IndexOutOfBoundsException("No next item was available");
		
		T item = bitmaps.get(currentPosition);
		
		currentPosition = (currentPosition + 1) % size;
		
		return item;
	}
	
	/**
	 * Makes obj the next item to be retrieved from the cache
	 * @param obj
	 */
	public void putNext(T obj)
	{
		putNextPlus(0, obj);

	}
	
	/**
	 * Makes obj the item to be retrieved after offset+1
	 * @param offset offset = 0 is same as putNext(obj), if offset=0 used then next item retrieve from cache will be obj
	 * @param obj
	 */
	public void putNextPlus(int offset, T obj)
	{
		if (offset >= size)
			throw new IndexOutOfBoundsException("Cannot put item at offset as would be bigger than the cache size");
		
		int nextPosition = (currentPosition + offset + 1) % size;
		
		bitmaps.set(nextPosition, obj);
		
		if (currentPosition == -1 || offset == 0)
		{
			currentPosition = nextPosition;
		}

	}
}
