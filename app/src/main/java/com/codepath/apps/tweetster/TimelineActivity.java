package com.codepath.apps.tweetster;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.codepath.apps.tweetster.fragments.HomeTimelineFragment;
import com.codepath.apps.tweetster.fragments.MentionsTimelineFragment;
import com.codepath.apps.tweetster.fragments.TweetFragment;
import com.codepath.apps.tweetster.fragments.TweetsListFragment;
import com.codepath.apps.tweetster.models.Tweet;
import com.codepath.apps.tweetster.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class TimelineActivity extends AppCompatActivity {
    private TweetsterClient client;
    private TweetsListFragment fragmentTweetsList;
    User myUserAccount;
    private TweetsArrayAdapter aTweets;
    private ArrayList<Tweet> tweets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        ViewPager vpPager = (ViewPager) findViewById(R.id.viewpager);
        vpPager.setAdapter(new TweetsPagerAdapter(getSupportFragmentManager()));

        PagerSlidingTabStrip tabStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        tabStrip.setViewPager(vpPager);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.action_compose){
            showComposeFragment();
        }
        return super.onOptionsItemSelected(item);
    }

    private void showComposeFragment(){
        FragmentManager fm = getSupportFragmentManager();
        String myProfileImageUrl = myUserAccount.getProfileImageUrl();
        TweetFragment tweetFilter =  TweetFragment.newInstance(myProfileImageUrl);
        tweetFilter.show(fm, "compose_tweet_fragment");
    }



    public void onTweetButtonClicked(String myTweetText) {
        // when the user composes a new tweet and taps the Tweet button, post it
        client.postTweet(myTweetText, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject json) {
                // get the new tweet and add it to the ArrayList
                Tweet myNewTweet = Tweet.fromJSON(json);
                tweets.add(0, myNewTweet);
                // notify the adapter
                aTweets.notifyDataSetChanged();
                // scroll back to display the new tweet
                // display a success Toast
                Toast toast = Toast.makeText(TimelineActivity.this, "Tweet posted!", Toast.LENGTH_SHORT);
                toast.show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("DEBUG", errorResponse.toString());
            }
        });
    }

    public class TweetsPagerAdapter extends FragmentPagerAdapter{
        private String tabTitles[] = {"Home", "Mentions"};

        public TweetsPagerAdapter(FragmentManager fm){
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if(position == 0){
                return new HomeTimelineFragment();
            }else if (position == 1){
                return  new MentionsTimelineFragment();
            }else{
                return null;
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }

        @Override
        public int getCount() {
            return tabTitles.length;
        }
    }



}
