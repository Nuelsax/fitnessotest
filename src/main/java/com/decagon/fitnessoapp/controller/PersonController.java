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


}
