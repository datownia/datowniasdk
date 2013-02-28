package com.datownia.datowniasdk;

import java.io.File;

import com.releasemobile.data.Repository;
import com.releasemobile.data.RepositoryStorableContext;
import com.releasemobile.data.RepositoryStore;

import android.content.Context;
import android.content.ContextWrapper;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

/**
 * DatabaseContext will override the default database path so that databases may
 * be read from the sdcard
 * 
 */
public class DatabaseContext extends ContextWrapper implements RepositoryStorableContext {

	private RepositoryStore repositoryStore = new RepositoryStore();
	private static final String DEBUG_CONTEXT = "DatabaseContext";
	private String databasePath;

	public DatabaseContext(Context base, String databasePath) {
		super(base);
		this.databasePath = databasePath;
	}

	@Override
	public File getDatabasePath(String name) {
		// File sdcard = Environment.getExternalStorageDirectory();
		// String dbfile = sdcard.getAbsolutePath() + File.separator+
		// "databases" + File.separator + name;
		String dbfile = databasePath + File.separator + name;
		if (!dbfile.endsWith(".db")) {
			dbfile += ".db";
		}

		File result = new File(dbfile);

		if (!result.getParentFile().exists()) {
			result.getParentFile().mkdirs();
		}

		
		Logger.w(DEBUG_CONTEXT,
				"getDatabasePath(" + name + ") = "
						+ result.getAbsolutePath());


		return result;
	}

	@Override
	public SQLiteDatabase openOrCreateDatabase(String name, int mode,
			SQLiteDatabase.CursorFactory factory) {
//		SQLiteDatabase result = SQLiteDatabase.openOrCreateDatabase(
//				getDatabasePath(name), null);
//		
		SQLiteDatabase result = SQLiteDatabase.openDatabase(getDatabasePath(name).getAbsolutePath(), null, SQLiteDatabase.OPEN_READWRITE | SQLiteDatabase.CREATE_IF_NECESSARY);
		// SQLiteDatabase result = super.openOrCreateDatabase(name, mode,
		// factory);
		Logger.w(DEBUG_CONTEXT, "openOrCreateDatabase(" + name + ",,) = "
				+ result.getPath());

		return result;
	}

	@Override
	public Repository getRepository(String name) {
		return repositoryStore.getRepository(name);
	}

	@Override
	public void addRepository(String name, Repository repository) {
		repositoryStore.addRepository(name, repository);
		
	}
}