package com.releasemobile.data;

import java.util.HashMap;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

public class DatabaseApplication extends Application implements RepositoryStorableContext {
	
//	private RepositoryStore repositoryStore;
	private HashMap<String, Repository> repoMap = new HashMap<String, Repository>();
	
	@Override
	public void addRepository(String name, Repository repository)
	{
		repoMap.put(name, repository);
	}

	@Override
	public Repository getRepository(String name)
	{
		//attempt to get Repository out of the map
		return repoMap.get(name);
	}
	
//	private String databasePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "databases";
//	@Override
//	public File getDatabasePath(String name) {
//		// File sdcard = Environment.getExternalStorageDirectory();
//		// String dbfile = sdcard.getAbsolutePath() + File.separator+
//		// "databases" + File.separator + name;
//		String dbfile = databasePath + File.separator + name;
//		if (!dbfile.endsWith(".db")) {
//			dbfile += ".db";
//		}
//
//		File result = new File(dbfile);
//
//		if (!result.getParentFile().exists()) {
//			result.getParentFile().mkdirs();
//		}
//
//		
//		Logger.d("database",
//				"getDatabasePath(" + name + ") = "
//						+ result.getAbsolutePath());
//
//
//		return result;
//	}
//	
//	@Override
//	public SQLiteDatabase openOrCreateDatabase(String name, int mode,
//			SQLiteDatabase.CursorFactory factory) {
//		
//		return super.openOrCreateDatabase(name, mode, factory);
////		Logger.d("database", "openOrCreateDatabase start(" + name + ",,) = ");
////		SQLiteDatabase result = SQLiteDatabase.openDatabase(getDatabasePath(name).getAbsolutePath(), null, SQLiteDatabase.OPEN_READWRITE | SQLiteDatabase.CREATE_IF_NECESSARY);
////
////		Logger.d("database", "openOrCreateDatabase done (" + name + ",,) = "
////				+ result.getPath());
////
////		return result;
//	}

}
