package com.decagon.fitnessoapp.controller;

import com.decagon.fitnessoapp.dto.BlogPostResponse;
import com.decagon.fitnessoapp.dto.BlogRequest;
import com.decagon.fitnessoapp.service.BlogPostService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@AllArgsConstructor
public class BlogPostController {

    private final BlogPostService blogPostService;
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/post")
    public ResponseEntity<String> createPost(@RequestBody BlogRequest blogRequest, Authentication authentication) {
        return blogPostService.addBlogPost(blogRequest, authentication);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/allposts")
    public ResponseEntity<List<BlogPostResponse>> getAllBlogPosts(
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "id") String sortBy) {
        List<BlogPostResponse> blogPostResponses = blogPostService.getAllPosts(pageNo, pageSize, sortBy);
        return new ResponseEntity<List<BlogPostResponse>>(
                blogPostResponses, new HttpHeaders(), HttpStatus.OK
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/updatepost/{postId}")
    public ResponseEntity<String> updatePost(@PathVariable Long postId, @RequestBody BlogPostResponse blogPostUpdate){
        return blogPostService.updatePost(blogPostUpdate, postId);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/deletepost/{postId}")
    public ResponseEntity<String> deletePost(@PathVariable Long postId){
        return blogPostService.deletePost(postId);
    }
}
