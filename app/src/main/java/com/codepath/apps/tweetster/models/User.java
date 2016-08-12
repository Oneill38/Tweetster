package com.codepath.apps.tweetster.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by meganoneill on 8/3/16.
 */
public class User {
    private String name;
    private Long uid;
    private String screenName;
    private String profileImageUrl;
    private String tagline;
    private int followers;
    private int following;

    public int getFollowers() {
        return followers;
    }

    public String getTagline() {
        return tagline;
    }

    public int getFollowing() {
        return following;
    }

    public String getScreenName() {
        return screenName;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public Long getUid() {
        return uid;
    }

    public String getName() {
        return name;
    }

    public static User fromJSON(JSONObject json){
        User u = new User();
        try {
            u.name = json.getString("name");
            u.uid = json.getLong("id");
            u.screenName = json.getString("screen_name");
            u.profileImageUrl = json.getString("profile_image_url");
            u.tagline = json.getString("description");
            u.followers = json.getInt("followers_count");
            u.following = json.getInt("friends_count");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return u;
    }
}
