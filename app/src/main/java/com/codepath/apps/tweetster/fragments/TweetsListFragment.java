package com.codepath.apps.tweetster.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.codepath.apps.tweetster.EndlessScrollListener;
import com.codepath.apps.tweetster.R;
import com.codepath.apps.tweetster.TweetsArrayAdapter;
import com.codepath.apps.tweetster.TweetsterClient;
import com.codepath.apps.tweetster.models.Tweet;
import com.codepath.apps.tweetster.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by meganoneill on 8/8/16.
 */
public abstract class TweetsListFragment extends Fragment {
    private TweetsterClient client;
    private TweetsArrayAdapter aTweets;
    private ArrayList<Tweet> tweets;
    private ListView lvTweets;
    User myUserAccount;
    private SwipeRefreshLayout swipeContainer;
    Tweet newestTweet = null;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tweets_list, parent, false);
        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tweets = new ArrayList<>();
        aTweets = new TweetsArrayAdapter(getActivity(), tweets);
    }



    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        lvTweets = (ListView) view.findViewById(R.id.lvTweets);
        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);

        lvTweets.setAdapter(aTweets);
        lvTweets.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                populateTimeline(getOldestTweetId());
                return false;
            }
        });

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshTimeline();
            }
        });

        lvTweets.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("Clicker", "CLICKED");
                client = new TweetsterClient(getContext());
                ImageView profile = (ImageView) view.findViewById(R.id.ivProfileImage);
                client.getUserTimeline(profile.toString(), new JsonHttpResponseHandler());
                UserTimelineFragment fragmentUserTimeline = UserTimelineFragment.newInstance(profile.toString());
            }
        });
    }


    private Long getOldestTweetId() {
        if (aTweets.getCount() == 0) {
            return null;
        } else {
            Tweet tweet = aTweets.getItem(aTweets.getCount() - 1);
            return tweet.getId();
        }
    }

    public Tweet addAll(List<Tweet> tweets){
        aTweets.addAll(tweets);
        if(aTweets.getCount() > 0){
            Tweet newestTweet = aTweets.getItem(aTweets.getCount() - 1);
            Log.d("tweets", aTweets.getItem(aTweets.getCount() - 1).toString());
        }
        return newestTweet;
    }

    protected abstract void populateTimeline(Long max_id);

    protected abstract void refreshTimeline();
}
