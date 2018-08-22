package com.cchao.simplemusic.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author cchao
 * @version 8/20/18.
 */
public class MusicItem implements Parcelable {

    public String id;
    public String src;
    public String name;
    public String singer;
    public String duration;
    public String picture;

    protected MusicItem(Parcel in) {
        id = in.readString();
        src = in.readString();
        name = in.readString();
        singer = in.readString();
        duration = in.readString();
        picture = in.readString();
    }

    public static final Creator<MusicItem> CREATOR = new Creator<MusicItem>() {
        @Override
        public MusicItem createFromParcel(Parcel in) {
            return new MusicItem(in);
        }

        @Override
        public MusicItem[] newArray(int size) {
            return new MusicItem[size];
        }
    };

    public String getId() {
        return id;
    }

    public MusicItem setId(String id) {
        this.id = id;
        return this;
    }

    public String getSrc() {
        return src;
    }

    public MusicItem setSrc(String src) {
        this.src = src;
        return this;
    }

    public String getName() {
        return name;
    }

    public MusicItem setName(String name) {
        this.name = name;
        return this;
    }

    public String getSinger() {
        return singer;
    }

    public MusicItem setSinger(String singer) {
        this.singer = singer;
        return this;
    }

    public String getDuration() {
        return duration;
    }

    public MusicItem setDuration(String duration) {
        this.duration = duration;
        return this;
    }

    public String getPicture() {
        return picture;
    }

    public MusicItem setPicture(String picture) {
        this.picture = picture;
        return this;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(src);
        parcel.writeString(name);
        parcel.writeString(singer);
        parcel.writeString(duration);
        parcel.writeString(picture);
    }
}
