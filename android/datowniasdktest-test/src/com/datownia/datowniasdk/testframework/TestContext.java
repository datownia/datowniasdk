package com.datownia.datowniasdk.testframework;

import android.content.Context;
import android.content.ContextWrapper;

import com.releasemobile.data.Repository;
import com.releasemobile.data.RepositoryStorableContext;
import com.releasemobile.data.RepositoryStore;

public class TestContext extends ContextWrapper implements RepositoryStorableContext {

	private RepositoryStore repositoryStore = new RepositoryStore();

	public TestContext(Context context)
	{
		super(context);
	}
	
	@Override
	public Repository getRepository(String name) {
		return repositoryStore.getRepository(name);
	}

	@Override
	public void addRepository(String name, Repository repository) {
		repositoryStore.addRepository(name, repository);
		
	}


}

