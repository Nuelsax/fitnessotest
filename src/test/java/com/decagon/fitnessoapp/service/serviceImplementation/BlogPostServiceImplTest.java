package com.decagon.fitnessoapp.service.serviceImplementation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.decagon.fitnessoapp.exception.CustomServiceExceptions;
import com.decagon.fitnessoapp.model.blog.BlogPost;
import com.decagon.fitnessoapp.model.user.Author;
import com.decagon.fitnessoapp.model.user.Person;
import com.decagon.fitnessoapp.model.user.ROLE_DETAIL;
import com.decagon.fitnessoapp.repository.AuthorRepository;
import com.decagon.fitnessoapp.repository.BlogPostRepository;
import com.decagon.fitnessoapp.repository.PersonRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

import java.util.ArrayList;
import java.util.Date;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {BlogPostServiceImpl.class})
@ExtendWith(SpringExtension.class)
class BlogPostServiceImplTest {
    @MockBean
    private AuthorRepository authorRepository;

    @MockBean
    private BlogPostRepository blogPostRepository;

    @Autowired
    private BlogPostServiceImpl blogPostServiceImpl;

    @MockBean
    private PersonRepository personRepository;

    @Test
    void testGetAllPosts() {
        when(this.blogPostRepository.findAll((org.springframework.data.domain.Pageable) any()))
                .thenReturn(new PageImpl<>(new ArrayList<>()));
        assertTrue(this.blogPostServiceImpl.getAllPosts(1, 3, "Sort By").isEmpty());
        verify(this.blogPostRepository).findAll((org.springframework.data.domain.Pageable) any());
    }

    @Test
    void testGetAllPosts2() {
        when(this.blogPostRepository.findAll((org.springframework.data.domain.Pageable) any()))
                .thenThrow(new CustomServiceExceptions("An error occurred"));
        assertThrows(CustomServiceExceptions.class, () -> this.blogPostServiceImpl.getAllPosts(1, 3, "Sort By"));
        verify(this.blogPostRepository).findAll((org.springframework.data.domain.Pageable) any());
    }

    @Test
    void testGetAllPosts3() {
        Author author = new Author();
        author.setAuthorName("JaneDoe");
        author.setBiography("Biography");
        author.setContact("Contact");
        author.setId(123L);
        author.setImage("Image");

        Person person = new Person();
        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        person.setDateOfBirth(Date.from(atStartOfDayResult.atZone(ZoneId.of("UTC")).toInstant()));
        person.setEmail("jane.doe@example.org");
        person.setFirstName("Jane");
        person.setGender("Gender");
        person.setId(123L);
        person.setImage("Image");
        person.setLastName("Doe");
        person.setPassword("iloveyou");
        person.setPhoneNumber("4105551212");
        person.setResetPasswordToken("ABC123");
        person.setRoleDetail(ROLE_DETAIL.ANONYMOUS);
        person.setUserName("janedoe");
        person.setVerifyEmail(true);

        BlogPost blogPost = new BlogPost();
        blogPost.setAuthor(author);
        blogPost.setContent("Not all who wander are lost");
        blogPost.setId(123L);
        blogPost.setPerson(person);
        blogPost.setTitle("Dr");

        ArrayList<BlogPost> blogPostList = new ArrayList<>();
        blogPostList.add(blogPost);
        PageImpl<BlogPost> pageImpl = new PageImpl<>(blogPostList);
        when(this.blogPostRepository.findAll((org.springframework.data.domain.Pageable) any())).thenReturn(pageImpl);
        assertEquals(1, this.blogPostServiceImpl.getAllPosts(1, 3, "Sort By").size());
        verify(this.blogPostRepository).findAll((org.springframework.data.domain.Pageable) any());
    }
}

