package com.managesensor;

import android.app.Application;
import android.content.Context;

/**
 * Application class to provide the global context.
 */
public class ApplicationContext extends Application
{
	private static ApplicationContext instance;

	public ApplicationContext()
	{
		instance = this;
	}

	public static Context getContext()
	{
		return instance;
	}
}
