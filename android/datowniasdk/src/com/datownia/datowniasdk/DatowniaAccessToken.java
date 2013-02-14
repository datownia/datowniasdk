package com.datownia.datowniasdk;

/*	Class : 	DatowniaAccessToken 
 *  Function :  Holds all the basic information about the access token that
 * 				Datownia gave from the request to get one.
 */

public class DatowniaAccessToken 
{
	private String accessToken; 
	private String expiresInSeconds = "";
    private String tokenType = "";

    public DatowniaAccessToken()
    {
    	
    }

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getExpiresInSeconds() {
		return expiresInSeconds;
	}

	public void setExpiresInSeconds(String expiresInSeconds) {
		this.expiresInSeconds = expiresInSeconds;
	}

	public String getTokenType() {
		return tokenType;
	}

	public void setTokenType(String tokenType) {
		this.tokenType = tokenType;
	}

}
