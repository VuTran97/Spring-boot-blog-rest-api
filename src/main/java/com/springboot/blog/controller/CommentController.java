package com.springboot.blog.controller;

import com.springboot.blog.payload.CommentDto;
import com.springboot.blog.service.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CommentController {

    private CommentService commentService;

    public CommentController(CommentService commentService){
        this.commentService = commentService;
    }

    @PostMapping("posts/{postId}/comments")
    public ResponseEntity<CommentDto> create(@PathVariable("postId") long postId,@RequestBody CommentDto commentDto){
        return new ResponseEntity<>(commentService.create(postId, commentDto), HttpStatus.CREATED);
    }

    @GetMapping("posts/{postId}/comments")
    public ResponseEntity<List<CommentDto>> getByPostId(@PathVariable("postId") long postId){
        return new ResponseEntity<>(commentService.getByPostId(postId), HttpStatus.OK);
    }

    @GetMapping("/posts/{postId}/comments/{id}")
    public ResponseEntity<CommentDto> getById(@PathVariable("postId") long postId,
                                              @PathVariable("id") long commentId){
        return new ResponseEntity<>(commentService.getById(postId, commentId), HttpStatus.OK);
    }

    @PutMapping("/posts/{postId}/comments/{id}")
    public ResponseEntity<CommentDto> getById(@PathVariable("postId") long postId,
                                              @PathVariable("id") long id,
                                              @RequestBody CommentDto commentRequest){
        return new ResponseEntity<>(commentService.updateById(postId,id,commentRequest), HttpStatus.OK);
    }

    @DeleteMapping("/posts/{postId}/comments/{id}")
    public ResponseEntity<String> delete(@PathVariable("postId") long postId,
                                     @PathVariable("id") long id){
        commentService.deleteById(postId,id);
        return new ResponseEntity<>("Comments delete successfully", HttpStatus.OK);
    }

}
