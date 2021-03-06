package com.releasemobile.toolkit;

import junit.framework.TestCase;
import static org.mockito.Mockito.*;

public class LimitedObjectCacheTest extends TestCase {

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void testEldestIsRemoved()
	{
		@SuppressWarnings("unchecked")
		CacheListener<String,String> mockListener = mock(CacheListener.class);
		LimitedObjectCache<String, String> cache = new LimitedObjectCache<String, String>(2, mockListener);
		
		cache.put("key1", "value1");
		assertEquals("value1", cache.get("key1"));
		
		cache.put("key2", "value2");
		assertEquals("value1", cache.get("key1"));
		assertEquals("value2", cache.get("key2"));
		
		cache.put("key3", "value3");
		assertNull(cache.get("key1"));
		assertEquals("value3", cache.get("key3"));
		assertEquals("value2", cache.get("key2"));
		
		verify(mockListener).willRemoveOldItem("key1", "value1");
	}

}
