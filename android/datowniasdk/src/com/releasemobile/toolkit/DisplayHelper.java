package com.releasemobile.toolkit;

import android.annotation.SuppressLint;
import android.graphics.Point;
import android.view.Display;

@SuppressLint("NewApi")
public class DisplayHelper {

	@SuppressWarnings("deprecation")
	public static Point getDisplaySize(final Display display) {
	    final Point point = new Point();
	    try {
	        display.getSize(point);
	    } catch (java.lang.NoSuchMethodError ignore) { // Older device
	        point.x = display.getWidth();
	        point.y = display.getHeight();
	    }
	    return point;
	}
	
}
