package com.springboot.blog.payload;

import lombok.Data;

import java.util.List;

@Data
public class PostResponse {
    private List<PostDto> content;
    private int pageSize;
    private int pageNo;
    private long totalElements;
    private int totalPage;
    private boolean last;

}
