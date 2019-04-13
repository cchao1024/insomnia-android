package com.cchao.insomnia.model.javabean.post;

import java.util.Date;

import lombok.Data;

/**
 * @author : cchao
 * @version 2019-03-11
 */
@Data
public class PostListVO {

    long id;
    long postUserId;
    int reviewCount;
    int likeCount;
    String content;
    String postUserName;
    String postUserAvatar;
    String images;
    Date updateTime;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getPostUserId() {
        return postUserId;
    }

    public void setPostUserId(long postUserId) {
        this.postUserId = postUserId;
    }

    public int getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(int reviewCount) {
        this.reviewCount = reviewCount;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPostUserName() {
        return postUserName;
    }

    public void setPostUserName(String postUserName) {
        this.postUserName = postUserName;
    }

    public String getPostUserAvatar() {
        return postUserAvatar;
    }

    public void setPostUserAvatar(String postUserAvatar) {
        this.postUserAvatar = postUserAvatar;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
