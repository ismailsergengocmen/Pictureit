package com.example.pictureit.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Photo implements Parcelable {
    private String date_created;
    private String image_path;
    private String image_id;
    private String user_id;
    private String tag1;
    private String tag2;
    private Integer position;

    public Photo(String date_created, String image_path, String image_id, String user_id, String tag1, String tag2, Integer position) {
        this.date_created = date_created;
        this.image_path = image_path;
        this.image_id = image_id;
        this.tag1 = tag1;
        this.tag2 = tag2;
        this.user_id = user_id;
        this.position = position;
    }

    protected Photo(Parcel in) {
        date_created = in.readString();
        image_path = in.readString();
        image_id = in.readString();
        user_id = in.readString();
        tag1 = in.readString();
        tag2 = in.readString();
        position = in.readInt();
    }

    public Photo() {

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(date_created);
        dest.writeString(image_path);
        dest.writeString(image_id);
        dest.writeString(user_id);
        dest.writeString(tag1);
        dest.writeString(tag2);
        dest.writeInt(position);
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

    public String getImage_id() {
        return image_id;
    }

    public String getDate_created() {
        return date_created;
    }

    public String getTag1() {
        return tag1;
    }

    public String getTag2() {
        return tag2;
    }

    public String getUser_id() {
        return user_id;
    }

    public Integer getPosition() {
        return position;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }

    public void setImage_id(String image_id) {
        this.image_id = image_id;
    }

    public void setDate_created(String date_created) {
        this.date_created = date_created;
    }

    public void setTag1(String tag1) {
        this.tag1 = tag1;
    }

    public void setTag2(String tag2) {
        this.tag2 = tag2;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    @Override
    public String toString() {
        return "Photo{" +
                "date_created='" + date_created + '\'' +
                ", image_path='" + image_id + '\'' +
                ", image_path='" + image_path + '\'' +
                ", user_id='" + user_id + '\'' +
                ", tag1='" + tag1 + '\'' +
                ", tag2='" + tag2 + '\'' +
                ", position='" + position +
                '}';
    }
}
