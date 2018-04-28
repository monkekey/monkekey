package com.yumi.butler.domain;

import javax.persistence.*;

@Entity
@Table(name = "comment_detail")
public class CommentDetail {
    private long id;
    private Long commentId;
    private Long commentTagId;

    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "commentId", nullable = true)
    public Long getCommentId() {
        return commentId;
    }

    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }

    @Basic
    @Column(name = "commentTagId", nullable = true)
    public Long getCommentTagId() {
        return commentTagId;
    }

    public void setCommentTagId(Long commentTagId) {
        this.commentTagId = commentTagId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CommentDetail that = (CommentDetail) o;

        if (id != that.id) return false;
        if (commentId != null ? !commentId.equals(that.commentId) : that.commentId != null) return false;
        if (commentTagId != null ? !commentTagId.equals(that.commentTagId) : that.commentTagId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (commentId != null ? commentId.hashCode() : 0);
        result = 31 * result + (commentTagId != null ? commentTagId.hashCode() : 0);
        return result;
    }
}
