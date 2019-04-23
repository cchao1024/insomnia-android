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
}
