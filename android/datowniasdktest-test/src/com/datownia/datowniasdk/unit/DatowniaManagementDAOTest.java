package com.datownia.datowniasdk.unit;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.commons.io.IOUtils;
import org.mockito.InOrder;

import android.database.sqlite.SQLiteDatabase;

import com.datownia.datowniasdk.DatowniaManagementDAO;
import com.datownia.datowniasdk.testframework.DatowniaTestCase;
import com.releasemobile.data.Repository;
import static org.mockito.Mockito.*;

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
		InOrder inOrder = inOrder(mockDb);

		Repository mockRepo = mock(Repository.class);
		when(mockRepo.getWritableDatabase()).thenReturn(mockDb);

		DatowniaManagementDAO dao = new DatowniaManagementDAO(getTestContext(), mockRepo);
		
		String sql1 = "replace into (x) values ('blah blah blah');\n";
		String sql2 = "delete from blah;\n";
		String sql3 = "replace into (x) values ('blah blah\r\n blah');\r\n";
		String sql4 = "replace into (x) values ('this ;\nis what I want to avoid failing on');\n";
		String sql5 = "replace into (x, y) values ('this', 'this ;\nalso this');";
		
		StringBuilder sb = new StringBuilder();
		sb.append(sql1);
		sb.append(sql2);
		sb.append(sql3);
		sb.append(sql4);
		sb.append(sql5);
		
		dao.updateDatabase(getInputStreamSQL(sb.toString()));
		
		//verify we get the expected sql statements fired at the db
		inOrder.verify(mockDb).execSQL(sql1);
		inOrder.verify(mockDb).execSQL(sql2);
		inOrder.verify(mockDb).execSQL(sql3);
		inOrder.verify(mockDb).execSQL(sql4);
		inOrder.verify(mockDb).execSQL(sql5);
		verify(mockDb, times(5)).execSQL(anyString());

	}
	
	private BufferedReader getInputStreamSQL(String sql)
	{

		InputStream inputStream = IOUtils.toInputStream(sql);
		
		InputStreamReader inReader = new InputStreamReader(inputStream);
		
		BufferedReader buff = new BufferedReader(inReader);
		
		return buff;

	}

}
