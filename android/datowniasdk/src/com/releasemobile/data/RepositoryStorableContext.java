package com.releasemobile.data;

import java.io.File;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public interface RepositoryStorableContext {
	
	public abstract Repository getRepository(String name);

	public abstract void addRepository(String name, Repository repository);
	
	//standard context methods
	public abstract Context getApplicationContext ();
	public abstract Object getSystemService (String name);

	public abstract File getDatabasePath(String name);
	
	public abstract SQLiteDatabase openOrCreateDatabase(String name, int mode,
			SQLiteDatabase.CursorFactory factory);
}
