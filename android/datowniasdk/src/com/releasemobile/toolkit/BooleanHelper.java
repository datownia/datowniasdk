package com.releasemobile.toolkit;

public class BooleanHelper {
	/**
	 * @param value
	 * @return true if value == "true" or value == "1"
	 */
	public static boolean parseBoolean(String value)
	{
		return Boolean.parseBoolean(value) || value.equals("1");
	}
}
