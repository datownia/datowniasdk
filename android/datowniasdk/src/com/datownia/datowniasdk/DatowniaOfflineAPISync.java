package com.datownia.datowniasdk;

import java.util.Timer;

import com.releasemobile.data.RepositoryStorableContext;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

public class DatowniaOfflineAPISync 
{
	private RepositoryStorableContext applicationContext;
	private DatowniaAppService appService;
	private DatowniaAppConfiguration configSettings;
	private Timer timer;
	
	
	public DatowniaOfflineAPISync()
	{
		
	}
	
	public void init(RepositoryStorableContext applicationContext, DatowniaAppConfiguration config)
	{
		this.applicationContext = applicationContext;
		this.configSettings = config;
		
		//todo set observers for when db is downloaded
		
	}
	
	//start method called from host application
	public void start()
	{
		//instantiate the timer and scedule the timer task to 
		//fire the datownia timer task once after 0.5 secs and then every subsequent interval
		//defined in the configuration settings
		
		int checkDatowniaInterval = this.configSettings.getCheckChangesFrequency();
		
		this.timer = new Timer();
		timer.schedule(new DatowniaTimerTask(this.applicationContext,this.configSettings), (long) (0.5*1000), checkDatowniaInterval * 1000);
		
		//startSync();                  
	}
	
//	public void stop()
//	{
//		
//	}
//	
//	//start sync process .. launch point for the rest of the app
//	private void startSync()
//	{
//		
//		//create the app service object, which is the main entry for the rest of the datownia
//		//library
//		DatowniaAppService appService = new DatowniaAppService(this.applicationContext);
//		appService.init(this.configSettings);
//		
//		//check if database exists at the location specified in the config settings object
//		if(doesDatabaseExist() == true)
//		{
//			//call syncronise on the app service
//			appService.synchroniseDBTables();
//		}
//		else
//		{
//			//phone has no database from datownia yet, so download it from datownia
//			appService.downloadAppDB();
//		} 
//	}
//	
//	private boolean doesDatabaseExist()
//	{
//		SQLiteDatabase checkDB = null;
//
//		try
//		{
//			String myPath = this.configSettings.getPhoneDatabasePath();
//			checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
//		}
//		catch(SQLiteException e)
//		{
//			//database does't exist yet.
//		}
//
//		if(checkDB != null)
//		{
//			checkDB.close();
//		}
//
//		return checkDB != null ? true : false;
//	}
	
	
}
