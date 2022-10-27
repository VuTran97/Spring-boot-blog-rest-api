package com.springboot.blog.service;

import com.springboot.blog.payload.CommentDto;

import java.util.List;

public interface CommentService {

    CommentDto create(long postId, CommentDto commentDto);

    List<CommentDto> getByPostId(long postId);

    CommentDto getById(long postId, long commentId);

    CommentDto updateById(long postId, long commentId, CommentDto commentBody);

    void deleteById(long postId, long commentId);

}
