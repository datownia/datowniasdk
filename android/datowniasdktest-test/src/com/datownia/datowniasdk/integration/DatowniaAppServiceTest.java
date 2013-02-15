package com.datownia.datowniasdk.integration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.json.JSONException;

import com.datownia.datowniasdk.DatabaseContext;
import com.datownia.datowniasdk.DatowniaAppConfiguration;
import com.datownia.datowniasdk.DatowniaAppService;
import com.datownia.datowniasdk.DatowniaSQLiteDBHelper;
import com.datownia.datowniasdk.testframework.DatowniaTestCase;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import static org.mockito.Mockito.*;
import android.os.Environment;
import android.test.AndroidTestCase;

public class DatowniaAppServiceTest extends DatowniaTestCase{


	public void testDownloadAppDB() throws IOException, JSONException
	{
		DatowniaAppConfiguration config = getConfig(getContext());
		DatowniaAppService appService = new DatowniaAppService(getContext(), config);
		
		appService.downloadAppDB();
		
		//check file exists
		java.io.File file = new java.io.File(config.getFullDatabasePath());
	    assertTrue(config.getFullDatabasePath(), file.exists());
	    assertEquals(config.getDatabaseName(), file.getName());
	   
	    //check it is a db by opening it and doing a query
	    DatabaseContext dbContext = new DatabaseContext(getContext(), config.getDatabaseFolder()); //DatabaseContext allow us to use non-standard folder for database
	    //means we can use sdcard and then it is easy to grab a copy for inspection
	    SQLiteOpenHelper dbHelper = DatowniaSQLiteDBHelper.getInstance(dbContext, config.getDatabaseName(), config.getDatabaseFolder());
	    
	    SQLiteDatabase db = dbHelper.getReadableDatabase();
	    
	    Cursor queryCursor = db.query("[table_def]", new String[]{"tablename","seq"} , null, null, null, null, null);
	    //Cursor queryCursor = db.rawQuery("select * from [table_def]", null);
	    queryCursor.moveToLast();
	    queryCursor.close();
	    dbHelper.close();

	}

}
