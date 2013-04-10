package com.datownia.datowniasdk.unit;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;

import com.datownia.datowniasdk.ConnectionFactory;
import com.datownia.datowniasdk.DaoFactory;
import com.datownia.datowniasdk.DatowniaAppConfiguration;
import com.datownia.datowniasdk.DatowniaAppService;
import com.datownia.datowniasdk.DatowniaManagementDAO;
import com.datownia.datowniasdk.DatowniaTableDef;
import com.datownia.datowniasdk.oauth2.DatowniaAccessToken;
import com.datownia.datowniasdk.oauth2.OAuth2Client;
import com.datownia.datowniasdk.testframework.DatowniaTestCase;
import com.datownia.datowniasdk.testframework.TestContext;
import com.releasemobile.data.Repository;
import com.releasemobile.data.RepositoryStorableContext;

import static org.mockito.Mockito.*;
import android.util.Log;

public class DatowniaAppServiceTest extends DatowniaTestCase{

	/**
	 * test cases
		1. replace into
		2. delete
		3. replace into containing a line feed
		4. multiple publishers in the app, some with the same table names
	 * @throws JSONException 
	 * @throws IOException 
	 */
	public void testSynchronizeDB() throws IOException, JSONException
	{
		//TestContext testContext = new TestContext(new IsolatedContext(null, getContext()));//not sure if need IsolatedContext anymore
		TestContext context = getTestContext();
		
		//create mock dao that returns our test data and uses a mock repo to perform updates
		Repository mockRepo = mock(Repository.class);
		DatowniaManagementDAO mockDao = mock(DatowniaManagementDAO.class);
		when(mockDao.getAllTableDefRecords()).thenReturn(getTestTableDef());
		when(mockDao.getRepository()).thenReturn(mockRepo);
		
		//create mock factory that returns the mock dao
		DaoFactory daoFactory = mock(DaoFactory.class);
		when(daoFactory.getDatowniaDao(any(RepositoryStorableContext.class), any(DatowniaAppConfiguration.class))).thenReturn(mockDao);
		
		//dummy auth token
		DatowniaAccessToken dummyToken = new DatowniaAccessToken("atoken", 7200, "tokenType");
		
		//create mock oauth2Client
		OAuth2Client mockOAuth2Client = mock(OAuth2Client.class);
		when(mockOAuth2Client.getAccessToken(anyString())).thenReturn(dummyToken);
		
		//create mock HttpUrlConnection and factory to return it
		HttpURLConnection mockConnection = mock(HttpURLConnection.class);
		when(mockConnection.getResponseCode()).thenReturn(200);
		when(mockConnection.getInputStream()).thenReturn(getInputStreamSQL());
		
		ConnectionFactory connectionFactory = mock(ConnectionFactory.class);
		when(connectionFactory.getConnection(any(URL.class))).thenReturn(mockConnection);
		
		//create app service and call synchronise
		DatowniaAppService appService = new DatowniaAppService(context, getConfig(context), daoFactory, mockOAuth2Client, connectionFactory);
		
		try {
			appService.synchroniseDb();
		} catch (IOException e) {
			e.printStackTrace();
			assertTrue(String.format("%s. \r\n%s", e.toString(), Log.getStackTraceString(e)), false);
		} catch (JSONException e) {
			e.printStackTrace();
			assertTrue(String.format("%s. \r\n%s", e.toString(), Log.getStackTraceString(e)), false);
		}
		
		//check we got 3 lines of sql fired at the db
		verify(mockDao, atLeastOnce()).updateDatabase(any(BufferedReader.class));
//		verify(mockDao, times(3)).updateDatowniaDataBase(anyString());
		
	}
	
	private InputStream getInputStreamSQL()
	{
		
		StringBuilder sb = new StringBuilder();
		sb.append("replace into (x) values ('blah blah blah');\r\n");
		sb.append("delete from blah;\r\n");
		sb.append("replace into (x) values ('blah blah\r\n blah');\r\n");

		return IOUtils.toInputStream(sb.toString());
	}
	
	private List<DatowniaTableDef> getTestTableDef()
	{
		List<DatowniaTableDef> result = new ArrayList<DatowniaTableDef>();
		result.add(new DatowniaTableDef("publisher1/table1_1.0", 0));
		result.add(new DatowniaTableDef("publisher1/table1_2.0", 0));
		result.add(new DatowniaTableDef("publisher1/table2_1.0", 1));
		result.add(new DatowniaTableDef("publisher2/table1_1.0", 0));
		result.add(new DatowniaTableDef("publisher2/table3_1.0", 0));
		
		return result;
	}


}
