package com.springboot.blog.exception;

public class BlogApiException extends RuntimeException{
    public BlogApiException(String message) {
        super(message);
    }

}
