package com.datownia.datowniasdk.oauth2.unit;

import java.io.File;
import java.util.Calendar;
import java.util.Locale;

import com.datownia.datowniasdk.oauth2.AccessTokenCache;
import com.datownia.datowniasdk.oauth2.DatowniaAccessToken;
import com.datownia.datowniasdk.testframework.DatowniaTestCase;

public class AccessTokenCacheTest extends DatowniaTestCase {

	private static final String scope = "a/scope";
	private static final String accessToken = "atoken";
	private static final String tokenType = "tokentype";
	
	protected void setUp() throws Exception
	{
		deleteFiles(getTestContext().getCacheDir());
		super.setUp();
	}

	/**
	 * Test cases:
	 * 1. token is not in cache
	 * 2. token is in cache
	 * 3. token is in cache but expired
	 * 4. token is in InternalStorage
	 */
	public void testGetAccessToken() {
		AccessTokenCache cache = new AccessTokenCache(getTestContext());
		
		//case 1 - token is not in cache
		DatowniaAccessToken token = cache.getAccessToken(scope);
		assertNull(token);
		
		//case 2 - token is in cache
		cache.setAccessToken(scope, new DatowniaAccessToken(accessToken, 7200, tokenType));
		token = cache.getAccessToken(scope);
		assertEquals(accessToken, token.getAccessToken());
		
		//case 3 - token is in cache but expired
		Calendar inPast = Calendar.getInstance(Locale.ROOT);
		inPast.set(2013, 0, 26, 14, 54, 30);
		cache.setAccessToken(scope, new DatowniaAccessToken(accessToken, 7200, tokenType, inPast));
		token = cache.getAccessToken(scope);
		assertNull(token);
		
		//case 4 - token is in internal storage
		cache.setAccessToken(scope, new DatowniaAccessToken(accessToken, 7200, tokenType));
		cache.clearMemoryCache();
		token = cache.getAccessToken(scope);
		assertEquals(accessToken, token.getAccessToken());
		
	}

	/**
	 * Test cases:
	 * 1. put in cache, make sure we can get it out again
	 * 2. put in cache, kill memory and make sure we can get it out again
	 */
	public void testSetAccessToken() {
		AccessTokenCache cache = new AccessTokenCache(getTestContext());
		
		//case 1 - put in cache, make sure we can get it out again
		cache.setAccessToken(scope, new DatowniaAccessToken(accessToken, 7200, tokenType));
		DatowniaAccessToken token = cache.getAccessToken(scope);
		assertEquals(accessToken, token.getAccessToken());
		
		//case 2 - put in cache, kill memory and make sure we can get it out again
		cache.setAccessToken(scope, new DatowniaAccessToken(accessToken, 7200, tokenType));
		cache.clearMemoryCache();
		token = cache.getAccessToken(scope);
		assertEquals(accessToken, token.getAccessToken());

	}
	
	public static void deleteFiles(File dir){
	    if (dir != null){
	        if (dir.listFiles() != null && dir.listFiles().length > 0){
	            // RECURSIVELY DELETE FILES IN DIRECTORY
	            for (File file : dir.listFiles()){
	                deleteFiles(file);
	            }
	        } else {
	            // JUST DELETE FILE
	            dir.delete();
	        }
	    }
	}

}
