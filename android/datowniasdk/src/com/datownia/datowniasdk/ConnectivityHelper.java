package com.datownia.datowniasdk;

import com.releasemobile.data.RepositoryStorableContext;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ConnectivityHelper {
	
	private RepositoryStorableContext context;

	public ConnectivityHelper(RepositoryStorableContext context)
	{
		this.context = context;
	}

	public boolean isNetworkAvailable() 
	{
	    ConnectivityManager connectivityManager  = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	    
	    if(activeNetworkInfo.isConnected())
	    	return true;
	    else
	    	return false;
	    	
	    	
	  //  return activeNetworkInfo != null;
	}
	
}
