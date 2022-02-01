package com.decagon.fitnessoapp.service;

import com.decagon.fitnessoapp.dto.*;
import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.errors.MailjetSocketTimeoutException;
import org.springframework.http.ResponseEntity;

public interface PersonService {

    ResponseEntity<AuthResponse> loginUser(AuthRequest req) throws Exception;

    PersonResponse updateUserDetails(UpdatePersonDetails updatePersonDetails);

    PersonResponse register(PersonRequest personRequest) throws MailjetSocketTimeoutException, MailjetException;

    PersonResponse addTrainer(PersonRequest personRequest);

    ResponseEntity<String> removeTrainer(Long id);

    void sendingEmail(PersonRequest personRequest) throws MailjetSocketTimeoutException, MailjetException;

    String updateCurrentPassword(ChangePassword changePassword);

    String resetPasswordToken(String email) throws MailjetSocketTimeoutException, MailjetException;

    String updateResetPassword(ResetPasswordRequest passwordRequest, String token);

    void resetPasswordMailSender(String email, String token) throws MailjetSocketTimeoutException,
            MailjetException;

    String buildEmail(String name, String link);

}
