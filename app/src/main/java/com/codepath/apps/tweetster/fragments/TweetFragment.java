package com.codepath.apps.tweetster.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.codepath.apps.tweetster.R;
import com.codepath.apps.tweetster.TimelineActivity;
import com.codepath.apps.tweetster.TweetsterClient;
import com.squareup.picasso.Picasso;

/**
 * Created by meganoneill on 8/4/16.
 */
public class TweetFragment extends DialogFragment {
    EditText message;
    ImageView profileImage;
    private String profileImageUrl;
    Button btnTweet;
    TweetsterClient client;

    public TweetFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public static TweetFragment newInstance(String name) {
        TweetFragment frag = new TweetFragment();
        Bundle args = new Bundle();
        args.putString("name", name);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.compose_tweet_fragment, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Get field from view
        message = (EditText) view.findViewById(R.id.tvMessage);
        // Fetch arguments from bundle and set title
        profileImage = (ImageView) view.findViewById(R.id.ivProfileImage);
        profileImageUrl = getArguments().getString("profileImageUrl");
        Picasso.with(getContext()).load(profileImageUrl).into(profileImage);
        // Show soft keyboard automatically and request focus to field
        message.requestFocus();


        btnTweet = (Button) view.findViewById(R.id.btnTweet);
        btnTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText etComposeTweet = (EditText) getDialog().findViewById(R.id.tvMessage);
                String myTweet = etComposeTweet.getText().toString();
                TimelineActivity timelineActivity = (TimelineActivity) getActivity();
                timelineActivity.onTweetButtonClicked(myTweet);
                getDialog().dismiss();
            }
        });


    }


}
