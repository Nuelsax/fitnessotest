package com.decagon.fitnessoapp.controller;

import com.decagon.fitnessoapp.model.user.ROLE_DETAIL;
import com.decagon.fitnessoapp.service.FavouriteService;
import com.decagon.fitnessoapp.service.PersonService;
import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.errors.MailjetSocketTimeoutException;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import com.decagon.fitnessoapp.dto.*;
import com.decagon.fitnessoapp.service.VerificationService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/person")
@AllArgsConstructor
public class PersonController {

    private final PersonService personService;
    private final FavouriteService favouriteService;
    public final VerificationService verificationTokenService;

        @PutMapping("/profile/edit/personinfo")
        public ResponseEntity<UpdatePersonResponse> editUserDetails(@RequestBody UpdatePersonRequest updatePersonDetails) {
            return ResponseEntity.ok().body( personService.updateUserDetails(updatePersonDetails));
        }

        @PreAuthorize("hasRole('ROLE_PREMIUM') or hasRole('ROLE_ADMIN')")
        @PutMapping("/profile/edit/password")
        public  ResponseEntity<ChangePasswordResponse> editUserPassword(@RequestBody ChangePasswordRequest changePassword) {
            return ResponseEntity.ok().body(personService.updateCurrentPassword(changePassword));
        }

        @PostMapping("/register")
        public ResponseEntity<?> register (@Valid @RequestBody PersonRequest personRequest) throws MailjetSocketTimeoutException, MailjetException, IOException {
            personRequest.setRoleDetail(ROLE_DETAIL.PREMIUM);
            return ResponseEntity.ok(personService.register(personRequest));
        }

        @PostMapping("/admin/register")
        public ResponseEntity<?> registerAdmin (@Valid @RequestBody PersonRequest personRequest) throws MailjetSocketTimeoutException, MailjetException, IOException {
            personRequest.setRoleDetail(ROLE_DETAIL.ADMIN);
            return ResponseEntity.ok(personService.register(personRequest));
        }

        @PreAuthorize("hasRole('ROLE_ADMIN')")
        @PutMapping("/trainer/register")
        public ResponseEntity<?> addTrainer (@Valid @RequestBody PersonRequest personRequest){
        return ResponseEntity.ok(personService.addTrainer(personRequest));
        }


        @PreAuthorize("hasRole('ROLE_ADMIN')")
        @DeleteMapping("/trainer/delete/{id}")
        public ResponseEntity<String> removeTrainer (@Valid @PathVariable ("id") Long id){
        return personService.removeTrainer(id);
    }

        @GetMapping("/confirm")
        public String confirm (@RequestParam("token") String token){
            return verificationTokenService.confirmToken(token);
        }

        @PostMapping(path="/login", produces = MediaType.APPLICATION_JSON_VALUE)
        public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest req) throws Exception {
            return personService.loginUser(req);
        }

        @PostMapping("/reset_password")
        public ResponseEntity<?> processResetPassword (@RequestBody EmailRequest resetEmail) throws MailjetSocketTimeoutException, MailjetException {
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
        @PostMapping("/add_or_delete_favourite/{productId}")
        public ResponseEntity<String> addOrDeleteFavourite(@PathVariable("productId") Long productId, Authentication authentication){
            return favouriteService.addOrDeleteFavourite(productId, authentication);
        }

        @GetMapping("/view_favourites")
         public ResponseEntity<List<ProductResponseDto>> viewFavourites(Authentication authentication){
            return ResponseEntity.ok().body(favouriteService.viewFavourites(authentication));
        }

}

