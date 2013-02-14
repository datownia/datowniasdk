package com.datownia.datowniasdk.integration;

import java.io.IOException;

import org.json.JSONException;

import com.datownia.datowniasdk.DatowniaAppService;
import com.datownia.datowniasdk.testframework.DatowniaTestCase;

import android.content.Context;
import static org.mockito.Mockito.*;
import android.test.AndroidTestCase;

public class DatowniaAppServiceTest extends DatowniaTestCase{


	public void testDownloadAppDB() throws IOException, JSONException
	{
		DatowniaAppService appService = new DatowniaAppService(getContext(), getConfig(getContext()));
		
		appService.downloadAppDB();
		
		fail("write assertions. check that file exists. check that it is indeed a database");
	}
}
