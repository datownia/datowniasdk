package com.datownia.datowniasdk.oauth2;


public interface AccessTokenCacheable {

	/**
	 * @param scope
	 * @return access token or null if it was not cached or had expired in cache
	 */
	public DatowniaAccessToken getAccessToken(String scope);
	public void setAccessToken(String scope, DatowniaAccessToken token);
	
}
