package com.decagon.fitnessoapp.service.serviceImplementation;

import com.decagon.fitnessoapp.dto.BlogPostResponse;
import com.decagon.fitnessoapp.dto.BlogRequest;
import com.decagon.fitnessoapp.dto.OrderResponse;
import com.decagon.fitnessoapp.exception.CustomServiceExceptions;
import com.decagon.fitnessoapp.exception.PersonNotFoundException;
import com.decagon.fitnessoapp.model.blog.BlogPost;
import com.decagon.fitnessoapp.model.product.Order;
import com.decagon.fitnessoapp.model.user.Author;
import com.decagon.fitnessoapp.model.user.Person;
import com.decagon.fitnessoapp.repository.AuthorRepository;
import com.decagon.fitnessoapp.repository.BlogPostRepository;
import com.decagon.fitnessoapp.repository.PersonRepository;
import com.decagon.fitnessoapp.service.BlogPostService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BlogPostServiceImpl implements BlogPostService {
    private final BlogPostRepository blogPostRepository;
    private final AuthorRepository authorRepository;
    private final PersonRepository personRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<BlogPostResponse> getAllPosts(Integer pageNo, Integer pageSize, String sortBy) {
        Pageable postPage = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
        Page<BlogPost> pagedPosts = blogPostRepository.findAll(postPage);
        List<BlogPostResponse> listOfBlogs = new ArrayList<>();
        if(pagedPosts.hasContent()){
            for (BlogPost blogPost : pagedPosts) {
                BlogPostResponse blog = new BlogPostResponse();
                blog.setId(blogPost.getId());
                blog.setAuthorName(blogPost.getAuthor().getAuthorName());
                blog.setContent(blogPost.getContent());
                blog.setImage(blogPost.getPerson().getImage());
                blog.setTitle(blogPost.getTitle());
                listOfBlogs.add(blog);
            }
        }
        return listOfBlogs;

    }

    @Override
    public Page<BlogPostResponse> getAllBlogPosts(int pageNo) {
        int pageSize = 10;
        int skipCount = (pageNo - 1) * pageSize;

        List<BlogPost> blogList = blogPostRepository.findAll();

        final List<BlogPostResponse> blogPostDtoList = blogList.stream()
                .map(x -> modelMapper.map(x, BlogPostResponse.class))
                .collect(Collectors.toList())
                .stream()
                .skip(skipCount)
                .limit(pageSize)
                .collect(Collectors.toList());

        Pageable blogPage = PageRequest.of(pageNo, pageSize, Sort.by("productName").ascending());

        return new PageImpl<>(blogPostDtoList, blogPage, blogList.size());
    }

    @Override
    public ResponseEntity<String> updatePost(BlogPostResponse blogPostUpdated, Long id){
        BlogPost blogPost = blogPostRepository.findBlogPostsById(id).orElseThrow(
                ()-> new CustomServiceExceptions("Post Not Found"));
        blogPost.setId(blogPostUpdated.getId());
        blogPost.setTitle(blogPostUpdated.getTitle());
        blogPost.getPerson().setImage(blogPostUpdated.getImage());
        blogPost.setContent(blogPostUpdated.getContent());
        blogPost.getAuthor().setAuthorName(blogPostUpdated.getAuthorName());
        blogPostRepository.save(blogPost);
        return ResponseEntity.ok().body("Post updated successfully");

    }

    @Override
    public ResponseEntity<String> deletePost(Long id) {
        BlogPost blogPost = blogPostRepository.findBlogPostsById(id).orElseThrow(
                ()-> new CustomServiceExceptions("Post Not Found"));
        blogPostRepository.delete(blogPost);
        return ResponseEntity.ok().body("Post deleted successfully");

    }

    /*@Override
    public ResponseEntity<String> addBlogPost(BlogRequest blogRequest, Authentication authentication) {
        BlogPost newBlogPost = new BlogPost();
        Author author = authorRepository.findById(blogRequest.getAuthorId()).orElseThrow(()->
                new CustomServiceExceptions("Author Not Found"));
        Person admin = personRepository.findByUserName(authentication.getName()).orElseThrow(
                () -> new CustomServiceExceptions("Admin Not Found"));
        newBlogPost.setTitle(blogRequest.getBlogTitle());
        newBlogPost.setContent(blogRequest.getBlogContent());
        newBlogPost.setAuthor(author);
        newBlogPost.setPerson(admin);
        this.blogPostRepository.save(newBlogPost);
        return ResponseEntity.ok().body("Post saved successfully");
    }*/
    @Override
    public void addBlogPost(BlogPost blogPost) {
        blogPostRepository.save(blogPost);
    }
}
