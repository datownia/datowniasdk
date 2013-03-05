package com.datownia.datowniasdk.unit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import com.datownia.datowniasdk.DaoFactory;
import com.datownia.datowniasdk.DatowniaAppConfiguration;
import com.datownia.datowniasdk.DatowniaAppService;
import com.datownia.datowniasdk.DatowniaManagementDAO;
import com.datownia.datowniasdk.DatowniaTableDef;
import com.datownia.datowniasdk.testframework.DatowniaTestCase;
import com.datownia.datowniasdk.testframework.TestContext;
import com.releasemobile.data.RepositoryStorableContext;

import android.content.Context;
import static org.mockito.Mockito.*;
import android.test.AndroidTestCase;
import android.test.IsolatedContext;
import android.util.Log;

public class DatowniaAppServiceTest extends DatowniaTestCase{

	/**
	 * test cases
		1. replace into
		2. delete
		3. replace into containing a line feed
		4. multiple publishers in the app, some with the same table names
	 */
	public void testSynchronizeDB()
	{
		TestContext testContext = new TestContext(new IsolatedContext(null, getContext()));
		
		//create mock dao that returns our test data
		DatowniaManagementDAO mockDao = mock(DatowniaManagementDAO.class);
		when(mockDao.getAllTableDefRecords()).thenReturn(getTestTableDef());
		
		//create mock factory that returns the mock dao
		DaoFactory daoFactory = mock(DaoFactory.class);
		when(daoFactory.getDatowniaDao(any(RepositoryStorableContext.class), any(DatowniaAppConfiguration.class))).thenReturn(mockDao);
		
		//create app service and call synchronise
		DatowniaAppService appService = new DatowniaAppService(testContext, getConfig(testContext), daoFactory);
		
		try {
			appService.synchroniseDb();
		} catch (IOException e) {
			e.printStackTrace();
			assertTrue(String.format("%s. \r\n%s", e.toString(), Log.getStackTraceString(e)), false);
		} catch (JSONException e) {
			e.printStackTrace();
			assertTrue(String.format("%s. \r\n%s", e.toString(), Log.getStackTraceString(e)), false);
		}
		
		
	}
	
	private List<DatowniaTableDef> getTestTableDef()
	{
		List<DatowniaTableDef> result = new ArrayList<DatowniaTableDef>();
		result.add(new DatowniaTableDef("publisher1/table1/1.0", 0));
		result.add(new DatowniaTableDef("publisher1/table1/2.0", 0));
		result.add(new DatowniaTableDef("publisher1/table2/1.0", 1));
		result.add(new DatowniaTableDef("publisher2/table1/1.0", 0));
		result.add(new DatowniaTableDef("publisher2/table3/1.0", 0));
		
		return result;
	}


}
