package com.decagon.fitnessoapp.controller;

import com.decagon.fitnessoapp.dtos.AuthRequest;
import com.decagon.fitnessoapp.security.JwtUtils;
import com.decagon.fitnessoapp.security.PersonDetailsService;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {PersonController.class})
@ExtendWith(SpringExtension.class)
public class PersonControllerTests {

    @Autowired
    private PersonController personController;
    @MockBean
    private PersonDetailsService personDetailsService;
    @MockBean
    private AuthenticationManager authenticationManager;
    @MockBean
    private JwtUtils jwtUtils;

   /* @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest req, HttpServletResponse response) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword()));
        } catch (Exception e) {
            throw new Exception("incorrect username or password!");
        }

        final PersonDetails person = personDetailsService.loadUserByUsername(req.getUsername());
        final String jwt = jwtUtils.generateToken(person);
        final AuthResponse res = new AuthResponse();
        res.setToken(jwt);
        response.addHeader("Authorization", "Bearer " + jwt);
        return new ResponseEntity(res, HttpStatus.CREATED);
    }*/

    @Test
    void testLogin() throws Exception {

        // Look at the return object here
        when(this.personDetailsService.loadUserByUsername(any()))
                .thenReturn(new User("mockitoro", "mosquito", new ArrayList<>()), );
        // Yes, the one directly above that starts with "new User"

        when(this.jwtUtils.generateToken(any()))
                .thenReturn("ABC123DEF456GHI789");
        when(this.authenticationManager.authenticate(any()))
                .thenReturn(new TestingAuthenticationToken("Principal", "Credentials"));

        AuthRequest req = new AuthRequest();
        req.setUsername("mockitoro");
        req.setPassword("mosquito");
        String content = (new ObjectMapper()).writeValueAsString(req);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MockMvcBuilders.standaloneSetup(this.personController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("{\"jwt\":\"ABC123DEF456GHI789\"}"));


    }

}
