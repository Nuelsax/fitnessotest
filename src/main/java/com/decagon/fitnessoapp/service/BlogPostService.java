package com.decagon.fitnessoapp.service;

import com.decagon.fitnessoapp.dto.BlogPostResponse;
import com.decagon.fitnessoapp.dto.BlogRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface BlogPostService {

    List<BlogPostResponse> getAllPosts(Integer pageNo, Integer pageSize, String sortBy);
    ResponseEntity<String> deletePost(Long id);
    ResponseEntity<String> updatePost(BlogPostResponse blogPostUpdated, Long id);
    ResponseEntity<String> addBlogPost(BlogRequest blogRequest, Authentication authentication);



}
