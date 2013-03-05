package com.datownia.datowniasdk.oauth2;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

import com.datownia.datowniasdk.Logger;

import android.content.Context;
import android.util.Log;


public class AccessTokenCache implements AccessTokenCacheable {

	private HashMap<String, DatowniaAccessToken> tokenCache = new HashMap<String, DatowniaAccessToken>();
	private Context context;
	
	public AccessTokenCache(Context context)
	{
		this.context = context;
		
	}
	
	private DatowniaAccessToken getAccessTokenOrNullIfExpired(DatowniaAccessToken token)
	{
		if (token.isExpired())
			return null;
		else
			return token; //is in memory and not expired
	}

	/* (non-Javadoc)
	 * @see com.datownia.datowniasdk.oauth2.AccessTokenCacheable#getAccessToken(java.lang.String)
	 */
	@Override
	public DatowniaAccessToken getAccessToken(String scope) {
		//is token in memory? 
		//	yes then use it, 
		//	no then is it in Internal Storage
		//		yes, grab it, put in memory and use it
		//		no, client will need to go get one from the server
		
		DatowniaAccessToken token = tokenCache.get(scope);
		if (token != null)
		{
			return getAccessTokenOrNullIfExpired(token);
		}
			
		
		//not in memory, check internal storage
		File cacheFile = new File(context.getCacheDir(), getSafeScope(scope));
		if (cacheFile.exists())
		{
			//deserialize it
			// Read from disk using FileInputStream
			try
			{
				FileInputStream fIn = new FileInputStream(cacheFile);

				// Read object using ObjectInputStream
				ObjectInputStream objIn = new ObjectInputStream (fIn);

				// Read an object
				Object obj = objIn.readObject();
				
				if (obj instanceof DatowniaAccessToken)
				{
					// Cast object to a DatowniaAccessToken
					DatowniaAccessToken cachedToken = (DatowniaAccessToken) obj;

					return getAccessTokenOrNullIfExpired(cachedToken);
				}
			} catch (FileNotFoundException e) {
				//we'll log it but continue as we can still get the token from the server
				Logger.w("datownia", String.format("Failed to read cache file %s. \r\n%s", cacheFile.getAbsolutePath(), Log.getStackTraceString(e)));
	
			}
			catch (IOException e) {
				//we'll log it but continue as we can still get the token from the server
				Logger.w("datownia", String.format("Failed to read cache file %s. \r\n%s", cacheFile.getAbsolutePath(), Log.getStackTraceString(e)));
			} catch (ClassNotFoundException e) {
				//we'll log it but continue as we can still get the token from the server
				Logger.w("datownia", String.format("cache file was to of expected type %s. \r\n%s", cacheFile.getAbsolutePath(), Log.getStackTraceString(e)));

			}

		}
		
		return null;
	}

	@Override
	public void setAccessToken(String scope, DatowniaAccessToken token) {
		
		//put in HashMap
		tokenCache.put(scope, token);
		
		//put in InternalStorage
		File cacheFile = null;
		try {
			cacheFile = new File(context.getCacheDir(), getSafeScope(scope));

			FileOutputStream fOut = new FileOutputStream(cacheFile);
			
			ObjectOutputStream objOut = new ObjectOutputStream (fOut);
			
			objOut.writeObject ( token );
		} catch (FileNotFoundException e) {
			Logger.w("datownia", String.format("Failed to create cache file %s. \r\n%s", cacheFile.getAbsolutePath(), Log.getStackTraceString(e)));

		}
		catch (IOException e) {
			Logger.w("datownia", String.format("Failed to create cache file %s. \r\n%s", cacheFile.getAbsolutePath(), Log.getStackTraceString(e)));
		}

		
	}
	
	private String getSafeScope(String scope)
	{
		try {
			scope = URLEncoder.encode(scope, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return scope;
	}
	
	/**
	 * clears tokens from memory. they will remain in local storage though
	 */
	public void clearMemoryCache()
	{
		tokenCache.clear();
	}

}
