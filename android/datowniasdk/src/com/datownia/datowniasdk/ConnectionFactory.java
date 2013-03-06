package com.datownia.datowniasdk;

import java.net.HttpURLConnection;
import java.net.URL;

public class ConnectionFactory {
	public HttpURLConnection getConnection(URL url)
	{
		return SecureConnection.GetConnection(url);
	}
}
