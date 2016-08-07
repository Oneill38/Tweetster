package com.codepath.apps.tweetster;

import android.content.Context;

/*
 * This is the Android application itself and is used to configure various settings
 * including the image cache in memory and on disk. This also adds a singleton
 * for accessing the relevant rest client.
 *
 *     TweetsterClient client = TweetsterApplication.getRestClient();
 *     // use client to send requests to API
 *
 */
public class TweetsterApplication extends com.activeandroid.app.Application {
	private static Context context;

	@Override
	public void onCreate() {
		super.onCreate();
		TweetsterApplication.context = this;
	}

	public static TweetsterClient getRestClient() {
		return (TweetsterClient) TweetsterClient.getInstance(TweetsterClient.class, TweetsterApplication.context);
	}
}