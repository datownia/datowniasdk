package com.datownia.datowniasdk.oauth2;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.datownia.datowniasdk.DatowniaAppConfiguration;
import com.datownia.datowniasdk.Logger;
import com.datownia.datowniasdk.SecureConnection;

public class AccessTokenGenerator
{
	private DatowniaAppConfiguration config;

	public AccessTokenGenerator(DatowniaAppConfiguration config) {
		this.config = config;

	}
	
	/**
	 * Gets an access token from the server
	 * @param scope
	 * @return
	 * @throws IOException
	 * @throws JSONException
	 */
	public DatowniaAccessToken generateAccessTokenFromScope(String scope) throws IOException, JSONException
	{
		StringBuilder root = new StringBuilder(String.format("https://%s/oauth2/token?", this.config.getHost()));

		StringBuilder parameters = new StringBuilder("client_id=");
		parameters.append(this.config.getAppKey());
		parameters.append("&client_secret=");
		parameters.append(this.config.getAppSecret())	;
		parameters.append("&grant_type=client_credentials&scope=");
		parameters.append(scope);

		//get token
		URL url = new URL(root.toString());
		
		// open HTTPS connection
		HttpURLConnection connection = SecureConnection.GetConnection(url);
		
		if (connection == null) 
		{
			Logger.w("datownia", String.format("generateAccessToken failed. could not get connection"));
			return null;
		}
			
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
		
		int status = 0;
		try
		{
			status = connection.getResponseCode();
		}
		catch(IOException e)
		{
			if (e.getMessage().equalsIgnoreCase("Received authentication challenge is null"))
			{
				Logger.w("datownia", String.format("generateAccessTokenFromScope failed. http status was 401. response: %s", IOUtils.toString(connection.getErrorStream())));
				return null;
			}
			else
			{
				Logger.w("datownia", String.format("generateAccessTokenFromScope failed with IOException. %s. response: %s", Log.getStackTraceString(e), IOUtils.toString(connection.getErrorStream())));
				return null;
			}
		}
		
		
		if(status != 200 )
		{
			Logger.w("datownia", String.format("generateAccessTokenFromScope failed. http status was %d. response: %s", status, IOUtils.toString(connection.getErrorStream())));
			return null;
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

	    DatowniaAccessToken result = new DatowniaAccessToken(objResponse.getString("access_token"), objResponse.getInt("expires_in"), objResponse.getString("token_type"));
		
		//return access token object
		return result;
	}
}