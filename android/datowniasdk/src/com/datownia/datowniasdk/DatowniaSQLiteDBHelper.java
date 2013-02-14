package com.datownia.datowniasdk;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

public class DatowniaSQLiteDBHelper extends SQLiteOpenHelper 
{
	//singleton/ single instance reference of database instanc
	private static DatowniaSQLiteDBHelper dbHelperInstance;

	//reference of database
	private SQLiteDatabase leithsDB;
	
	private Context context;
	
	private final String DB_PATH;// = "/data/data/com.releasemobile.leithscookery/databases/";
	private final String DB_NAME;

	// constructor
	public DatowniaSQLiteDBHelper(Context context, final String databaseName, final String databaseStoragePath)
	{
		super(context, databaseName, null, 1);
		this.context = context;
		this.DB_NAME= databaseName; 
		this.DB_PATH= databaseStoragePath; 

	}


	public void initWithInputStream(InputStream stream) 
	{
		try 
		{
			this.createInputStreamDataBase(stream);
		} 
		catch (IOException ioe) 
		{
			throw new Error("Unable to create database");
		}

		try 
		{
			this.openDataBase();
		}
		catch(SQLException sqle)
		{
			throw sqle;
		}	
		
	}
	
	public void createInputStreamDataBase(InputStream stream) throws IOException
	{
		boolean dbExist = doesDatabaseExist();

		if(dbExist)
		{
			//do nothing - database already exist
		}
		else
		{
			this.getReadableDatabase();
			try 
			{
				copyDataBaseFromInputStream(stream);
			} 
			catch (IOException e)
			{
				throw new Error("Error copying database");
			}
		}
	}

	private String getFullDbpath() {
		return this.DB_PATH + this.DB_NAME;
	}
	
	private void copyDataBaseFromInputStream(InputStream inputStream) throws IOException
	{
		//Open your local db as the input stream
		InputStream myInput = inputStream;

		// Path to the just created empty db
		String outFileName = getFullDbpath();

		//Open the empty db as the output stream
		OutputStream myOutput = new FileOutputStream(outFileName);

		//transfer bytes from the inputfile to the outputfile

		byte[] buffer = new byte[1024];
		int length;

		while ((length = myInput.read(buffer))>0)
		{
			myOutput.write(buffer, 0, length);
		}

		//Close the streams
		myOutput.flush();
		myOutput.close();
		myInput.close();


	}
	
	public void openDataBase() throws SQLException
	{
		leithsDB = SQLiteDatabase.openDatabase(getFullDbpath(), null, SQLiteDatabase.OPEN_READWRITE);
	}

	//grabbing the instance of our database helper and initing it from an input stream
	public static DatowniaSQLiteDBHelper getInstance(Context context, final String databaseName, final String databaseStoragePath) 
	{
		if (dbHelperInstance == null) 
		{
			dbHelperInstance = new DatowniaSQLiteDBHelper(context, databaseName, databaseStoragePath);
		}

		return dbHelperInstance;
	}
	
	

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}
	
	
	public boolean doesDatabaseExist()
	{
		java.io.File file = new java.io.File(getFullDbpath());
	    return file.exists();
	}

}
