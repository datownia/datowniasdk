package com.datownia.datowniasdk;

import com.releasemobile.data.RepositoryStorableContext;

public class DaoFactory {

	public DatowniaManagementDAO getDatowniaDao(RepositoryStorableContext context, DatowniaAppConfiguration config)
	{
		return new DatowniaManagementDAO(context, 
				config.getDatabaseName(), 
				config.getDatabaseFolder());
	}
}

