package com.decagon.fitnessoapp.controller;

import com.decagon.fitnessoapp.dto.*;
import com.decagon.fitnessoapp.security.JwtUtils;
import com.decagon.fitnessoapp.security.PersonDetails;
import com.decagon.fitnessoapp.security.PersonDetailsService;
import com.decagon.fitnessoapp.service.VerificationService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import com.decagon.fitnessoapp.service.PersonService;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequestMapping("/person")
@AllArgsConstructor
public class PersonController {

    private final PersonService personService;
    public final VerificationService verificationTokenService;
    private final PersonDetailsService personDetailsService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody PersonDto personDto){
        return ResponseEntity.ok(personService.register(personDto));
    }

    @GetMapping("/confirm")
    public String confirm(@RequestParam("token") String token){
        return verificationTokenService.confirmToken(token);
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
