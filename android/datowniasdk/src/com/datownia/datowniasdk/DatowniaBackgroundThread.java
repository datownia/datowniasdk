package com.datownia.datowniasdk;

import android.os.Handler;

public class DatowniaBackgroundThread extends Thread
{
	private Handler parentHandler;
	
	public DatowniaBackgroundThread(Handler parentHandler) 
	{
		this.parentHandler = parentHandler;
	}
	
	@Override
	public void run() 
	{
		super.run();
		
		
	}

	
}
