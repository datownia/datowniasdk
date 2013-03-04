package com.datownia.datowniasdk;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import com.releasemobile.data.RepositoryStorableContext;

import android.content.Context;
import android.util.Log;

public class ServiceBase 
{
	protected DatowniaAppConfiguration configurationSettings;
	protected RepositoryStorableContext applicationContext;
	
	public ServiceBase(RepositoryStorableContext appContext, DatowniaAppConfiguration configurationSettings)
	{
		this.applicationContext = appContext;
		this.configurationSettings = configurationSettings;
	}
	
	public ServiceBase()
	{
		
	}

	public void requestAccessTokenIfNeeded(String scope) throws IOException, JSONException
	{
		//TODO: implement proper access token expiry awareness and could also persist the access token in local storage
		
		//check if access token object is non existant
		if(this.configurationSettings.getAccessToken() == null)
		{
			//generate access token from scope
			this.configurationSettings.setAccessToken(this.generateAccessTokenFromScope(scope));
		}
	}
		
	protected DatowniaAccessToken generateAccessTokenFromScope(String scope) throws IOException, JSONException
	{
		//TODO: cache access tokens
		
		DatowniaAccessToken result = null;
		StringBuilder root = new StringBuilder(String.format("https://%s/oauth2/token?", this.configurationSettings.getHost()));

		StringBuilder parameters = new StringBuilder("client_id=");
		parameters.append(this.configurationSettings.getAppKey());
		parameters.append("&client_secret=");
		parameters.append(this.configurationSettings.getAppSecret())	;
		parameters.append("&grant_type=client_credentials&scope=");
		parameters.append(scope);

		//get token
		URL url;
//		try 
//		{
			url = new URL(root.toString());

			// open HTTPS connection
			HttpURLConnection connection = SecureConnection.GetConnection(url);
			
			if (connection == null) return result;
				
			connection.setRequestMethod("POST");
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setUseCaches(false);
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

			//need to set content length
			String bits = parameters.toString();

			int contentLength = bits.toString().getBytes("UTF-8").length;

			String str = String.valueOf(contentLength);
			connection.setFixedLengthStreamingMode(contentLength);
			connection.setRequestProperty("Content-Length", str);
			
			DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
			wr.writeBytes(parameters.toString());
			wr.flush();
			wr.close();

			//connection.connect();
			StringBuffer text = new StringBuffer();
			InputStreamReader in;
			
			if(connection.getResponseCode() != 200 )
			{
				int x = 5;
			}
			
			in = new InputStreamReader((InputStream) connection.getContent());
		    
			BufferedReader buff;
		    buff = new BufferedReader(in);
		    String line;
		    
		    //TODO:is this really the best way to read an entire stream?
		    do 
		    {
		      line = buff.readLine();
		      text.append(line + "\n");
		    } 
		    while (line != null);
		    
		    
		    connection.disconnect();

		    //TODO: use proper types for access token object, and use the actual expiry time rather than store the number of seconds
		    //subtract a few seconds off of the expiry seconds so that we have it expire slightly early rather than late, when you 
		   //would then get an auth error
		    JSONObject objResponse = new JSONObject(text.toString());	
		    String accessToken =  objResponse.getString("access_token");
		    String expiresInSeconds = objResponse.getString("expires_in");
		    String tokenType = objResponse.getString("token_type");

		    //build the result access token object
		    result = new DatowniaAccessToken();
		    result.setAccessToken(accessToken);
		    result.setExpiresInSeconds(expiresInSeconds);
		    result.setTokenType(tokenType);

		
		//return access token object
		return result;
	}

}
