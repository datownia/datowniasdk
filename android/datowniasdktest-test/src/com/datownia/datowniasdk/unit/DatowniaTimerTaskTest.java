package com.datownia.datowniasdk.unit;

import java.io.IOException;

import org.json.JSONException;

import com.datownia.datowniasdk.ConnectivityHelper;
import com.datownia.datowniasdk.DatowniaAppConfiguration;
import com.datownia.datowniasdk.DatowniaAppService;
import com.datownia.datowniasdk.DatowniaSQLiteDBHelper;
import com.datownia.datowniasdk.DatowniaTimerTask;
import com.datownia.datowniasdk.ServiceFactory;
import com.datownia.datowniasdk.testframework.DatowniaTestCase;

import android.content.Context;
import static org.mockito.Mockito.*;
import android.test.AndroidTestCase;

public class DatowniaTimerTaskTest extends DatowniaTestCase {


	/**
	 * We want to test that a timer task calls appropriate AppService methods
	 * depending on the state of the database and the connection
	 * @throws JSONException 
	 * @throws IOException 
	 */
	public void testRun() throws IOException, JSONException {

		//create mock app service
		DatowniaAppService appServiceMock = mock(DatowniaAppService.class);
		
		//create mock factory to return the service
		ServiceFactory serviceFactoryMock = mock(ServiceFactory.class);
		when(serviceFactoryMock.createAppService(any(Context.class), any(DatowniaAppConfiguration.class))).thenReturn(appServiceMock);
		
		//mock db helper and ConnectivityHelper
		DatowniaSQLiteDBHelper dbHelperMock = mock(DatowniaSQLiteDBHelper.class);
		ConnectivityHelper connectivityHelperMock = mock(ConnectivityHelper.class);
		when(connectivityHelperMock.isNetworkAvailable()).thenReturn(true);
		
		//run. this should create an app service and initialize with the configuration
		//then it should call downloadAppDb
		DatowniaTimerTask timerTask = new DatowniaTimerTask(getContext(), getConfig(getContext()));
		timerTask.setServiceFactory(serviceFactoryMock);
		timerTask.setDbHelper(dbHelperMock);
		timerTask.setConnectivityHelper(connectivityHelperMock);
		
		when(dbHelperMock.doesDatabaseExist()).thenReturn(false); //no db first time around
		timerTask.run();
		verify(appServiceMock, times(1)).downloadAppDB();
		verify(appServiceMock, never()).synchroniseDBTables();
		
		//run again. this time is should create an app service and call sync
		when(dbHelperMock.doesDatabaseExist()).thenReturn(true); //now there is a db
		timerTask.run();
		verify(appServiceMock, times(1)).downloadAppDB();
		verify(appServiceMock, times(1)).synchroniseDBTables();
		
		//now make network unavailable. should not call synchronise again
		when(connectivityHelperMock.isNetworkAvailable()).thenReturn(false);
		timerTask.run();
		verify(appServiceMock, times(1)).downloadAppDB();
		verify(appServiceMock, times(1)).synchroniseDBTables();
		
	}

}
