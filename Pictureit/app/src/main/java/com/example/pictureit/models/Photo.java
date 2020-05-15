package com.example.pictureit.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Photo implements Parcelable {
    private String date_created;
    private String image_path;
    private String user_id;
    private String tags;

    public Photo(String date_created, String image_path, String user_id, String tags ){
        this.date_created = date_created;
        this.image_path = image_path;
        this.tags = tags;
        this.user_id = user_id;
    }

    protected Photo(Parcel in) {
        date_created = in.readString();
        image_path = in.readString();
        user_id = in.readString();
        tags = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(date_created);
        dest.writeString(image_path);
        dest.writeString(user_id);
        dest.writeString(tags);
    }

    public static final Creator<Photo> CREATOR = new Creator<Photo>() {
        @Override
        public Photo createFromParcel(Parcel in) {
            return new Photo(in);
        }

        @Override
        public Photo[] newArray(int size) {
            return new Photo[size];
        }
    };

    public String getImage_path() {
        return image_path;
    }

    public String getDate_created() {
        return date_created;
    }

    public String getTags() {
        return tags;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }

    public void setDate_created(String date_created) {
        this.date_created = date_created;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    @Override
    public String toString() {
        return "Photo{" +
                "date_created='" + date_created + '\'' +
                ", image_path='" + image_path + '\'' +
                ", user_id='" + user_id + '\'' +
                ", tags='" + tags +
                '}';
    }


}
