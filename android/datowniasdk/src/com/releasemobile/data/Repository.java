package com.releasemobile.data;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;

import com.datownia.datowniasdk.Logger;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class Repository extends SQLiteOpenHelper {
	private static final int VERSION = 1;
	private String databaseName;
	private File databaseFile;
	
	private Context mContext;
	
	/**
	 * number of users of the database connection.
	 * */
	private int mOpenConnections = 0;
	
	/**
	 * gets an instance of a repository for the supplied database name using a supplied path
	 * @param context
	 * @param name name of the database file
	 * @param path full path to the database file
	 * @return
	 */
	synchronized static public Repository getInstance(RepositoryStorableContext context, String name, String path) {
		Repository repo = context.getRepository(name);
		
		if (repo == null)
		{
			repo = new Repository((Context)context, name, path);
			context.addRepository(name, repo);
		}
		
		return repo;
	}
	
	/**
	 * gets an instance of a repository for the supplied database name using the default path
	 * @param context
	 * @param name name of the database file
	 * @return
	 */
	synchronized static public Repository getInstance(RepositoryStorableContext context, String name) {
		Repository repo = context.getRepository(name);
		
		if (repo == null)
		{
			repo = new Repository((Context)context, name);
			context.addRepository(name, repo);
		}
		
		return repo;
	}
	
	/**
	 * Creates a Repository and registers it in the context. Should do this on application create usually
	 * @param context
	 * @param name
	 * @param path
	 */
	synchronized static public void register(RepositoryStorableContext context, String name, String path) {
		getInstance(context, name, path);
	}
	
	/**
	 * Creates a Repository and registers it in the context. Should do this on application create usually
	 * @param context
	 * @param name
	 */
	synchronized static public void register(RepositoryStorableContext context, String name) {
		getInstance(context, name);
	}
	
	/**
	 * creates an instance of a repository for the supplied database name using the default path
	 * @param context
	 * @param name
	 */
	private Repository(Context context, String name) {
		super(context, name, null, VERSION);
		this.mContext = context;
		this.databaseName = name;
		SQLiteDatabase db = null;
		try {
			databaseFile = context.getDatabasePath(databaseName);
			
			if (!doesDatabaseExist()) {
				copyDatabase();
			}
			
			db =  getReadableDatabase();
			if (db != null) {
		  		db.close();
			}

		} catch (SQLiteException e) {
			Logger.w("database", "Repository ctor failed. Exception: %s", Log.getStackTraceString(e));
		} finally {
			if (db != null && db.isOpen()) {
				db.close();
			}
		}
	}
	
	/**
	 * creates an instance of a repository for the supplied database name using a supplied path
	 * @param context
	 * @param name
	 * @param path
	 */
	private Repository(Context context, String name, String path) {
		super(context, name, null, VERSION);
		this.mContext = context;
		this.databaseName = name;
		databaseFile = new File(path);

	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase database,
		int old_version, int new_version) {
	}
	
	@Override
	public synchronized void onOpen(SQLiteDatabase db) {
		super.onOpen(db);
		// increment the number of users of the database connection.
		mOpenConnections++;
		if (!db.isReadOnly()) {
			// Enable foreign key constraints
			db.execSQL("PRAGMA foreign_keys=ON;");
		}
	}
	
	/**
	 * implementation to avoid closing the database connection while it is in 
	 * use by others.
	 */
	@Override
	public synchronized void close() {
		mOpenConnections--;
		if (mOpenConnections == 0) {
			super.close();
		}
	}
	
	public boolean doesDatabaseExist()
	{
	    return this.databaseFile.exists();
	}
	
	private void copyDatabase() {
		AssetManager assetManager = mContext.getResources().getAssets();
		InputStream in = null;
		OutputStream out = null;
		try {
			in = assetManager.open(databaseName);
			
			//make sure the directory exists
			databaseFile.getParentFile().mkdirs();

			out = new FileOutputStream(databaseFile);
			IOUtils.copy(in, out);
		} catch (IOException e) {
			Logger.w("database", "copyDatabase failed. Exception: %s", Log.getStackTraceString(e));
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) { Logger.w("database", "copyDatabase failed. Exception: %s", Log.getStackTraceString(e)); }
			}
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) { Logger.w("database", "copyDatabase failed. Exception: %s", Log.getStackTraceString(e)); }
			}
		}
		setDatabaseVersion();
		
		
	}
	
	private void setDatabaseVersion() {
		SQLiteDatabase db = null;
		try {
			Logger.d("database", "setDatabaseVersion about to open %s", databaseFile.getAbsolutePath());
			//db = SQLiteDatabase.openDatabase(databaseFile.getAbsolutePath(), null, SQLiteDatabase.OPEN_READWRITE);
			db = getWritableDatabase();
			
			Logger.d("database", "setDatabaseVersion opened %s", databaseFile.getAbsolutePath());
			db.execSQL("PRAGMA user_version = " + VERSION);
		} catch (SQLiteException e ) {
			Logger.w("database", "setDatabaseVersion failed. Exception: %s", Log.getStackTraceString(e));
			
		} finally {
			if (db != null && db.isOpen()) {
		  		db.close();
			}
		}
	}
}