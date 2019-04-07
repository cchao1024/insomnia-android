package com.cchao.insomnia.model.javabean.post;

import java.util.Date;
import java.util.List;

import lombok.Data;

/**
 * @author : cchao
 * @version 2019-03-11
 */
@Data
public class PostVO {

    long id;
    long postUserId;
    int likeCount;
    String content;
    String postUserName;
    String postUserAvatar;
    String images;
    Date updateTime;

    int curPage;
    int totalPage;
    List<CommentVO> list;
}
