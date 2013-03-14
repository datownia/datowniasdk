package com.datownia.datowniasdk.integration;

import java.io.IOException;

import org.json.JSONException;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.datownia.datowniasdk.DatabaseContext;
import com.datownia.datowniasdk.DatowniaAppConfiguration;
import com.datownia.datowniasdk.DatowniaAppService;
import com.datownia.datowniasdk.testframework.DatowniaTestCase;
import com.datownia.datowniasdk.testframework.TestContext;
import com.releasemobile.data.Repository;

public class DatowniaAppServiceTest extends DatowniaTestCase{


	public DatowniaAppServiceTest()
	{
		
		
	}
	
	
	/**
	 * To be used by test class wishing to re-use e.g. concrete integration tests in datownia clients
	 * @param testContext
	 */
	public DatowniaAppServiceTest(TestContext testContext)
	{
		this.context = testContext;
	}
	
	public void testDownloadDb()
	{
		DatowniaAppConfiguration config = getConfig(getTestContext());
		doDownloadDb(config);

		doValidateDb(3, config);
	}

	public void doDownloadDb(DatowniaAppConfiguration config) {
		
		DatowniaAppService appService = new DatowniaAppService(getTestContext(), config);
		
		try {
			appService.downloadDb();
		} catch (IOException e) {
			e.printStackTrace();
			assertTrue("IOException", false);
		} catch (JSONException e) {
			e.printStackTrace();
			assertTrue("JSONException", false);
		}
		
		//check file exists
		java.io.File file = new java.io.File(config.getFullDatabasePath());
	    assertTrue(config.getFullDatabasePath(), file.exists());
	    assertEquals(config.getDatabaseName(), file.getName());
	   
	    
	}


	protected void doValidateDb(int expectedNumberOfTable, DatowniaAppConfiguration config) {
		//check it is a db by opening it and doing a query
    	DatabaseContext dbContext = new DatabaseContext(getTestContext(), config.getDatabaseFolder()); //DatabaseContext allow us to use non-standard folder for database
	    //means we can use sdcard and then it is easy to grab a copy for inspection
    	Repository repository = Repository.getInstance(dbContext, config.getDatabaseName(), config.getFullDatabasePath());

	    SQLiteDatabase db = repository.getWritableDatabase();
	    Cursor queryCursor = db.query("[table_def]", new String[]{"tablename","seq"} , null, null, null, null, null);

	    assertTrue(queryCursor.moveToFirst());
	    int rows = 1;
	    while(queryCursor.moveToNext())
	    {
	    	rows++;
	    }

	    
		assertEquals(expectedNumberOfTable, rows); //catalogue v1 and 2 + offers
	    queryCursor.close();
	    repository.close();
	}
	
	public void testSynchronizeDB()
	{
		//download the db first
		DatowniaAppConfiguration config = getConfig(getTestContext());
		doDownloadDb(config);
		
		//then synchronize it
		resetSeqNumbers(config);
		doSynchronizeDb(config);
		
	}
	
	protected void doSynchronizeDb(DatowniaAppConfiguration config) {
		
		DatabaseContext dbContext = new DatabaseContext(getTestContext(), config.getDatabaseFolder());
		DatowniaAppService appService = new DatowniaAppService(dbContext, config);
		
		try {
			appService.synchroniseDb();
		} catch (IOException e) {
			e.printStackTrace();
			assertTrue(String.format("%s. \r\n%s", e.toString(), Log.getStackTraceString(e)), false);
		} catch (JSONException e) {
			e.printStackTrace();
			assertTrue(String.format("%s. \r\n%s", e.toString(), Log.getStackTraceString(e)), false);
		}
		
		
		
	}


	void resetSeqNumbers(DatowniaAppConfiguration config) {
		//check it is a db by opening it and doing a query
    	DatabaseContext dbContext = new DatabaseContext(getTestContext(), config.getDatabaseFolder()); //DatabaseContext allow us to use non-standard folder for database
	    //means we can use sdcard and then it is easy to grab a copy for inspection
    	Repository repository = Repository.getInstance(dbContext, config.getDatabaseName(), config.getFullDatabasePath());

	    SQLiteDatabase db = repository.getWritableDatabase();
	    db.execSQL("update [table_def]  set seq=0");

	    repository.close();
	}

}
