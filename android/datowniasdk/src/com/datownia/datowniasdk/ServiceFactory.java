package com.datownia.datowniasdk;

import android.content.Context;

public class ServiceFactory {

	public DatowniaAppService createAppService(Context context, DatowniaAppConfiguration config)
	{
		DatowniaAppService service = new DatowniaAppService(context, config);
		return service;
		
	}

}
