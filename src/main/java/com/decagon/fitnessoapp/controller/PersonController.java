package com.decagon.fitnessoapp.controller;

import com.decagon.fitnessoapp.model.user.Person;
import com.decagon.fitnessoapp.security.PersonDetailsService;
import com.decagon.fitnessoapp.security.PersonNotFoundException;
import com.decagon.fitnessoapp.utils.Utility;
import lombok.AllArgsConstructor;
import net.bytebuddy.utility.RandomString;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

@RestController
@AllArgsConstructor
public class PersonController {
    private final JavaMailSender mailSender;
    private final PersonDetailsService personDetailsService;
    private final Utility utility;


    @PostMapping("/forgot_password")
    public ResponseEntity<String> processForgotPassword(@RequestBody Person person, HttpServletRequest request)
        throws PersonNotFoundException, MessagingException, UnsupportedEncodingException{
        String email = person.getEmail();
        String token = RandomString.make(64);

        personDetailsService.updateResetPasswordToken(token, email);
        String resetPasswordLink = utility.getSiteURL(request) + "/reset_password?token=" + token;
        sendEmail(email, resetPasswordLink);
        return new ResponseEntity<>("forgot_password_form", HttpStatus.ACCEPTED);

    }

    public void sendEmail(String recipientEmail, String link) throws MessagingException, UnsupportedEncodingException{
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom("contact@resetpassword.com", "ResetPassword Support");
        helper.setTo(recipientEmail);
        String subject = "Here's the link to reset your password";
        String content = "<p>Hello,</p>"
                + "<p>You have requested to reset your password.</p>"
                + "<p>Click the link below to change your password:</p>"
                + "<p><a href=\"" + link + "\">Change my password</a></p>"
                + "<br>"
                + "<p> Ignore this email if you do remember your password, "
                + "or you have not made the request.</p>";
        helper.setSubject(subject);
        helper.setText(content, true);
        mailSender.send(message);
    }

    @GetMapping("/reset_password")
    public ResponseEntity<String> showResetPasswordForm(@Param(value = "token") String token){
        Person person = personDetailsService.getByResetPasswordToken(token);
        if (person == null){
            return new ResponseEntity("invalid_token", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity("reset_password_form", HttpStatus.ACCEPTED);
    }

    @PostMapping("/reset_password")
    public ResponseEntity<String> processResetPassword(@RequestBody Person person, @Param(value = "token") String token, HttpServletRequest request){
        String password = person.getPassword();
        Person person1 = personDetailsService.getByResetPasswordToken(token);
        if (person1 == null){
            return new ResponseEntity("invalid_token", HttpStatus.BAD_REQUEST);
        }else{
            personDetailsService.updatePassword(person1, password);
        }
        return new ResponseEntity("You have successfully changed your password. ", HttpStatus.CREATED);
    }

    @PostMapping("/create_person")
    public ResponseEntity<Person> createPerson(@RequestBody Person person){
        return new ResponseEntity<>(personDetailsService.savePerson(person), HttpStatus.CREATED);
    }

}

