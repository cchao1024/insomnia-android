package com.cchao.sleeping.model.javabean.fall;

public class FallMusic {

    private Long id;
    private String name;
    private String src;
    private int play_count;
    private String cover_img;
    private String add_time;

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

    public Long getId() {
        return id;
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
}