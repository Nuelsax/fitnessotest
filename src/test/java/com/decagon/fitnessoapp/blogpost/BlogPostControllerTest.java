package com.decagon.fitnessoapp.blogpost;

import com.decagon.fitnessoapp.controller.BlogPostController;
import com.decagon.fitnessoapp.dto.BlogPostResponse;
import com.decagon.fitnessoapp.dto.BlogRequest;
import com.decagon.fitnessoapp.dto.BlogResponse;
import com.decagon.fitnessoapp.dto.BlogUpdateRequest;
import com.decagon.fitnessoapp.service.serviceImplementation.BlogPostServiceImpl;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
@SpringBootTest
public class BlogPostControllerTest {
    @Mock
    BlogPostServiceImpl blogPostServiceImpl;
    @InjectMocks
    BlogPostController blogPostController;
    @Before
    public void setup() {
    }
    @Mock
    Authentication authentication;
    @Test
    public void test_updatePost(){
        BlogUpdateRequest blogPostUpdate = new BlogUpdateRequest();
        Long postId = 1L;
        BlogResponse blogResponse = new BlogResponse();
        blogResponse.setMessage("Post Updated Successfully");
        when(blogPostServiceImpl.updatePost(blogPostUpdate, postId)).thenReturn(blogResponse);
        ResponseEntity<BlogResponse> response = blogPostController.updatePost(postId, blogPostUpdate);
        assertEquals(blogResponse, response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
    @Test
    public void test_deletePost(){
        Long id = 1L;
        BlogResponse blogResponse = new BlogResponse();
        blogResponse.setMessage("Post Deleted Successfully");
        when(blogPostServiceImpl.deletePost(id)).thenReturn(blogResponse);
        ResponseEntity<BlogResponse> response = blogPostController.deletePost(id);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(blogResponse, response.getBody());
    }
    @Test
    public void test_addBlogPost(){
        BlogRequest blogRequest = new BlogRequest();
        BlogResponse blogResponse = new BlogResponse();
        blogResponse.setMessage("Post Added Successfully");
        when(blogPostServiceImpl.addBlogPost(blogRequest, authentication)).thenReturn(blogResponse);
        ResponseEntity<BlogResponse> response = blogPostController.createPost(blogRequest, authentication);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(blogResponse, response.getBody());
    }
    @Test
    public void test_GetAllPosts() {
        String sortBy = "id";
        int pageNo = 0;
        int pageSize = 10;
        List<BlogPostResponse> blogPostResponses = new ArrayList<>();
        when(blogPostServiceImpl.getAllPosts(pageNo, pageSize, sortBy)).thenReturn(blogPostResponses);
        ResponseEntity<List<BlogPostResponse>> response = blogPostController.getAllBlogPosts(pageNo, pageSize, sortBy);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(blogPostResponses, response.getBody());
    }
}