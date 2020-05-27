package com.example.pictureit.models;

public class UserAccountSettings {

    private String display_name;
    private long posts;
    private String profile_photo;
    private String username;

    public UserAccountSettings(String display_name, long posts, String profile_photo, String username) {
        this.display_name = display_name;
        this.posts = posts;
        this.profile_photo = profile_photo;
        this.username = username;
    }

    public UserAccountSettings() {
    }

    public String getDisplay_name() {
        return display_name;
    }

    public void setDisplay_name(String display_name) {
        this.display_name = display_name;
    }

    public long getPosts() {
        return posts;
    }

    public void setPosts(long posts) {
        this.posts = posts;
    }

    public String getProfile_photo() {
        return profile_photo;
    }

    public void setProfile_photo(String profile_photo) {
        this.profile_photo = profile_photo;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "UserAccountSettings{" +
                ", display_name='" + display_name + '\'' +
                ", posts=" + posts +
                ", profile_photo='" + profile_photo + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
}