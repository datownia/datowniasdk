package com.datownia.datowniasdk.oauth2.unit;

import java.util.Calendar;
import java.util.Locale;

import com.datownia.datowniasdk.oauth2.DatowniaAccessToken;
import com.releasemobile.toolkit.DateHelper;

import junit.framework.TestCase;

public class DatowniaAccessTokenTest extends TestCase {

	public void testGetExpiryDate() {
		Calendar created = Calendar.getInstance(Locale.ROOT);
		created.set(2013, 0, 26, 14, 54, 30);
		DatowniaAccessToken token = new DatowniaAccessToken("token", 7200, "testtype", created);
		
		Calendar expires = DateHelper.add(created, Calendar.HOUR, 2);
		expires.add(Calendar.SECOND, -DatowniaAccessToken.EXPIRY_TOLERANCE);

		assertEquals(expires, token.getExpiryDate());
		
	}

	/**
	 * Test cases:
	 * 1. expiry is in the past
	 * 2. expiry is in the future
	 */
	public void testIsExpired() {
		//Test case 1. expiry in past
		Calendar inPast = Calendar.getInstance(Locale.ROOT);
		inPast.set(2013, 0, 26, 14, 54, 30);
		DatowniaAccessToken token = new DatowniaAccessToken("token", 7200, "testtype", inPast);

		assertTrue(token.isExpired());
		
		//Test case 2. expiry in future
		Calendar now = Calendar.getInstance(Locale.ROOT);

		token = new DatowniaAccessToken("token", 7200, "testtype", now);
		
		assertFalse(token.isExpired());
	}

}
