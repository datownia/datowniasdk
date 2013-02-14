package com.datownia.datowniasdk;

import android.database.Cursor;

//simple class to hold info from tabledef table when downloaded from datownia

public class DatowniaTableDef 
{
	private String tableName;
	private int seqNo;
	
	public DatowniaTableDef()
	{
		
	}
	
	public DatowniaTableDef(Cursor cursor)
	{
		this.tableName = cursor.getString(0);	
		this.seqNo = cursor.getInt(1);
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public int getSeqNo() {
		return seqNo;
	}

	public void setSeqNo(int seqNo) {
		this.seqNo = seqNo;
	}

}
