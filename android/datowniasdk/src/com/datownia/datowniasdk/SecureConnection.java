package com.datownia.datowniasdk;

import java.io.IOException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class SecureConnection 
{

	public static HttpsURLConnection GetConnection(URL url) 
	{
		//create trust manager
		X509TrustManager tm = new X509TrustManager() 
		{

			public void checkClientTrusted(X509Certificate[] chain,
					String authType) throws CertificateException {
				// TODO Auto-generated method stub
				
			}

			public void checkServerTrusted(X509Certificate[] chain,
					String authType) throws CertificateException {
				// TODO Auto-generated method stub
				
			}

			public X509Certificate[] getAcceptedIssuers() {
				// TODO Auto-generated method stub
				return null;
			}
			
//			@Override
//			public X509Certificate[] getAcceptedIssuers() {
//				return null;
//			}
//			
//			@Override
//			public void checkClientTrusted( X509Certificate[] chain, String authType)
//											throws CertificateException 
//			{
//				// Intentionally empty method body
//
//			}
//			
//			@Override
//			public void checkServerTrusted(
//					X509Certificate[] chain, String authType) throws CertificateException 
//			{
//				// Intentionally empty method body
//
//			}
		};

		SSLContext ctx = null;
		HttpsURLConnection conn = null;

		try 
		{
			ctx = SSLContext.getInstance("TLS");
			ctx.init(null, new TrustManager[] { tm }, null);

			SSLContext.setDefault(ctx);

			conn = (HttpsURLConnection) url.openConnection();
			conn.setHostnameVerifier(new HostnameVerifier() 
			{	
				public boolean verify(String paramString, SSLSession paramSSLSession) 
				{
					return true;
				}
			});
			
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (KeyManagementException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return conn;
	}


}
