package com.decagon.fitnessoapp.service;

import com.decagon.fitnessoapp.dto.BlogPostResponse;
import com.decagon.fitnessoapp.dto.BlogRequest;
import com.decagon.fitnessoapp.dto.BlogResponse;
import com.decagon.fitnessoapp.dto.BlogUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface BlogPostService {

    BlogResponse deletePost(Long id);

    List<BlogPostResponse> getAllPosts(Integer pageNo, Integer pageSize, String sortBy);

    Page<BlogPostResponse> getAllBlogPosts(int pageNo);

    BlogResponse updatePost(BlogUpdateRequest blogUpdateRequest, Long id);
    BlogResponse addBlogPost(BlogRequest blogRequest, Authentication authentication);



}
