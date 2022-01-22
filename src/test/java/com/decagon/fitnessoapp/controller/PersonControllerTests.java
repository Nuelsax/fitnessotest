package com.decagon.fitnessoapp.controller;

import com.decagon.fitnessoapp.dtos.AuthRequest;
import com.decagon.fitnessoapp.model.user.Person;
import com.decagon.fitnessoapp.model.user.Role;
import com.decagon.fitnessoapp.security.JwtUtils;
import com.decagon.fitnessoapp.security.PersonDetails;
import com.decagon.fitnessoapp.security.PersonDetailsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Date;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {PersonController.class})
@ExtendWith(SpringExtension.class)
class PersonControllerTests {

    @Autowired
    private PersonController personController;
    @MockBean
    private PersonDetailsService personDetailsService;
    @MockBean
    private AuthenticationManager authenticationManager;
    @MockBean
    private JwtUtils jwtUtils;



    @Test
    void testLogin() throws Exception {

        Person personTest = new Person(12L, "chike", "Jachike", "Bodam", null, "12345", "09056803454", null, Role.ADMIN, true, "Male",
                new Date(), null);

        when(this.personDetailsService.loadUserByUsername((String) any()))
                .thenReturn(new PersonDetails(personTest));
        when(this.jwtUtils.generateToken((org.springframework.security.core.userdetails.UserDetails) any()))
                .thenReturn("ABC123DEF456GHI789");
        when(this.authenticationManager.authenticate((org.springframework.security.core.Authentication) any()))
                .thenReturn(new TestingAuthenticationToken("Principal", "Credentials"));

        AuthRequest req = new AuthRequest();
        req.setUsername("chike");
        req.setPassword("12345");
        String content = (new ObjectMapper()).writeValueAsString(req);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/person/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MockMvcBuilders.standaloneSetup(this.personController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("{\"token\":\"ABC123DEF456GHI789\"}"));
    }

}
