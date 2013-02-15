package com.datownia.datowniasdk;

import java.util.TimerTask;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class DatowniaTimerTask extends TimerTask
{
	private DatowniaAppConfiguration settings;
	private Context appContext;
	private ServiceFactory serviceFactory = new ServiceFactory();
	private DatowniaSQLiteDBHelper dbHelper;
	private ConnectivityHelper connectivityHelper;
	
	public DatowniaTimerTask(Context appContext, DatowniaAppConfiguration configSettings)
	{
		this.settings = configSettings;
		this.appContext = appContext;	
		setDbHelper(new DatowniaSQLiteDBHelper(appContext, configSettings.getDatabaseName(), configSettings.getDatabaseFolder()));
		setConnectivityHelper(new ConnectivityHelper(appContext));
	}	
	
	public DatowniaTimerTask(Context appContext, DatowniaAppConfiguration configSettings, DatowniaSQLiteDBHelper dbHelper, ConnectivityHelper connectivityHelper)
	{
		this.settings = configSettings;
		this.appContext = appContext;	
		setDbHelper(dbHelper);
		setConnectivityHelper(connectivityHelper);
		
	}
	
	@Override
	public void run() 
	{
		try
		{
			//create the app service object, which is the main entry for the rest of the datownia
			//library
			
			DatowniaAppService appService = this.getServiceFactory().createAppService(this.appContext, this.settings);

			//check if database exists at the location specified in the config settings object
			if(getDbHelper().doesDatabaseExist())
			{
				//call syncronise on the app service
				Log.d("timer task", "attempting to sync database tables");
			
				if(getConnectivityHelper().isNetworkAvailable())
					appService.synchroniseDBTables();
					Log.d("timer task", "synced database tables");
			}
			else
			{
				//phone has no database from datownia yet, so download it from datownia
				Log.d("timer task", "attempting to download database tables");
				appService.downloadAppDB();
				Log.d("timer task", "downloaded the database");
			} 
		}
		catch(Exception e)
		{
			//if datownia fails, we just want to allow the app to continue to run
			Log.d("datownia", e.toString());
		}

		

	}

	public ServiceFactory getServiceFactory() {
		return serviceFactory;
	}

	public void setServiceFactory(ServiceFactory serviceFactory) {
		this.serviceFactory = serviceFactory;
	}

	public DatowniaSQLiteDBHelper getDbHelper() {
		return dbHelper;
	}

	public void setDbHelper(DatowniaSQLiteDBHelper dbHelper) {
		this.dbHelper = dbHelper;
	}

	public ConnectivityHelper getConnectivityHelper() {
		return connectivityHelper;
	}

	public void setConnectivityHelper(ConnectivityHelper connectivityHelper) {
		this.connectivityHelper = connectivityHelper;
	}
	
	
	

}
