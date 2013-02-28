package com.datownia.datowniasdk.testframework;

import java.io.File;

import com.datownia.datowniasdk.DatowniaAppConfiguration;

import android.content.Context;
import android.os.Environment;
import android.test.AndroidTestCase;

public class DatowniaTestCase extends AndroidTestCase {
	
	protected void setUp() throws Exception {
		System.setProperty("dexmaker.dexcache", "/sdcard");
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	protected DatowniaAppConfiguration getConfig(Context context) {
		DatowniaAppConfiguration config = new DatowniaAppConfiguration();
		config.setPublisher("example.db");
		config.setAppKey("b317eac00b");
		config.setAppSecret("5156a8e80e");
		config.setHost("www.datownia.com");
		config.setCheckChangesFrequency(300);
		config.setDatabaseName("exampledatownia");
		//config.setPhoneDatabasePath(context.getApplicationInfo().dataDir);
		//we use sdcard for the tests so that we can inspect the sqlite file more easily when debugging
		config.setFullDatabasePath(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "databases");
		return config;
	}
	
	private TestContext context = null;
	public TestContext getTestContext()
	{
		if (context == null)
			context =  new TestContext(getContext());
		
		return context;
	}

}
