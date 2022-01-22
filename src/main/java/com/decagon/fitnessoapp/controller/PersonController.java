package com.decagon.fitnessoapp.controller;

import com.decagon.fitnessoapp.dtos.AuthRequest;
import com.decagon.fitnessoapp.dtos.AuthResponse;
import com.decagon.fitnessoapp.security.JwtUtils;
import com.decagon.fitnessoapp.security.PersonDetails;
import com.decagon.fitnessoapp.security.PersonDetailsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/person")
public class PersonController {
    private final PersonDetailsService personDetailsService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    public PersonController(PersonDetailsService personDetailsService, AuthenticationManager authenticationManager, JwtUtils jwtUtils) {
        this.personDetailsService = personDetailsService;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/login")
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
    }
}
