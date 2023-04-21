package com.myblog.service.impl;

import com.myblog.entity.Comment;
import com.myblog.payload.CommentDTO;

import java.util.List;

public interface CommentService {
    CommentDTO createComment(long postId, CommentDTO commentDTO);

    List<CommentDTO>getCommentsByPostId(long postId);

    CommentDTO getCommentById(long postId, long commentId);

    CommentDTO updateComment(long postId, long id, CommentDTO commentDTO);

    void deleteComment(long postId, long id);
}
