package com.cchao.insomnia.ui.post.convert;

import com.cchao.insomnia.model.javabean.post.CommentVO;
import com.cchao.insomnia.model.javabean.post.ReplyVO;

import java.util.Date;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author : cchao
 * @version 2019-04-13
 */
@Data
@Accessors(chain = true)
public class CommentConvert {
    // comment | reply
    String type = "comment";
    String fromUserName;
    String fromUserAvatar;
    long fromUserId;

    String toUserName;
    long toUserId;
    String content;
    int likeCount;
    Date updateTime;

    public static CommentConvert fromReplyVO(ReplyVO replyVO) {
        CommentConvert item2 = new CommentConvert();
        item2.setFromUserId(replyVO.getReplyUserId())
            .setFromUserName(replyVO.getReplyUserName())
            .setFromUserAvatar(replyVO.getReplyUserAvatar())
            .setToUserId(replyVO.getCommentUserId())
            .setToUserName(replyVO.getCommentUserName())
            .setType("reply")
            .setUpdateTime(replyVO.getUpdateTime())
            .setContent(replyVO.getContent())
            .setLikeCount(replyVO.getLikeCount());
        return item2;
    }

    public static CommentConvert fromCommentVo(CommentVO commentVO) {
        CommentConvert item = new CommentConvert();
        item.setFromUserId(commentVO.getCommentUserId())
            .setFromUserName(commentVO.getCommentUserName())
            .setFromUserAvatar(commentVO.getCommentUserAvatar())
            .setToUserId(commentVO.getPostUserId())
            .setToUserName(commentVO.getPostUserName())
            .setType("comment")
            .setUpdateTime(commentVO.getUpdateTime())
            .setContent(commentVO.getContent())
            .setLikeCount(commentVO.getLikeCount());
        return item;
    }
}
