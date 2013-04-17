package com.datownia.datowniasdk.testframework;

import java.io.File;

import com.datownia.datowniasdk.DatowniaAppConfiguration;

import android.content.Context;
import android.os.Environment;
import android.test.AndroidTestCase;

public class DatowniaTestCase extends AndroidTestCase {
	
	protected void setUp() throws Exception {
		System.setProperty("dexmaker.dexcache", Environment.getExternalStorageDirectory().getPath());
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	protected DatowniaAppConfiguration getConfig(Context context) {
		DatowniaAppConfiguration config = new DatowniaAppConfiguration(context);
		config.setPublisher("example");
		config.setAppKey("04686c430e");
		config.setAppSecret("812e890302");
		config.setHost("www.datownia.com");
		config.setCheckChangesFrequency(300);
		config.setDatabaseName("exampledatownia.db");
		config.setLimit(3);
		//config.setPhoneDatabasePath(context.getApplicationInfo().dataDir);
		//we use sdcard for the tests so that we can inspect the sqlite file more easily when debugging
		//config.setDatabasePath(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "databases");
		return config;
	}
	
	protected TestContext context = null;
	public TestContext getTestContext()
	{
		if (context == null)
			context =  new TestContext(getContext());
		
		return context;
	}

}
