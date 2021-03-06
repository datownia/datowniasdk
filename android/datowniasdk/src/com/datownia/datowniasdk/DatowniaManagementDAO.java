package com.datownia.datowniasdk;

import java.io.BufferedReader;
import java.io.CharArrayWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.releasemobile.data.Repository;
import com.releasemobile.data.RepositoryStorableContext;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

//DAO to handle management of database retreived from datownia. 
//this DAO wont distingusish between tables (aka one DAO per table) but just handle getting
//the table_def objects and handle updating the sql statements

public class DatowniaManagementDAO 
{
	private Repository repository;
	
	//table_def table
	private String TABLE_DEF_TABLE = "[table_def]";
	
	//table columns
	private String tableName = "[tablename]";
	private String sequenceNO  = "[seq]";
	
	private String[] tabledef_tableColumns = new String[]{this.tableName, this.sequenceNO};
	
	
	//constructor
	public DatowniaManagementDAO(RepositoryStorableContext context, final String databaseName, final String databaseStoragePath)
	{
		setRepository(Repository.getInstance(context, databaseName, databaseStoragePath));
	}
	
	public DatowniaManagementDAO(RepositoryStorableContext context, Repository repository)
	{
		setRepository(repository);
	}
	
	//get all table_def objects from the table_def table in database
	public List<DatowniaTableDef> getAllTableDefRecords()
	{
		List<DatowniaTableDef> tableDefs = new ArrayList<DatowniaTableDef>();
		
		SQLiteDatabase db = getRepository().getReadableDatabase();
		Cursor cursor = db.query(this.TABLE_DEF_TABLE, this.tabledef_tableColumns, null, null, null, null, null, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) 
		{
			DatowniaTableDef tableDef = new DatowniaTableDef(cursor);
			tableDefs.add(tableDef);
			cursor.moveToNext();
		}
		
		//close the cursor
		cursor.close();
		
		return tableDefs;
	}
	
	public DatowniaTableDef getTableDefRecord(String tablename)
	{
		SQLiteDatabase db = getRepository().getReadableDatabase();
		Cursor cursor = db.query(this.TABLE_DEF_TABLE, this.tabledef_tableColumns, "tablename=?", new String[]{tablename}, null, null, null, null);
		cursor.moveToFirst();
		DatowniaTableDef tableDef = null;
		while (!cursor.isAfterLast()) 
		{
			tableDef = new DatowniaTableDef(cursor);

			cursor.moveToNext();
		}
		
		//close the cursor
		cursor.close();
		
		return tableDef;
	}
	
	//updates the datownia database 
	public void updateDatowniaDataBase(String rawSQL, SQLiteDatabase db)
	{
		//if( (!rawSQL.equals("") )
		//adb shell setprop log.tag.datownia VERBOSE  if not seeing the log
		
		if (rawSQL != null)
		{
			Logger.d("datownia", rawSQL);
			db.execSQL(rawSQL);	
		}
	}

	public Boolean updateDatabase(BufferedReader buff) throws IOException {
		SQLiteDatabase db = getRepository().getWritableDatabase();
		
		
		//read characters until get to ;, then work out if we are in a '', if we are keep reading
		//this way we don't care about different types of line endings
		char[] buffer = new char[1];
		CharArrayWriter sqlBuffer = new CharArrayWriter();
		int numApostrophes = 0;
		Boolean foundUpdates = false;
		
		while(buff.read(buffer, 0, 1) > -1)
		{
			sqlBuffer.append(buffer[0]);
			
			if (buffer[0] == '\'') 
				numApostrophes++;
			
			//if we hit a ; and an even number of apostrophes then we are at a sql statement end
			if (buffer[0] == ';' && numApostrophes % 2 == 0)
			{
				//if next char is \n or \r\n then read these in as well
				char overread = 0;
				boolean wasOverread = false;
				if (buff.read(buffer, 0, 1) > -1)
				{
					if (buffer[0] == '\r')
					{
						sqlBuffer.append(buffer[0]);
						if (buff.read(buffer, 0, 1) > -1)
						{
							if (buffer[0] == '\n')
							{
								sqlBuffer.append(buffer[0]);
							}
							else
							{
								wasOverread = true;
								//we need to remember this char for next sqlbuffer
								overread = buffer[0];
							}
						}
					}
					else if (buffer[0] == '\n')
					{
						sqlBuffer.append(buffer[0]);
					}
					else
					{
						wasOverread = true;
						//we need to remember this char for next sqlbuffer
						overread = buffer[0];
					}
				}
				
				String sqlToExecute = new String(sqlBuffer.toCharArray());
				
				this.updateDatowniaDataBase(sqlToExecute, db);
				
				foundUpdates = true;
				sqlBuffer.reset();
				
				if (wasOverread)
					sqlBuffer.append(overread);
			}
		}
		
		return foundUpdates;

	}

	public Repository getRepository() {
		return repository;
	}

	private void setRepository(Repository repository) {
		this.repository = repository;
	}
	
	
	
	
}
