package com.cchao.insomnia.model.javabean.fall;

import android.databinding.ObservableBoolean;

import lombok.Data;

@Data
public class FallMusic {

    private long id;
    private String name;
    private String src;
    private String singer;
    private String play_count;
    private String cover_img;

    public ObservableBoolean isPlaying = new ObservableBoolean(false);
}
