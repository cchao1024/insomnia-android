package com.cchao.insomnia.ui.post.convert;

import com.cchao.insomnia.manager.UserManager;
import com.cchao.insomnia.model.javabean.post.CommentVO;
import com.cchao.insomnia.model.javabean.post.ReplyVO;
import com.cchao.insomnia.model.javabean.post.Replyable;
import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.Date;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author : cchao
 * @version 2019-04-13
 */
@Data
@Accessors(chain = true)
public class CommentConvert implements MultiItemEntity {
    long id;
    // 发送请求时 带过去的id
    long toId;

    public static final int TYPE_COMMENT = 0;
    public static final int TYPE_REPLY = 1;
    // comment:0 | reply:1
    int type = TYPE_COMMENT;
    String fromUserName;
    String fromUserAvatar;
    long fromUserId;

    String toUserName;
    long toUserId;
    String content;
    int likeCount;
    Date updateTime;
    // 来源数据bean
    Replyable mSourceBean;

    public static CommentConvert fromReplyVO(ReplyVO replyVO) {
        CommentConvert item2 = new CommentConvert();
        item2.setMSourceBean(replyVO);
        item2.setId(replyVO.getId())
            .setToId(replyVO.getCommentId())
            .setFromUserId(replyVO.getReplyUserId())
            .setFromUserName(replyVO.getReplyUserName())
            .setFromUserAvatar(replyVO.getReplyUserAvatar())
            .setToUserId(replyVO.getCommentUserId())
            .setToUserName(replyVO.getCommentUserName())
            .setType(TYPE_REPLY)
            .setUpdateTime(replyVO.getUpdateTime())
            .setContent(replyVO.getContent())
            .setLikeCount(replyVO.getLikeCount());
        return item2;
    }

    public static CommentConvert fromCommentVo(CommentVO commentVO) {
        CommentConvert item = new CommentConvert();
        item.setMSourceBean(commentVO);
        item.setId(commentVO.getId())
            .setToId(commentVO.getId())
            .setFromUserId(commentVO.getCommentUserId())
            .setFromUserName(commentVO.getCommentUserName())
            .setFromUserAvatar(commentVO.getCommentUserAvatar())
            .setToUserId(commentVO.getPostUserId())
            .setToUserName(commentVO.getPostUserName())
            .setType(TYPE_COMMENT)
            .setUpdateTime(commentVO.getUpdateTime())
            .setContent(commentVO.getContent())
            .setLikeCount(commentVO.getLikeCount());
        return item;
    }

    @Override
    public int getItemType() {
        return type;
    }

    public void addLike(Runnable callBack) {
        String apiType = type == TYPE_COMMENT ? "comment" : "reply";
        UserManager.addLike(apiType, id, bool1 -> {
            if (bool1) {
                mSourceBean.setLiked(true);
                mSourceBean.setLikeCount(mSourceBean.getLikeCount() + 1);
            }
            if (callBack != null) {
                callBack.run();
            }
        });
    }
}
