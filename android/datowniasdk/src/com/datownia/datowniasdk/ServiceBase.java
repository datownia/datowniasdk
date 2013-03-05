package com.datownia.datowniasdk;

import com.datownia.datowniasdk.oauth2.OAuth2Client;
import com.releasemobile.data.RepositoryStorableContext;

public class ServiceBase 
{
	protected DatowniaAppConfiguration configurationSettings;
	protected RepositoryStorableContext applicationContext;
	protected OAuth2Client oauth2Client;
	
	public ServiceBase(RepositoryStorableContext appContext, DatowniaAppConfiguration configurationSettings)
	{
		this.applicationContext = appContext;
		this.configurationSettings = configurationSettings;
		this.oauth2Client = new OAuth2Client(appContext.getApplicationContext(), configurationSettings);
	}
	
	public ServiceBase(RepositoryStorableContext appContext, DatowniaAppConfiguration configurationSettings, OAuth2Client oauth2Client)
	{
		this.applicationContext = appContext;
		this.configurationSettings = configurationSettings;
		this.oauth2Client = oauth2Client;
	}
	
	public ServiceBase()
	{
		
	}		

	
}
