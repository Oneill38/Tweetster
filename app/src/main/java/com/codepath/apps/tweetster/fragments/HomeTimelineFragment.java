package com.codepath.apps.tweetster.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.codepath.apps.tweetster.R;
import com.codepath.apps.tweetster.TweetsArrayAdapter;
import com.codepath.apps.tweetster.TweetsterApplication;
import com.codepath.apps.tweetster.TweetsterClient;
import com.codepath.apps.tweetster.models.Tweet;
import com.codepath.apps.tweetster.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * Created by meganoneill on 8/8/16.
 */
public class HomeTimelineFragment extends TweetsListFragment {
    private TweetsterClient client;
    private ArrayList<Tweet> tweets;
    private ListView lvTweets;
    User myUserAccount;
    private TweetsArrayAdapter aTweets;
    SwipeRefreshLayout swipeContainer;
    Tweet last_tweet;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = TweetsterApplication.getRestClient();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tweets_list, parent, false);
        popTimeline(null);
        return super.onCreateView(inflater, parent, savedInstanceState);
    }

    @Override
    protected void populateTimeline(Long max_id) {
        if(max_id != null){
            popTimeline(max_id);
        }else{
            popTimeline(null);
        }
    }

    @Override
    protected void refreshTimeline() {
        popTimeline(null);
    }

    private void popTimeline(Long tweetID){
        client.getHomeTimeline(tweetID, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray json) {
                ArrayList<Tweet> tweets = Tweet.fromJSONArray(json);
                addAll(tweets);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("debug", errorResponse.toString());
            }
        });
    }

}
