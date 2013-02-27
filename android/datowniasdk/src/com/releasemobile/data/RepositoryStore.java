package com.releasemobile.data;

import java.util.HashMap;

public class RepositoryStore {

	private HashMap<String, Repository> repoMap = new HashMap<String, Repository>();
	
	public void addRepository(String name, Repository repository)
	{
		repoMap.put(name, repository);
	}

	public Repository getRepository(String name)
	{
		//attempt to get Repository out of the map
		return repoMap.get(name);
	}
}
