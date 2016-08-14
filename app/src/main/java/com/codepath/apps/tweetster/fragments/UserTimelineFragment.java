package com.codepath.apps.tweetster.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ListView;

import com.codepath.apps.tweetster.TweetsArrayAdapter;
import com.codepath.apps.tweetster.TweetsterApplication;
import com.codepath.apps.tweetster.TweetsterClient;
import com.codepath.apps.tweetster.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * Created by meganoneill on 8/9/16.
 */
public class UserTimelineFragment extends TweetsListFragment{
    private TweetsterClient client;
    private ListView lvTweets;
    private TweetsArrayAdapter aTweets;
    Tweet last_tweet;

    public static UserTimelineFragment newInstance(String screen_name){
        UserTimelineFragment userFragment = new UserTimelineFragment();
        Bundle args = new Bundle();
        args.putString("screen_name", screen_name);
        userFragment.setArguments(args);
        return userFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = TweetsterApplication.getRestClient();
        popTimeline(null);
    }

    @Override
    protected void populateTimeline(Long max_id) {
        popTimeline(last_tweet.getId());
    }

    @Override
    protected void refreshTimeline() {
        popTimeline(null);
    }

    private void popTimeline(Long tweetID){
        String screenName = getArguments().getString("screen_name");
        client.getUserTimeline(screenName, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray json) {
                ArrayList<Tweet> tweets = Tweet.fromJSONArray(json);
                addAll(tweets);
                last_tweet = tweets.get(0);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("debug", errorResponse.toString());
            }
        });
    }
}
