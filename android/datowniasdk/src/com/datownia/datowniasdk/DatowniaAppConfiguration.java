package com.datownia.datowniasdk;

/*	Class : 	DatowniaAppConfiguration 
 *  Function :  Holds all the config settings and methods that datownia requires
 *  			such as accessTokens etc 
 * 
 */

public class DatowniaAppConfiguration 
{
	//variables
	
	private String appKey;
	private String appSecret;
	private String publisher;
	private int checkChangesFrequency;
	private DatowniaAccessToken accessToken;
	private String host;
	private String phoneDatabasePath;
	private String phoneDatabaseName;

	//methods
	
	public String getAppKey() {
		return appKey;
	}
	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}
	public String getAppSecret() {
		return appSecret;
	}
	public void setAppSecret(String appSecret) {
		this.appSecret = appSecret;
	}
	public DatowniaAccessToken getAccessToken() {
		return accessToken;
	}
	public void setAccessToken(DatowniaAccessToken accessToken) {
		this.accessToken = accessToken;
	}
	public String getPublisher() {
		return publisher;
	}
	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}
	public int getCheckChangesFrequency() {
		return checkChangesFrequency;
	}
	public void setCheckChangesFrequency(int checkChangesFrequency) {
		this.checkChangesFrequency = checkChangesFrequency;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	
	
	/**
	 * @return path to store database files. trailing slash
	 */
	public String getDatabaseFolder() {
		return phoneDatabasePath;
	}
	
	/**
	 * @param phoneDatabasePath path to use to store database file. trailing slash will be added if not present
	 */
	public void setFullDatabasePath(String phoneDatabasePath) {
		if (!phoneDatabasePath.endsWith("/"))
			phoneDatabasePath = phoneDatabasePath.concat("/");
		this.phoneDatabasePath = phoneDatabasePath;
	}
	public String getFullDatabasePath() {
		return phoneDatabasePath + phoneDatabaseName;
	}

	public String getDatabaseName() {
		return phoneDatabaseName;
	}
	public void setDatabaseName(String phoneDatabaseName) {
		this.phoneDatabaseName = phoneDatabaseName;
	}

}
