package com.decagon.fitnessoapp.controller;

import com.decagon.fitnessoapp.dto.ChangePassword;
import com.decagon.fitnessoapp.dto.UpdatePersonDetails;
import com.decagon.fitnessoapp.service.PersonService;
import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.errors.MailjetSocketTimeoutException;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.decagon.fitnessoapp.dto.*;
import com.decagon.fitnessoapp.model.user.Person;
import com.decagon.fitnessoapp.security.JwtUtils;
import com.decagon.fitnessoapp.security.PersonDetails;
import com.decagon.fitnessoapp.security.PersonDetailsService;
import com.decagon.fitnessoapp.service.VerificationService;
import lombok.AllArgsConstructor;
import net.bytebuddy.utility.RandomString;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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


        @PutMapping("/profile/edit/personinfo")
        public void editUserDetails(@RequestBody UpdatePersonDetails updatePersonDetails) {
            personService.updateUserDetails(updatePersonDetails);
        }

        @PutMapping("/profile/edit/password")
        public void editUserPassword(@RequestBody ChangePassword changePassword) {
            personService.updateCurrentPassword(changePassword);
        }


        @PostMapping("/register")
        public ResponseEntity<?> register (@Valid @RequestBody PersonDto personDto) throws MailjetSocketTimeoutException, MailjetException {
            return ResponseEntity.ok(personService.register(personDto));
        }

        @GetMapping("/confirm")
        public String confirm (@RequestParam("token") String token){
            return verificationTokenService.confirmToken(token);
        }

        @PostMapping("/login")
        public ResponseEntity<AuthResponse> login (@RequestBody AuthRequest req, HttpServletResponse response) throws
        Exception {
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

        @PostMapping("/forgot_password")
        public ResponseEntity<String> processForgotPassword (@RequestBody Person person) throws MailjetSocketTimeoutException, MailjetException {
            String email = person.getEmail();
            String token = RandomString.make(64);

            personDetailsService.updateResetPasswordToken(token, email);
            return new ResponseEntity<>("forgot_password_form", HttpStatus.ACCEPTED);

        }

        @GetMapping("/reset_password")
        public ResponseEntity<String> showResetPasswordForm (@Param(value = "token") String token){
            Person person = personDetailsService.getByResetPasswordToken(token);
            if (person == null) {
                return new ResponseEntity("invalid_token", HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity("reset_password_form", HttpStatus.ACCEPTED);
        }

        @PostMapping("/reset_password")
        public ResponseEntity<String> processResetPassword (@RequestBody Person person, @Param(value = "token") String
        token, HttpServletRequest request){
            String password = person.getPassword();
            Person person1 = personDetailsService.getByResetPasswordToken(token);
            if (person1 == null) {
                return new ResponseEntity("invalid_token", HttpStatus.BAD_REQUEST);
            } else {
                personDetailsService.updatePassword(person1, password);
            }
            return new ResponseEntity("You have successfully changed your password. ", HttpStatus.CREATED);
        }
}

