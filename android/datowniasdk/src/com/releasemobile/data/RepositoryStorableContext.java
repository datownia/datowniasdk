package com.releasemobile.data;

import android.content.Context;

public interface RepositoryStorableContext {
	
	public abstract Repository getRepository(String name);

	public abstract void addRepository(String name, Repository repository);
	
	//standard context methods
	public abstract Context getApplicationContext ();
	public abstract Object getSystemService (String name);

}
