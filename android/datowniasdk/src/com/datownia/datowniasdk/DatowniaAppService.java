package com.datownia.datowniasdk;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;

import com.datownia.datowniasdk.oauth2.OAuth2Client;
import com.releasemobile.data.RepositoryStorableContext;

import android.util.Log;

public class DatowniaAppService extends ServiceBase 
{
	private String appScope = null;
	private DaoFactory daoFactory = new DaoFactory();
	private ConnectionFactory connectionFactory;
	
	public DatowniaAppService(RepositoryStorableContext appContext, DatowniaAppConfiguration configurationSettings)
	{
		super(appContext, configurationSettings);
		connectionFactory = new ConnectionFactory();
	}
	
	public DatowniaAppService(RepositoryStorableContext appContext, DatowniaAppConfiguration configurationSettings, DaoFactory daoFactory, OAuth2Client oauth2Client, ConnectionFactory connectionFactory)
	{
		super(appContext, configurationSettings, oauth2Client);
		this.daoFactory = daoFactory;
		this.connectionFactory = connectionFactory;
	}
	
	public DatowniaAppService()
	{
		
	}

	//download the 
	public void downloadDb() throws IOException, JSONException
	{
		Log.d("datownia", "begin download app db");
		//builds the access token if one does not exist yet
		//and is stored as part of the bases' configuration settings 
		this.oauth2Client.getAccessToken(this.getScope());
		
		//create the URL for the download of the file
		URL url = null;
		
		try 
		{
			url = new URL(String.format("https://%s/api/app/%s/%s.sqlite", 
							this.configurationSettings.getHost(), 
							this.configurationSettings.getPublisher(), 
							this.configurationSettings.getAppKey()));
		} 
		catch (MalformedURLException e) 
		{
			Logger.e("datownia", String.format("failed to download database. \r\n%s", Log.getStackTraceString(e)));
			return;
		}
		
		
		//getGTTPDownload will returns an input stream.
		//we use this input stream to create a sqlite database helper which
		//handles the copying of the downloaded database onto the app
		InputStream resultStream = this.getHTTPDownload(url);
		
		if(resultStream != null)
		{
			Log.d("datownia", String.format("storing database to path %s", this.configurationSettings.getFullDatabasePath()));
			
			File folder = new File(this.configurationSettings.getDatabaseFolder());
			if (!folder.exists())
			{
				folder.mkdir();
			}
			OutputStream outputStream = new FileOutputStream(this.configurationSettings.getFullDatabasePath());
			
			IOUtils.copy(resultStream, outputStream);
			
			outputStream.close();
			resultStream.close();
			
		}
	}

	//synchronise the database tables with datownia for updated data
	public void synchroniseDb() throws IOException, JSONException
	{
		//temporary .. jsut request a new token no matter what
		//this.requestAccessTokenIfNeeded(this.getScope());
		//this.configurationSettings.setAccessToken(this.generateAccessTokenFromScope(this.getScope()));
		
		DatowniaManagementDAO dao = daoFactory.getDatowniaDao(this.applicationContext, this.configurationSettings);
		
		List<DatowniaTableDef> tableDefs = dao.getAllTableDefRecords();
		
		//now that there is a list of objects representing the table def records
		//loop through for each one and extract the components 
		
		for (DatowniaTableDef datowniaTableDef : tableDefs) 
		{
			String tableName = datowniaTableDef.getTableName();
			//the string which is tableName without the publisher name and the "/"
			String minusPublisherName = null;
			
			//docName will be the minusPublisherName - the "_1.0" etc
			String docName = null;
			String versionNumberStr = null;
		
			int slashPoint = tableName.indexOf("/");
			minusPublisherName = tableName.substring(slashPoint + 1);
			
			//extract the doc name from minusPublisherName
			int underScorePoint = minusPublisherName.indexOf("_");
			docName = minusPublisherName.substring(0, underScorePoint);
			
			//extract the version number from the minusPublisherName
			versionNumberStr = minusPublisherName.substring(underScorePoint + 1);
			
			//retrieve the delta update SQL string
			Boolean foundDeltas = true;
			while(foundDeltas)
			{
				foundDeltas = this.getDeltaAndUpdateTable(docName, versionNumberStr, datowniaTableDef.getSeqNo());

				datowniaTableDef = dao.getTableDefRecord(tableName);
			}
			
		}
	}
	
	private String getScope()
	{
		if(this.appScope == null)
			this.appScope = String.format("Read|%s|%s", "app", this.configurationSettings.getAppKey());
		
		return this.appScope;
	}
	
	private String getDeltaScope(String docName)
	{
		return String.format("Read|%s|%s", this.configurationSettings.getPublisher(), docName);
	}
	
	
	private InputStream getHTTPDownload(URL endPoint)
	{
		InputStream resultInputStream = null;
		
		URL url;
		try 
		{
			url = new URL(endPoint.toString());

			HttpURLConnection connection = SecureConnection.GetConnection(url);
			
			if (connection == null) 
			{
				Logger.w("datownia", String.format("getHTTPDownload failed. could not get connection"));
				return null;
			}

			connection.setRequestMethod("GET");
			connection.setDoInput(true);
			connection.setRequestProperty("Authorization", "Bearer "+ this.oauth2Client.getAccessToken(this.getScope()).getAccessToken());
			connection.setRequestProperty("scope", this.getScope());
			connection.setRequestProperty("grant_type", "client_credentials");
			connection.setRequestProperty("client_id", this.configurationSettings.getAppKey());
			connection.setRequestProperty("client_secret", this.configurationSettings.getAppSecret());

			//StringBuffer text = new StringBuffer();
			resultInputStream = connection.getInputStream();

			if(resultInputStream == null)
				return null;
		
		}
		catch (Exception e) 
		{
			Logger.e("datownia", String.format("failed to download database. \r\n%s", Log.getStackTraceString(e)));

		} 
		
		return resultInputStream;
		
	}

	private Boolean getDeltaAndUpdateTable(String documentName, String version, int sequenceNumber) throws IOException, JSONException
	{
		URL url = null;
		
		String accessTokenForDelta = this.oauth2Client.getAccessToken(this.getDeltaScope(documentName)).getAccessToken();
		

		url = new URL(String.format("https://%s/api/doc/%s/v%s/delta/%s.sql?seq=%d&limit=%d",
				this.configurationSettings.getHost(),
				this.configurationSettings.getPublisher(),
				version,
				documentName,
				sequenceNumber,
				this.configurationSettings.getLimit()));

		HttpURLConnection connection = connectionFactory.getConnection(url);
		
		if (connection == null) 
		{
			Logger.w("datownia", String.format("delta failed. could not get connection"));
			return false;
		}
			
				
		connection.setRequestMethod("GET");
		connection.setDoInput(true);
		connection.setRequestProperty("Authorization", "Bearer " + accessTokenForDelta);
		connection.setRequestProperty("scope", this.getDeltaScope(documentName));
		connection.setRequestProperty("client_id", this.configurationSettings.getAppKey());
		
		int status = connection.getResponseCode();
		
		if(status != 200)
		{
			Logger.w("datownia", String.format("delta failed. http status was %d. response: %s", status, IOUtils.toString(connection.getErrorStream())));
			return false;
		}
		
		InputStream resultInputStream = connection.getInputStream();
		
		InputStreamReader in;
		in = new InputStreamReader(resultInputStream);

	    BufferedReader buff = new BufferedReader(in);
	    
	    DatowniaManagementDAO dao = daoFactory.getDatowniaDao(this.applicationContext, this.configurationSettings);
	    Boolean hadUpdates = dao.updateDatabase(buff);
	   
	    connection.disconnect();

	    return hadUpdates;
	}
	
	
}
