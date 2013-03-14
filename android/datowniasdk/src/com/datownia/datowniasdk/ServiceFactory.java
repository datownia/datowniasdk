package com.datownia.datowniasdk;

import com.releasemobile.data.RepositoryStorableContext;

public class ServiceFactory {

	public DatowniaAppService createAppService(RepositoryStorableContext context, DatowniaAppConfiguration config)
	{
		DatowniaAppService service = new DatowniaAppService(context, config);
		return service;
		
	}

}
