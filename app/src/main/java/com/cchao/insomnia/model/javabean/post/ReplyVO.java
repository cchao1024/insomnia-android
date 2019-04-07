package com.cchao.insomnia.model.javabean.post;

import java.util.Date;

import lombok.Data;

/**
 * @author : cchao
 * @version 2019-03-11
 */
@Data
public class ReplyVO {

    long id;
    long postId;
    long commentId;
    long commentUserId;
    long replyUserId;
    String replyUserAvatar;
    String commentUserName;

    int likeCount;
    String content;
    String images;
    Date updateTime;
}
