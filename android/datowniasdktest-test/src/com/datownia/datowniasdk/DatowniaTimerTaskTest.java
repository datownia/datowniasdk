package com.datownia.datowniasdk;

import com.datownia.datowniasdk.DatowniaAppConfiguration;
import com.datownia.datowniasdk.DatowniaAppService;
import com.datownia.datowniasdk.DatowniaTimerTask;
import com.datownia.datowniasdk.ServiceFactory;

import android.content.Context;
import static org.mockito.Mockito.*;
import android.test.AndroidTestCase;

public class DatowniaTimerTaskTest extends AndroidTestCase {

	protected void setUp() throws Exception {
		System.setProperty("dexmaker.dexcache", "/sdcard");
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testRun() {
		//create timer task with datownia configuration
		DatowniaAppConfiguration config = new DatowniaAppConfiguration();
		config.setPublisher("example");
		config.setAppKey("b317eac00b");
		config.setAppSecret("5156a8e80e");
		config.setHost("www.datownia.com");
		config.setCheckChangesFrequency(300);
		config.setPhoneDatabaseName("exampledb");
		config.setPhoneDatabasePath("/temp/database/");
		
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
		DatowniaTimerTask timerTask = new DatowniaTimerTask(getContext(), config);
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
