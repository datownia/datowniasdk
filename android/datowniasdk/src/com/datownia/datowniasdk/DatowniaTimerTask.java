package com.datownia.datowniasdk;

import java.util.TimerTask;

import com.releasemobile.data.Repository;
import com.releasemobile.data.RepositoryStorableContext;

import android.util.Log;

public class DatowniaTimerTask extends TimerTask
{
	private DatowniaAppConfiguration settings;
	private RepositoryStorableContext appContext;
	private ServiceFactory serviceFactory = new ServiceFactory();
	private Repository repository;
	private ConnectivityHelper connectivityHelper;
	
	public DatowniaTimerTask(RepositoryStorableContext appContext, DatowniaAppConfiguration configSettings)
	{
		this.settings = configSettings;
		this.appContext = appContext;	
		this.repository = Repository.getInstance(appContext,  configSettings.getDatabaseName(), configSettings.getFullDatabasePath());
//		setDbHelper(new DatowniaSQLiteDBHelper(appContext, configSettings.getDatabaseName(), configSettings.getDatabaseFolder()));
		setConnectivityHelper(new ConnectivityHelper(appContext));
	}	
	
	public DatowniaTimerTask(RepositoryStorableContext appContext, DatowniaAppConfiguration configSettings, Repository repository, ConnectivityHelper connectivityHelper)
	{
		this.settings = configSettings;
		this.appContext = appContext;	
		this.repository = repository;

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
			if(repository.doesDatabaseExist())
			{
				//call syncronise on the app service
				Logger.d("timertask", "attempting to sync database tables");
			
				if(getConnectivityHelper().isNetworkAvailable())
					appService.synchroniseDb();
					Logger.d("timertask", "synced database tables");
			}
			else
			{
				//phone has no database from datownia yet, so download it from datownia
				Logger.d("timertask", "attempting to download database tables");
				appService.downloadDb();
				Logger.d("timertask", "downloaded the database");
			} 
		}
		catch(Exception e)
		{
			//if datownia fails, we just want to allow the app to continue to run
			Log.d("timertask", String.format("App service failed. %s. %s", e.toString(), Log.getStackTraceString(e)));
		}

		

	}

	public ServiceFactory getServiceFactory() {
		return serviceFactory;
	}

	public void setServiceFactory(ServiceFactory serviceFactory) {
		this.serviceFactory = serviceFactory;
	}

	public Repository getRepository() {
		return repository;
	}

	public void setRepository(Repository repository) {
		this.repository = repository;
	}

	public ConnectivityHelper getConnectivityHelper() {
		return connectivityHelper;
	}

	public void setConnectivityHelper(ConnectivityHelper connectivityHelper) {
		this.connectivityHelper = connectivityHelper;
	}
	
	
	

}
