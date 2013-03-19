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
			
			logMethodAndLine(tag);
		}
	}

	
	public static void d(String tag, String formatString, Object... args) {
		if (Log.isLoggable(tag, Log.DEBUG)) {
			Log.d(tag, String.format(formatString, args));
			
			logMethodAndLine(tag);
		}
	}

	public static void i(String tag, String msg) {
		if (Log.isLoggable(tag, Log.INFO)) {
			Log.i(tag, msg);
			
			logMethodAndLine(tag);
		}
	}
	
	public static void i(String tag, String formatString, Object... args) {
		if (Log.isLoggable(tag, Log.INFO)) {
			Log.i(tag, String.format(formatString, args));
			
			logMethodAndLine(tag);
		}
	}
	
	public static void e(String tag, String msg) {
		if (Log.isLoggable(tag, Log.ERROR)) {
			Log.e(tag, msg);
			
			logMethodAndLine(tag);
		}
	}
	
	public static void e(String tag, String formatString, Object... args) {
		if (Log.isLoggable(tag, Log.ERROR)) {
			Log.e(tag, String.format(formatString, args));
			
			logMethodAndLine(tag);
		}
	}

	public static void v(String tag, String msg) {
		if (Log.isLoggable(tag, Log.VERBOSE)) {
			Log.v(tag, msg);
			
			logMethodAndLine(tag);
		}
	}
	
	public static void v(String tag, String formatString, Object... args) {
		if (Log.isLoggable(tag, Log.VERBOSE)) {
			Log.v(tag, String.format(formatString, args));
			
			logMethodAndLine(tag);
		}
	}

	public static void w(String tag, String msg) {
		if (Log.isLoggable(tag, Log.WARN)) {
			Log.w(tag, msg);
			
			logMethodAndLine(tag);
		}
	}
	
	public static void w(String tag, String formatString, Object... args) {
		if (Log.isLoggable(tag, Log.WARN)) {
			Log.w(tag, String.format(formatString, args));
			
			logMethodAndLine(tag);
		}
	}

	protected static void logMethodAndLine(String tag) {
		
		//we'll only log method and line if in debug mode
		if (!Log.isLoggable(tag, Log.DEBUG))
			return;
			
		StackTraceElement[] stackTraceElement = Thread.currentThread()
		        .getStackTrace();
		int currentIndex = -1;
		for (int i = 0; i < stackTraceElement.length; i++) {
		    if (stackTraceElement[i].getMethodName().compareTo("logMethodAndLine") == 0)
		    {
		        currentIndex = i + 2;
		        break;
		    }
		}
		
		//if we didn't find it then get out, do not crash
		if (currentIndex == -1)
			return;

		String fullClassName = stackTraceElement[currentIndex].getClassName();
		String className = fullClassName.substring(fullClassName
		        .lastIndexOf(".") + 1);
		String methodName = stackTraceElement[currentIndex].getMethodName();
		String lineNumber = String
		        .valueOf(stackTraceElement[currentIndex].getLineNumber());
		
		Log.d(tag + " position", "at " + fullClassName + "." + methodName + "("
		        + className + ".java:" + lineNumber + ")");
	}
	
}
