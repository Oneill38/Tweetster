package com.codepath.apps.tweetster;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.tweetster.fragments.UserTimelineFragment;
import com.codepath.apps.tweetster.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class ProfileActivity extends AppCompatActivity {
    TweetsterClient client;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        client = TweetsterApplication.getRestClient();
        String screen_name = getIntent().getStringExtra("screen_name");
        client.getUserInfo(screen_name, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                user = User.fromJSON(response);
                Log.d("user", user.getScreenName());
                getSupportActionBar().setTitle("@" + user.getScreenName());
                populateProfileHeader(user);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("DEBUG", errorResponse.toString());
            }
        });

        if(savedInstanceState == null){
            UserTimelineFragment fragmentUserTimeline = UserTimelineFragment.newInstance(screen_name);

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.flContainer, fragmentUserTimeline);
            ft.commit();
        }

    }

    private void populateProfileHeader(User user){
        TextView name = (TextView) findViewById(R.id.tvName);
        TextView tagline = (TextView) findViewById(R.id.tvTagline);
        TextView followers = (TextView) findViewById(R.id.tvFollowers);
        TextView following = (TextView) findViewById(R.id.tvFollowing);
        ImageView pic = (ImageView) findViewById(R.id.ivProfileImage);
        name.setText(user.getName());
        tagline.setText(user.getTagline());
        followers.setText(user.getFollowers() + " Followers");
        following.setText(user.getFollowing() + " Following");
        Picasso.with(this).load(user.getProfileImageUrl()).into(pic);
    }
}
