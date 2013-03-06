package com.datownia.datowniasdk.unit;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.commons.io.IOUtils;

import android.database.sqlite.SQLiteDatabase;

import com.datownia.datowniasdk.DatowniaManagementDAO;
import com.datownia.datowniasdk.testframework.DatowniaTestCase;
import com.releasemobile.data.Repository;
import static org.mockito.Mockito.*;
import junit.framework.TestCase;

public class DatowniaManagementDAOTest extends DatowniaTestCase {

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/**
	 * Test cases:
	 * 1. splits lines with windows based crlf
	 * 2. works when a line contains text with a line feed
	 * @throws IOException
	 */
	public void testUpdateDatabase() throws IOException {
		SQLiteDatabase mockDb = mock(SQLiteDatabase.class);

		Repository mockRepo = mock(Repository.class);
		when(mockRepo.getWritableDatabase()).thenReturn(mockDb);

		DatowniaManagementDAO dao = new DatowniaManagementDAO(getTestContext(), mockRepo);
		
		StringBuilder sb = new StringBuilder();
		sb.append("replace into (x) values ('blah blah blah');\r\n");
		sb.append("delete from blah;\r\n");
		sb.append("replace into (x) values ('blah blah\r\n blah');\r\n");
		
		dao.updateDatabase(getInputStreamSQL());
		
		verify(mockDb, times(3)).execSQL(anyString());

	}
	
	private BufferedReader getInputStreamSQL()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("replace into (x) values ('blah blah blah');\r\n");
		sb.append("delete from blah;\r\n");
		sb.append("replace into (x) values ('blah blah\r\n blah');\r\n");

		InputStream inputStream = IOUtils.toInputStream(sb.toString());
		
		InputStreamReader inReader = new InputStreamReader(inputStream);
		
		BufferedReader buff = new BufferedReader(inReader);
		
		return buff;

	}

}
