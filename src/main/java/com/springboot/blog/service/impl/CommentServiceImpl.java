package com.springboot.blog.service.impl;

import com.springboot.blog.entity.Comment;
import com.springboot.blog.entity.Post;
import com.springboot.blog.exception.BlogApiException;
import com.springboot.blog.exception.ResourceNotFoundException;
import com.springboot.blog.payload.CommentDto;
import com.springboot.blog.repository.CommentRepository;
import com.springboot.blog.repository.PostRepository;
import com.springboot.blog.service.CommentService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    private PostRepository postRepository;
    private CommentRepository commentRepository;

    private ModelMapper mapper;

    public CommentServiceImpl(PostRepository postRepository, CommentRepository commentRepository, ModelMapper mapper){
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.mapper = mapper;
    }
    @Override
    public CommentDto create(long postId, CommentDto commentDto) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));
        Comment comment = mapToEntity(commentDto);
        comment.setPost(post);
        commentRepository.save(comment);
        return mapToDto(comment);
    }

    @Override
    public List<CommentDto> getByPostId(long postId) {
        List<Comment> comments = commentRepository.findByPostId(postId);
        return comments.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    public CommentDto getById(long postId, long commentId) {
        Comment comment = checkItemExists(postId,commentId);
        return mapToDto(comment);
    }

    @Override
    public CommentDto updateById(long postId, long commentId, CommentDto commentBody) {
        Comment comment = checkItemExists(postId,commentId);
        comment.setName(commentBody.getName());
        comment.setEmail(commentBody.getEmail());
        comment.setBody(commentBody.getBody());
        Comment updatedComment = commentRepository.save(comment);
        return mapToDto(updatedComment);
    }

    @Override
    public void deleteById(long postId, long commentId) {
        Comment comment = checkItemExists(postId,commentId);
        commentRepository.delete(comment);
    }

    private Comment checkItemExists(long postId, long commentId){
        Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new ResourceNotFoundException("Comment", "id", commentId));
        if(!comment.getPost().getId().equals(post.getId())){
            throw new BlogApiException("Comment does not belong to post");
        }
        return comment;
    }

    private CommentDto mapToDto(Comment comment){
        CommentDto result = mapper.map(comment, CommentDto.class);
        return result;
    }

    private Comment mapToEntity(CommentDto commentDto){
        Comment result = mapper.map(commentDto, Comment.class);
        return result;
    }
}
