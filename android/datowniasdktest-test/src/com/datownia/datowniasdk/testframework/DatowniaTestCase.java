package com.datownia.datowniasdk.testframework;

import com.datownia.datowniasdk.DatowniaAppConfiguration;

import android.content.Context;
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
		config.setPublisher("example");
		config.setAppKey("b317eac00b");
		config.setAppSecret("5156a8e80e");
		config.setHost("www.datownia.com");
		config.setCheckChangesFrequency(300);
		config.setPhoneDatabaseName("exampledatowniadb");
		config.setPhoneDatabasePath(context.getApplicationInfo().dataDir);
		return config;
	}

}
