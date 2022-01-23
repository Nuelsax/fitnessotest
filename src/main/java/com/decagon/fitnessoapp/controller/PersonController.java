package com.decagon.fitnessoapp.controller;

import com.decagon.fitnessoapp.dto.PersonDto;
import com.decagon.fitnessoapp.service.VerificationService;
import com.decagon.fitnessoapp.service.serviceImplementation.VerificationTokenServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.decagon.fitnessoapp.service.PersonService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/person")
@AllArgsConstructor
public class PersonController {

    private final PersonService personService;
    public final VerificationService verificationTokenService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody PersonDto personDto){
        return ResponseEntity.ok(personService.register(personDto));
    }

    @GetMapping("/confirm")
    public String confirm(@RequestParam("token") String token){
        return verificationTokenService.confirmToken(token);
    }


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
