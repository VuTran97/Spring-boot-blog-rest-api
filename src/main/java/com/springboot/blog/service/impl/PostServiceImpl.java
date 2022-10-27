package com.springboot.blog.service.impl;

import com.springboot.blog.entity.Post;
import com.springboot.blog.exception.ResourceNotFoundException;
import com.springboot.blog.payload.PostDto;
import com.springboot.blog.payload.PostResponse;
import com.springboot.blog.repository.PostRepository;
import com.springboot.blog.service.PostService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;

    private ModelMapper mapper;

    public PostServiceImpl(PostRepository postRepository, ModelMapper mapper){
        this.postRepository = postRepository;
        this.mapper = mapper;
    }
    @Override
    public PostDto createPost(PostDto postDto) {
        //convert dto to entity
        Post post = mapToPostEntity(postDto);
        Post newPost = postRepository.save(post);

        //convert entity to dto
        return mapToPostDto(newPost);
    }

    @Override
    public PostResponse getPosts(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<Post> page = postRepository.findAll(pageable);
        List<Post> posts = page.getContent();
        List<PostDto> content = posts.stream().map(this::mapToPostDto).collect(Collectors.toList());
        PostResponse response = new PostResponse();
        response.setContent(content);
        response.setPageNo(pageNo);
        response.setPageSize(pageSize);
        response.setTotalElements(page.getTotalElements());
        response.setTotalPage(page.getTotalPages());
        response.setLast(page.isLast());
        return response;
    }

    @Override
    public PostDto getPostById(long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));
        return mapToPostDto(post);
    }

    @Override
    public PostDto updatePost(PostDto postDto, long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));
        post.setTitle(postDto.getTitle());
        post.setDescription(postDto.getDescription());
        post.setContent(postDto.getContent());

        Post updatePost = postRepository.save(post);
        return mapToPostDto(updatePost);
    }

    @Override
    public void deletePostById(long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));
        postRepository.delete(post);
    }

    private PostDto mapToPostDto(Post post){
        PostDto result = mapper.map(post, PostDto.class);
        return result;
    }

    private Post mapToPostEntity(PostDto postDto){
        Post result = mapper.map(postDto, Post.class);
        return result;
    }
}
