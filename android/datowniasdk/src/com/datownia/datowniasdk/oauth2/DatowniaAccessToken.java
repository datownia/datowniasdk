package com.datownia.datowniasdk.oauth2;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Locale;

/*	Class : 	DatowniaAccessToken 
 *  Function :  Holds all the basic information about the access token that
 * 				Datownia gave from the request to get one.
 */

public class DatowniaAccessToken implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String accessToken; 
	private int expiresInSeconds = 0;
    private String tokenType = "";
    private Calendar dateCreated;
    private Calendar expiryDate = null;
    public static final int EXPIRY_TOLERANCE = 90; //used to make us think the token expires before it really expires, so that we don't use expired tokens when close to the actual expiry time

    public DatowniaAccessToken()
    {
    	
    }
    
    public DatowniaAccessToken( String accessToken, int expiresInSeconds, String tokenType)
    {
    	this.accessToken = accessToken;
		this.tokenType = tokenType;
		dateCreated = Calendar.getInstance(Locale.ROOT);
		setExpiresInSeconds(expiresInSeconds);
		
    }
    
    public DatowniaAccessToken( String accessToken, int expiresInSeconds, String tokenType, Calendar dateCreated)
    {
    	this.accessToken = accessToken;
		this.tokenType = tokenType;
		this.dateCreated = dateCreated;
		setExpiresInSeconds(expiresInSeconds);
		
    }

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public int getExpiresInSeconds() {
		return expiresInSeconds;
	}

	public void setExpiresInSeconds(int expiresInSeconds) {
		this.expiresInSeconds = expiresInSeconds;
		setExpiryDate();
	}
	
	public Calendar getExpiryDate()
	{
		if (expiryDate == null)
			setExpiryDate();
		return expiryDate;
	}
	
	private void setExpiryDate()
	{
		Calendar expiry = (Calendar)dateCreated.clone();
		if (expiresInSeconds > EXPIRY_TOLERANCE)
			expiry.add(Calendar.SECOND, expiresInSeconds - EXPIRY_TOLERANCE);
		else
			expiry.add(Calendar.SECOND, expiresInSeconds); //this is highly unlikely to occur as expiry would never be set so low
		
		expiryDate = expiry;
	}
	
	public boolean isExpired()
	{
		Calendar now = Calendar.getInstance(Locale.ROOT);
		return now.after(getExpiryDate());
	}

	public String getTokenType() {
		return tokenType;
	}

	public void setTokenType(String tokenType) {
		this.tokenType = tokenType;
	}

}
