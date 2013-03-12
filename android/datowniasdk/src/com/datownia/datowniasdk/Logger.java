package com.datownia.datowniasdk;

import android.util.Log;

/**
 * Log wrapper to make android logging code a bit less cumbersome
 * 
 */
public class Logger {
	public static void d(String tag, String msg) {
		if (Log.isLoggable(tag, Log.DEBUG)) {
			Log.d(tag, msg);
		}
	}
	
	public static void d(String tag, String formatString, Object... args) {
		if (Log.isLoggable(tag, Log.DEBUG)) {
			Log.d(tag, String.format(formatString, args));
		}
	}

	public static void i(String tag, String msg) {
		if (Log.isLoggable(tag, Log.INFO)) {
			Log.i(tag, msg);
		}
	}

	public static void i(String tag, String formatString, Object... args) {
		if (Log.isLoggable(tag, Log.INFO)) {
			Log.i(tag, String.format(formatString, args));
		}
	}
	
	public static void e(String tag, String msg) {
		if (Log.isLoggable(tag, Log.ERROR)) {
			Log.e(tag, msg);
		}
	}
	
	public static void e(String tag, String formatString, Object... args) {
		if (Log.isLoggable(tag, Log.ERROR)) {
			Log.e(tag, String.format(formatString, args));
		}
	}

	public static void v(String tag, String msg) {
		if (Log.isLoggable(tag, Log.VERBOSE)) {
			Log.v(tag, msg);
		}
	}
	
	public static void v(String tag, String formatString, Object... args) {
		if (Log.isLoggable(tag, Log.VERBOSE)) {
			Log.v(tag, String.format(formatString, args));
		}
	}

	public static void w(String tag, String msg) {
		if (Log.isLoggable(tag, Log.WARN)) {
			Log.w(tag, msg);
		}
	}
	
	public static void w(String tag, String formatString, Object... args) {
		if (Log.isLoggable(tag, Log.WARN)) {
			Log.w(tag, String.format(formatString, args));
		}
	}
}
