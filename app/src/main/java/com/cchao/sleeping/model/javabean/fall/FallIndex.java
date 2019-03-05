package com.cchao.insomnia.model.javabean.fall;

import java.util.List;

public class FallIndex {
    List<FallImage> fallimages;
    List<FallMusic> music;

    public List<FallImage> getFallimages() {
        return fallimages;
    }

    public void setFallimages(List<FallImage> fallimages) {
        this.fallimages = fallimages;
    }

    public List<FallMusic> getMusic() {
        return music;
    }

    public void setMusic(List<FallMusic> music) {
        this.music = music;
    }
}