package com.yumi.butler.vo;

import com.yumi.butler.domain.Comment;
import com.yumi.butler.domain.CommentTag;

import java.util.List;

public class CommentVo extends Comment {

    public List<CommentTag> commentTags;

    public List<CommentTag> getCommentTags() {
        return commentTags;
    }

    public void setCommentTags(List<CommentTag> commentTags) {
        this.commentTags = commentTags;
    }
}
