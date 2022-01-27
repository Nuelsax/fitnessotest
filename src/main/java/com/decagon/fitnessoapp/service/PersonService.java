package com.decagon.fitnessoapp.service;

import com.decagon.fitnessoapp.dto.ChangePassword;
import com.decagon.fitnessoapp.dto.PersonResponse;
import com.decagon.fitnessoapp.dto.UpdatePersonDetails;
import com.decagon.fitnessoapp.dto.PersonRequest;
import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.errors.MailjetSocketTimeoutException;


public interface PersonService {

    void updateUserDetails(UpdatePersonDetails updatePersonDetails);

    PersonResponse register(PersonRequest personDto) throws MailjetSocketTimeoutException, MailjetException;

    void sendingEmail(PersonRequest personDto) throws MailjetSocketTimeoutException, MailjetException;

    String buildEmail(String name, String link);

    void updateCurrentPassword(ChangePassword changePassword);
}
