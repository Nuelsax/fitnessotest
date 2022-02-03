package com.decagon.fitnessoapp.controller;

import com.decagon.fitnessoapp.dto.ChangePassword;
import com.decagon.fitnessoapp.dto.UpdatePersonDetails;
import com.decagon.fitnessoapp.model.user.ROLE_DETAIL;
import com.decagon.fitnessoapp.service.PersonService;
import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.errors.MailjetSocketTimeoutException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.decagon.fitnessoapp.dto.*;
import com.decagon.fitnessoapp.service.VerificationService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/person")
@AllArgsConstructor
public class PersonController {

    private final PersonService personService;
    public final VerificationService verificationTokenService;

        @PutMapping("/profile/edit/personinfo")
        public ResponseEntity<PersonResponse> editUserDetails(@RequestBody UpdatePersonDetails updatePersonDetails) {
            return ResponseEntity.ok().body( personService.updateUserDetails(updatePersonDetails));
        }

        @PreAuthorize("hasRole('PREMIUM') or hasRole('ADMIN')")
        @PutMapping("/profile/edit/password")
        public  ResponseEntity<?> editUserPassword(@RequestBody ChangePassword changePassword) {
            return ResponseEntity.ok().body(personService.updateCurrentPassword(changePassword));
        }

        @PostMapping("/register")
        public ResponseEntity<?> register (@Valid @RequestBody PersonRequest personRequest) throws MailjetSocketTimeoutException, MailjetException {
            personRequest.setRoleDetail(ROLE_DETAIL.PREMIUM);
            return ResponseEntity.ok(personService.register(personRequest));
        }

        @PostMapping("/admin/register")
        public ResponseEntity<?> registerAdmin (@Valid @RequestBody PersonRequest personRequest) throws MailjetSocketTimeoutException, MailjetException {
            personRequest.setRoleDetail(ROLE_DETAIL.ADMIN);
            return ResponseEntity.ok(personService.register(personRequest));
        }

        @GetMapping("/confirm")
        public String confirm (@RequestParam("token") String token){
            return verificationTokenService.confirmToken(token);
        }

        @PostMapping("/login")
        public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest req) throws Exception {
            return personService.loginUser(req);
        }

        @PostMapping("/reset_password")
        public ResponseEntity<String> processResetPassword (@RequestBody EmailRequest resetEmail) throws MailjetSocketTimeoutException, MailjetException {
            return ResponseEntity.ok().body(personService.resetPasswordToken(resetEmail.getEmail()));
        }

        @PutMapping("/update_password")
        public ResponseEntity<?> updatePassword(@RequestBody ResetPasswordRequest passwordRequest, @RequestParam(value = "token") String token){
            return ResponseEntity.ok().body(personService.updateResetPassword(passwordRequest, token));
        }

        @PostMapping("/admin/reset_password")
        public ResponseEntity<String> adminProcessResetPassword (@RequestBody EmailRequest resetEmail) throws MailjetSocketTimeoutException, MailjetException {
            return ResponseEntity.ok().body(personService.resetPasswordToken(resetEmail.getEmail()));
        }

        @PutMapping("/admin/update_password")
        public ResponseEntity<?> adminUpdatePassword(@RequestBody ResetPasswordRequest passwordRequest, @RequestParam(value = "token") String token){
            return ResponseEntity.ok().body(personService.updateResetPassword(passwordRequest, token));
        }
}

