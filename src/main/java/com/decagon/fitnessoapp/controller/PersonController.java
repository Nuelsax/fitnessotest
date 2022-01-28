package com.decagon.fitnessoapp.controller;

import com.decagon.fitnessoapp.dto.ChangePassword;
import com.decagon.fitnessoapp.dto.UpdatePersonDetails;
import com.decagon.fitnessoapp.model.user.Role;
import com.decagon.fitnessoapp.service.PersonService;
import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.errors.MailjetSocketTimeoutException;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.decagon.fitnessoapp.dto.*;
import com.decagon.fitnessoapp.security.JwtUtils;
import com.decagon.fitnessoapp.service.VerificationService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequestMapping("/person")
@AllArgsConstructor
public class PersonController {

    private final PersonService personService;
    public final VerificationService verificationTokenService;

        @PutMapping("/profile/edit/personinfo")
        public ResponseEntity<String> editUserDetails(@RequestBody UpdatePersonDetails updatePersonDetails) {
            return ResponseEntity.ok().body( personService.updateUserDetails(updatePersonDetails));
        }

        @PutMapping("/profile/edit/password")
        public  ResponseEntity<String> editUserPassword(@RequestBody ChangePassword changePassword) {

            return ResponseEntity.ok().body(personService.updateCurrentPassword(changePassword));
        }

        @PostMapping("/register")
        public ResponseEntity<?> register (@Valid @RequestBody PersonRequest personRequest) throws MailjetSocketTimeoutException, MailjetException {
            personRequest.setRole(Role.PREMIUM);
            return ResponseEntity.ok(personService.register(personRequest));
        }

        @PostMapping("/admin/register")
        public ResponseEntity<?> registerAdmin (@Valid @RequestBody PersonRequest personRequest) throws MailjetSocketTimeoutException, MailjetException {
            personRequest.setRole(Role.ADMIN);
            return ResponseEntity.ok(personService.register(personRequest));
        }

        @GetMapping("/confirm")
        public String confirm (@RequestParam("token") String token){
            return verificationTokenService.confirmToken(token);
        }

        @PostMapping("/login")
        public ResponseEntity<AuthResponse> login (@RequestBody AuthRequest req, HttpServletResponse response) throws Exception {
            return personService.loginUser(req);
        }

        @PostMapping("/reset_password")
        public ResponseEntity<String> processResetPassword (@RequestBody EmailRequest resetEmail) throws MailjetSocketTimeoutException, MailjetException {
            return ResponseEntity.ok().body(personService.resetPasswordToken(resetEmail.getEmail()));
        }

        @PutMapping("/update_password/{token}")
        public ResponseEntity<?> updatePassword(@RequestBody ResetPasswordRequest passwordRequest, /*@RequestParam*/@PathVariable ("token") String token){
            System.out.println("here");
            return ResponseEntity.ok().body(personService.updateResetPassword(passwordRequest, token));
        }

        //TODO: add preAuthorization

        @PostMapping("/admin/reset_password")
        public ResponseEntity<String> adminProcessResetPassword (@RequestBody EmailRequest resetEmail) throws MailjetSocketTimeoutException, MailjetException {
            return ResponseEntity.ok().body(personService.resetPasswordToken(resetEmail.getEmail()));
        }

        @PutMapping("/admin/update_password")
        public ResponseEntity<?> adminUpdatePassword(@RequestBody ResetPasswordRequest passwordRequest, @RequestParam(value = "token") String token){
            return ResponseEntity.ok().body(personService.updateResetPassword(passwordRequest, token));
        }
}

