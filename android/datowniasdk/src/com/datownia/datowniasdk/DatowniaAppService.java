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
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;

import android.content.Context;
import android.util.Log;

public class DatowniaAppService extends ServiceBase 
{
	private String appScope = null;
	
	public DatowniaAppService(Context appContext, DatowniaAppConfiguration configurationSettings)
	{
		super(appContext, configurationSettings);
	}
	
	public DatowniaAppService()
	{
		
	}

	//download the 
	public void downloadAppDB() throws IOException, JSONException
	{
		Log.d("datownia", "begin download app db");
		//builds the access token if one does not exist yet
		//and is stored as part of the bases' configuration settings 
		this.requestAccessTokenIfNeeded(this.getScope());
		
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
			// TODO Auto-generated catch block
			e.printStackTrace();
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
			//create the sqlite helper with the input stream and application context
//			DatowniaSQLiteDBHelper dbHelper = DatowniaSQLiteDBHelper.getInstance(this.applicationContext, 
//																				 this.configurationSettings.getPhoneDatabaseName(),
//																				 this.configurationSettings.getPhoneDatabasePath());
//			dbHelper.initWithInputStream(resultStream);
			
		}
	}

	//synchronise the database tables with datownia for updated data
	public void synchroniseDBTables() throws IOException, JSONException
	{
		//temporary .. jsut request a new token no matter what
		//this.requestAccessTokenIfNeeded(this.getScope());
		//this.configurationSettings.setAccessToken(this.generateAccessTokenFromScope(this.getScope()));
		
		DatowniaManagementDAO dao = new DatowniaManagementDAO(this.applicationContext, 
															  this.configurationSettings.getDatabaseName(), 
															  this.configurationSettings.getDatabaseFolder());
		
		List<DatowniaTableDef> tableDefs = dao.getAllTableDefRecords();
		
		//now that there is a list of objects representing the table def records
		//loop through for each one and extract the components 
		
		for (DatowniaTableDef datowniaTableDef : tableDefs) 
		{
			String tableName = datowniaTableDef.getTableName();
			int sequenceNo = datowniaTableDef.getSeqNo();
			String SQL = null;
			
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
			SQL = this.getHTTPDeltaSQL(docName, versionNumberStr, datowniaTableDef.getSeqNo());
			
			//make sure the sql statement is a valid string
			if( (!SQL.equals("")) || (SQL.length() != 0)  ||  (SQL != null)  )
			{
				dao.updateDatowniaDataBase(SQL);
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
				return null;

			connection.setRequestMethod("GET");
			connection.setDoInput(true);
			connection.setRequestProperty("Authorization", "Bearer "+this.configurationSettings.getAccessToken().getAccessToken());
			connection.setRequestProperty("scope", this.getScope());
			connection.setRequestProperty("grant_type", "client_credentials");
			connection.setRequestProperty("client_id", this.configurationSettings.getAppKey());
			connection.setRequestProperty("client_secret", this.configurationSettings.getAppSecret());

			//StringBuffer text = new StringBuffer();
			resultInputStream = connection.getInputStream();

			if(resultInputStream == null)
				return null;
		
		}
		catch (MalformedURLException e) 
		{
			e.printStackTrace();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		return resultInputStream;
		
	}

	private String getHTTPDeltaSQL(String documentName, String version, int sequenceNumber) throws IOException, JSONException
	{
		String result = null;
		URL url = null;
		
		String accessTokenForDelta = this.generateAccessTokenFromScope(this.getDeltaScope(documentName)).getAccessToken();
		
		try 
		{
			url = new URL(String.format("https://%s/api/doc/%s/v%s/delta/%s.sql?seq=%d",
					this.configurationSettings.getHost(),
					this.configurationSettings.getPublisher(),
					version,
					documentName,
					sequenceNumber));
			
//			url = new URL(String.format("https://%s/api/doc/%s/v%s/delta/%s.sql?%s",
//					this.configurationSettings.getHost(),
//					this.configurationSettings.getPublisher(),
//					version,
//					documentName,
//					"seq=2"));
			
			HttpURLConnection connection = SecureConnection.GetConnection(url);
			
			if (connection == null) 
				return null;
			
			//String accessToken = this.configurationSettings.getAccessToken().getAccessToken();
			
			connection.setRequestMethod("GET");
			connection.setDoInput(true);
			//connection.setRequestProperty("Authorization", "Bearer " + accessToken);
			connection.setRequestProperty("Authorization", "Bearer " + accessTokenForDelta);
			//connection.setRequestProperty("scope", this.getScope());
			connection.setRequestProperty("scope", this.getDeltaScope(documentName));
		//	connection.setRequestProperty("grant_type", "client_credentials");
			connection.setRequestProperty("client_id", this.configurationSettings.getAppKey());
			//connection.setRequestProperty("client_secret", this.configurationSettings.getAppSecret());
			
			StringBuffer text = new StringBuffer();
			int status = connection.getResponseCode();
			
			if(status != 200)
				return "";
			
			InputStream resultInputStream = connection.getInputStream();
			
			InputStreamReader in;
			in = new InputStreamReader(resultInputStream);

		    BufferedReader buff;
		    buff = new BufferedReader(in);
		    String line;
		    
		    do 
		    {
		      line = buff.readLine();
		      text.append(line);
		    } 
		    while (line != null);
		   
		    connection.disconnect();
		    result = text.toString();
		} 
		catch (MalformedURLException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
	
		return result;
	}
	
	
}
