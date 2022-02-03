package com.decagon.fitnessoapp.controller;

import com.decagon.fitnessoapp.dto.BlogPostResponse;
import com.decagon.fitnessoapp.dto.BlogRequest;
import com.decagon.fitnessoapp.model.blog.BlogPost;
import com.decagon.fitnessoapp.service.BlogPostService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/articles")
@AllArgsConstructor
public class BlogPostController {

    private final BlogPostService blogPostService;
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin/post")
    public ResponseEntity<String> createPost(@RequestBody BlogRequest blogRequest, Authentication authentication) {
        return blogPostService.addBlogPost(blogRequest, authentication);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/allposts")
    public ResponseEntity<List<BlogPostResponse>> getAllBlogPosts(
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "id") String sortBy) {
        List<BlogPostResponse> blogPostResponses = blogPostService.getAllPosts(pageNo, pageSize, sortBy);
        return new ResponseEntity<>(
                blogPostResponses, new HttpHeaders(), HttpStatus.OK
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/admin/updatepost/{postId}")
    public ResponseEntity<String> updatePost(@PathVariable Long postId, @RequestBody BlogPostResponse blogPostUpdate){
        return blogPostService.updatePost(blogPostUpdate, postId);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/admin/deletepost/{postId}")
    public ResponseEntity<String> deletePost(@PathVariable Long postId){
        return blogPostService.deletePost(postId);
    }

    @GetMapping("/blogposts/{number}")
    public ResponseEntity<?> getBlogPosts(@PathVariable (name = "number") int pageNumber) {
        int pageSize = 10;
        String sortBy = "id";
        List<BlogPostResponse> blogPosts = blogPostService.getAllPosts(pageNumber, pageSize, sortBy);
        return new ResponseEntity<>(blogPosts, HttpStatus.OK);
    }
}
