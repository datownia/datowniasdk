package com.releasemobile.toolkit;

import java.util.Locale;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.TextView;

public class ViewHelper {

	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	public static void setBackground(View v, Drawable drawable)
	{
		if (android.os.Build.VERSION.SDK_INT >= 16)
			v.setBackground(drawable);
		else
			v.setBackgroundDrawable(drawable);
	}
	
	@SuppressLint("NewApi")
	public static void setAllCaps(TextView v, boolean value)
	{
		if (android.os.Build.VERSION.SDK_INT >= 14)
			v.setAllCaps(value);
		else
			v.setText(((String) v.getText()).toUpperCase(Locale.ROOT));
	}
}
