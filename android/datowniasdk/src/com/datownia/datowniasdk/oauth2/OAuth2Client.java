package com.datownia.datowniasdk.oauth2;

import java.io.IOException;
import org.json.JSONException;

import android.content.Context;
import com.datownia.datowniasdk.DatowniaAppConfiguration;

public class OAuth2Client {

	private DatowniaAppConfiguration config;
	private AccessTokenCache tokenCache;
	private AccessTokenGenerator accessTokenGenerator;

	public OAuth2Client(Context context, DatowniaAppConfiguration config) {
		this.config = config;
		this.tokenCache = new AccessTokenCache(context);
		this.accessTokenGenerator = new AccessTokenGenerator(config);
	}
	
	public OAuth2Client(DatowniaAppConfiguration config, AccessTokenCache tokenCache, AccessTokenGenerator generator) {
		this.config = config;
		this.tokenCache = tokenCache;
		this.accessTokenGenerator = generator;

	}
	
	public OAuth2Client(Context context, DatowniaAppConfiguration config, AccessTokenGenerator generator) {
		this.config = config;
		this.tokenCache = new AccessTokenCache(context);;
		this.accessTokenGenerator = generator;

	}

	public DatowniaAccessToken getAccessToken(String scope) throws IOException,
			JSONException {
		// TODO: implement proper access token expiry awareness and could also
		// persist the access token in local storage
		DatowniaAccessToken token = this.tokenCache.getAccessToken(scope);

		// check if access token object is non existant
		if (token == null) {
			
			token = accessTokenGenerator.generateAccessTokenFromScope(scope);
			this.tokenCache.setAccessToken(scope,  token);
		}
		
		return token;
	}

	public AccessTokenGenerator getAccessTokenGenerator() {
		return accessTokenGenerator;
	}

	public void setAccessTokenGenerator(AccessTokenGenerator generator) {
		this.accessTokenGenerator = generator;
	}
	
}
