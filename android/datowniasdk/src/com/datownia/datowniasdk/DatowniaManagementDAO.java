package com.datownia.datowniasdk;

import java.util.ArrayList;
import java.util.List;

import com.releasemobile.data.Repository;
import com.releasemobile.data.RepositoryStorableContext;

import android.content.Context;
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
		repository = Repository.getInstance(context, databaseName, databaseStoragePath);
	}
	
	//get all table_def objects from the table_def table in database
	public List<DatowniaTableDef> getAllTableDefRecords()
	{
		List<DatowniaTableDef> tableDefs = new ArrayList<DatowniaTableDef>();
		
		SQLiteDatabase db = repository.getReadableDatabase();
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
	
	//updates the datownia database 
	public void updateDatowniaDataBase(String rawSQL)
	{
		SQLiteDatabase db = repository.getWritableDatabase();
		//if( (!rawSQL.equals("") )
		//TODO: this doesn't work
			db.execSQL(rawSQL);	
	}
	
	
	
	
}
