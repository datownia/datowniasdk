package application;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import model.Document;


import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


public class ApiClient {
	private String Publisher;
	private String AppKey;
	private String AppSecret;
	private String serviceHost = "www.datownia.com";
	
	public ApiClient(String publisher, String appKey, String appSecret)
	{
		AppKey = appKey;
		AppSecret = appSecret;
		Publisher = publisher;
	}
	
	public Document GetDocument(String documentId) {
		return GetDocumentWithQuery(documentId, "");
	}
	
	public Document GetDocument(String documentId, int offset, int limit) {
		return GetDocumentWithQuery(documentId, String.format("offset=%s&limit=%s", offset, limit));
	}
	
	public Document GetMetadataOnly(String documentId) {
		return GetDocumentWithQuery(documentId, "metadataonly=y");
	}
	
	public Document GetDocumentSample(String documentId) {
		return GetDocumentWithQuery(documentId, "sampledata=y");
	}
	
	public Document SearchField(String documentId, String field, String value) {
		return GetDocumentWithQuery(documentId, String.format("field=%s&value=%s", field, value));
	}
	
	public Document SearchRange(String documentId, String field, String from, String to) {
		return GetDocumentWithQuery(documentId, String.format("field=%s&from=%s&to=%s", field, from, to));
	}
	
	public Document GetDocumentWithQuery(String docPath, String query)
	{
		URL endpoint;
		Document result = null;

		try {
			endpoint = DocumentEndPoint(docPath, query);
			String scope = String.format("Read|%s|%s", Publisher, docPath);
			result = GetQuery(scope, endpoint);
		}
		 catch (MalformedURLException e) {
			e.printStackTrace();
		}
		
		return result;
	} 
	
	public URL DocumentEndPoint(String docPath, String query) throws MalformedURLException
	{
		if (query == null)
			query = "";

		//strip leading slash
		if (docPath.startsWith("/"))
			docPath = docPath.substring(1);

		return new URL(String.format("https://%s/api/doc/%s/v%s/%s?%s", serviceHost, Publisher, "max", docPath, query));
	}
	
	public String GetAccessToken(String scope){
		String result = "";
		StringBuilder root = new StringBuilder(String.format("https://%s/oauth2/token?", serviceHost));

		StringBuilder parameters = new StringBuilder("client_id=");
		parameters.append(AppKey);
		parameters.append("&client_secret=");
		parameters.append(AppSecret);
		parameters.append("&grant_type=client_credentials&scope=");
		parameters.append(scope);
		
		//get token
		URL url;
		try {
			
			url = new URL(root.toString());
			
			// open HTTPS connection
			HttpsURLConnection connection = SecureConnection.GetConnection(url);
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
			in = new InputStreamReader((InputStream) connection.getContent());
		    BufferedReader buff;
		    buff = new BufferedReader(in);
		    String line;
		    do {
		      line = buff.readLine();
		      text.append(line + "\n");
		    } while (line != null);
		    connection.disconnect();
		    
		    //now that line contains the string, json parse and extract token
		    //System.out.println(text);
		    ObjectMapper om = new ObjectMapper();
		    JsonNode rootNode = om.readValue(text.toString(), JsonNode.class);
		    JsonNode accessToken = rootNode.get("access_token");
		    result = accessToken.asText();
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	public String GetDocument(String scope, URL endpoint, String accessToken) {
		String result = "";
	
		URL url;
		try {
			
			url = new URL(endpoint.toString());
			
			// open HTTPS connection
			HttpURLConnection connection = SecureConnection.GetConnection(url);
				if (connection == null) return result;
				
			connection.setRequestMethod("GET");
			connection.setDoInput(true);
			connection.setRequestProperty("Authorization", "Bearer "+accessToken);
			connection.setRequestProperty("scope", scope);
			connection.setRequestProperty("grant_type", "client_credentials");
			connection.setRequestProperty("client_id", AppKey);
			connection.setRequestProperty("client_secret", AppSecret);
			
			StringBuffer text = new StringBuffer();
			InputStream stream = connection.getInputStream();
			InputStreamReader in;
			in = new InputStreamReader(stream);
		    BufferedReader buff;
		    buff = new BufferedReader(in);
		    String line;
		    do {
		      line = buff.readLine();
		      text.append(line);
		    } while (line != null);
		    connection.disconnect();
		    result = text.toString();
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	public Document DocumentFromString(String raw) {
		ObjectMapper om = new ObjectMapper();
	    Document result = null;
		try {
			result = om.readValue(raw, Document.class);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	    return result;
	}
	
	public Document GetQuery(String scope, URL endpoint) {
		Document result = null;
		String token = GetAccessToken(scope);
		if (token !=null && !token.isEmpty())
		{
			String doc = GetDocument(scope, endpoint, token);
			if (doc !=null && !doc.isEmpty())
				result = DocumentFromString(doc);
		}	    
		return result;
	}
}
