package com.datownia.datowniasdk.oauth2.unit;

import java.io.IOException;

import org.json.JSONException;

import android.content.Context;

import com.datownia.datowniasdk.oauth2.AccessTokenGenerator;
import com.datownia.datowniasdk.oauth2.DatowniaAccessToken;
import com.datownia.datowniasdk.oauth2.OAuth2Client;
import com.datownia.datowniasdk.testframework.DatowniaTestCase;
import static org.mockito.Mockito.*;
import junit.framework.TestCase;

public class OAuth2ClientTest extends DatowniaTestCase {

	private static final String scope = "a/scope";
	
	protected void setUp() throws Exception
	{
		AccessTokenCacheTest.deleteFiles(getTestContext().getCacheDir()); //make sure file cache is empty before we start
		super.setUp();
	}
	/**
	 * Test cases:
	 * 1. access token not already cached
	 * 2. access token already cached
	 * @throws JSONException 
	 * @throws IOException 
	 */
	public void testGetAccessToken() throws IOException, JSONException
	{
		Context context = getTestContext();
		
		//create a mock AccessTokenGenerator that returns a dummy token
		DatowniaAccessToken dummyToken = new DatowniaAccessToken("atoken", 7200, "tokenType");
		AccessTokenGenerator mockGenerator = mock(AccessTokenGenerator.class);
		when(mockGenerator.generateAccessTokenFromScope(anyString())).thenReturn(dummyToken);
		
		//Case 1. access token not already cached (generator should be called)
		getAndTestAccessToken(context, dummyToken, mockGenerator);
		verify(mockGenerator, times(1)).generateAccessTokenFromScope(anyString());
		
		//Case 2. access token already cached (do it again - generator should not be called) 
		getAndTestAccessToken(context, dummyToken, mockGenerator);
		verify(mockGenerator, times(1)).generateAccessTokenFromScope(anyString());
		
	}
	protected void getAndTestAccessToken(Context context,
			DatowniaAccessToken dummyToken, AccessTokenGenerator mockGenerator)
			throws IOException, JSONException 
	{
		OAuth2Client oauth2Client = new OAuth2Client(context, getConfig(context), mockGenerator);
		DatowniaAccessToken retrievedToken = oauth2Client.getAccessToken(scope);
		
		assertEquals(dummyToken.getAccessToken(), retrievedToken.getAccessToken());
	}

}
