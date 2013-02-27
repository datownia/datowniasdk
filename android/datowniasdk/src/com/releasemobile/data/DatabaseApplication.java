package com.releasemobile.data;

import java.util.HashMap;

import android.app.Application;
import android.content.Context;

public class DatabaseApplication extends Application implements RepositoryStorableContext {
	
//	private RepositoryStore repositoryStore;
	private HashMap<String, Repository> repoMap = new HashMap<String, Repository>();
	
	@Override
	public void addRepository(String name, Repository repository)
	{
		repoMap.put(name, repository);
	}

	@Override
	public Repository getRepository(String name)
	{
		//attempt to get Repository out of the map
		return repoMap.get(name);
	}

}
