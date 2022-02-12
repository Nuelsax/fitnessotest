package com.decagon.fitnessoapp.service;

import com.decagon.fitnessoapp.dto.BlogPostResponse;
import com.decagon.fitnessoapp.dto.BlogRequest;
import com.decagon.fitnessoapp.model.blog.BlogPost;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface BlogPostService {

    ResponseEntity<String> deletePost(Long id);

    List<BlogPostResponse> getAllPosts(Integer pageNo, Integer pageSize, String sortBy);

    Page<BlogPostResponse> getAllBlogPosts(int pageNo);

    ResponseEntity<String> updatePost(BlogPostResponse blogPostUpdated, Long id);
   // ResponseEntity<String> addBlogPost(BlogRequest blogRequest, Authentication authentication);

    void addBlogPost(BlogPost blogPost);



}
