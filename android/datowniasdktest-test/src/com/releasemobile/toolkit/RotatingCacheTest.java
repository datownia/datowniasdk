package com.releasemobile.toolkit;

import android.graphics.Bitmap;
import junit.framework.TestCase;

public class RotatingCacheTest extends TestCase {

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	
	/**
	 * Test cases:
	 * 1. prep image in next slot and get it
	 * 2. prep image in next plus n slot and get it
	 * 3. make when cycles round is ok
	 * 4. if put beyond current size then will be padded with nulls
	 */
	public void testPopulateCacheAndGet()
	{
		//create cache
		RotatingCache<String> imageCache = new RotatingCache<String>(4);
		
		String bitmap1 = "string1";
		String bitmap2 = "string2";
		String bitmap3 = "string3";
		String bitmap4 = "string4";
		
		//case 1 - prep image in next slot
		imageCache.putNext(bitmap1);
		//case 2,5 -prep image in next next slot
		imageCache.putNextPlus(1, bitmap2);
		//prep image in next next next slot
		imageCache.putNextPlus(2, bitmap3);
		
		
		//test case 1 - get next image from cache
		Object bitmapget1 = imageCache.getNext();
		assertSame(bitmap1, bitmapget1);
		
		//test case 4 - missed slot will be null 
		Object bitmapgetnull = imageCache.getNext();
		assertNull(bitmapgetnull);
		
		//test case 2 
		//get next image from cache
		Object bitmapget2 = imageCache.getNext();
		assertSame(bitmap2, bitmapget2);
		
		//get next image from cache
		Object bitmapget3 = imageCache.getNext();
		assertSame(bitmap3, bitmapget3);
		
		//test case 3
		//put next and make sure it is next (we will now have gone round a whole cycle)
		imageCache.putNext(bitmap4);
		Object bitmapget4 = imageCache.getNext();
		assertSame(bitmap4, bitmapget4);
		
		//cycle around putting next and getting
		imageCache.putNext(bitmap1);
		assertSame(bitmap1, imageCache.getNext());
		
		imageCache.putNext(bitmap2);
		assertSame(bitmap2, imageCache.getNext());
		
		imageCache.putNext(bitmap3);
		assertSame(bitmap3, imageCache.getNext());
		
		imageCache.putNext(bitmap4);
		assertSame(bitmap4, imageCache.getNext());

	}

	public void testCannotPutOffsetMoreThanSize()
	{
		RotatingCache<String> imageCache = new RotatingCache<String>(3);
		
		
		boolean exceptionOccurred = false;
		try
		{
			imageCache.putNextPlus(3, "test");
		}
		catch(Exception ex)
		{
			assertTrue(ex instanceof IndexOutOfBoundsException);
			exceptionOccurred = true;
		}
		assertTrue(exceptionOccurred);
	}
	
	public void testEmptyCache()
	{
		RotatingCache<String> imageCache = new RotatingCache<String>(3);
		

		assertNull(imageCache.getNext());
	}
	
}
