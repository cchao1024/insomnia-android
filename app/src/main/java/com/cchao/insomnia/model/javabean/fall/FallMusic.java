package com.cchao.insomnia.model.javabean.fall;

import android.databinding.ObservableBoolean;

public class FallMusic {

    private Long id;
    private String name;
    private String src;
    private String singer;
    private int play_count;
    private String cover_img;
    private String add_time;

    public ObservableBoolean isPlaying = new ObservableBoolean(false);

    public String getPlay_count() {
        return play_count + "";
    }

    public void setPlay_count(int play_count) {
        this.play_count = play_count;
    }

    public String getCover_img() {
        return cover_img;
    }

    public void setCover_img(String cover_img) {
        this.cover_img = cover_img;
    }

    public String getId() {
        return id+"";
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getAdd_time() {
        return add_time;
    }

    public void setAdd_time(String add_time) {
        this.add_time = add_time;
    }

    public String getSinger() {
        return singer;
    }

    public FallMusic setSinger(String singer) {
        this.singer = singer;
        return this;
    }
}
