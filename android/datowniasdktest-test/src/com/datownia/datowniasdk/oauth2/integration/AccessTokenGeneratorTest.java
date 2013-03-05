package com.datownia.datowniasdk.oauth2.integration;

import java.io.IOException;

import org.json.JSONException;

import android.content.Context;
import android.util.Log;

import com.datownia.datowniasdk.oauth2.AccessTokenGenerator;
import com.datownia.datowniasdk.oauth2.DatowniaAccessToken;
import com.datownia.datowniasdk.oauth2.OAuth2Client;
import com.datownia.datowniasdk.oauth2.unit.AccessTokenCacheTest;
import com.datownia.datowniasdk.testframework.DatowniaTestCase;

import junit.framework.TestCase;

public class AccessTokenGeneratorTest extends DatowniaTestCase {

	protected void setUp() throws Exception
	{
		AccessTokenCacheTest.deleteFiles(getTestContext().getCacheDir()); //make sure file cache is empty before we start
		super.setUp();
	}
	
	/**
	 * Test cases:
	 * 1. invalid scope
	 * 2. valid scope
	 * 
	 * note we don't need to test every type of scope as we aren't unit testing datownia server but we do want to make sure we handle invalid and valid response
	 */
	public void testGenerateAccessTokenFromScope()
	{
		Context context = getTestContext();
		AccessTokenGenerator tokenGenerator = new AccessTokenGenerator(getConfig(context));
		
		String scope = "Read|example|willstoyscatalogue/catalogue";
		DatowniaAccessToken accessToken = getAccessToken(tokenGenerator, scope);
		assertNotNull(accessToken);
		assertTrue(accessToken.getAccessToken().length() > 0);
		
		scope = "Read|example|notvalid";
		accessToken = getAccessToken(tokenGenerator, scope);
		assertNull(accessToken);

		scope = "very wrong scope";
		accessToken = getAccessToken(tokenGenerator, scope);
		assertNull(accessToken);
	
		
	}
	


	protected DatowniaAccessToken getAccessToken(AccessTokenGenerator tokenGenerator,
			String scope) {
		DatowniaAccessToken accessToken = null;
		try {
			
			accessToken = tokenGenerator.generateAccessTokenFromScope(scope);
		} catch (IOException e) {
			e.printStackTrace();
			assertTrue(String.format("%s. \r\n%s", e.toString(), Log.getStackTraceString(e)), false);
		} catch (JSONException e) {
			e.printStackTrace();
			assertTrue(String.format("%s. \r\n%s", e.toString(), Log.getStackTraceString(e)), false);
		}
		return accessToken;
	}
}
