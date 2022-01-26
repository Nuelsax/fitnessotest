package com.decagon.fitnessoapp.service;

import com.decagon.fitnessoapp.dto.ChangePassword;
import com.decagon.fitnessoapp.dto.UpdatePersonDetails;
import com.decagon.fitnessoapp.dto.PersonDto;
import com.decagon.fitnessoapp.model.user.Person;
import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.errors.MailjetSocketTimeoutException;


public interface PersonService {

    default void updateUserDetails(UpdatePersonDetails updatePersonDetails) {

    }


    default Person register(PersonDto personDto) throws MailjetSocketTimeoutException, MailjetException {
        return null;
    }

    default void sendingEmail(PersonDto personDto) throws MailjetSocketTimeoutException, MailjetException {

    }

    default String buildEmail(String name, String link) {
        return null;
    }

    void updateCurrentPassword(ChangePassword changePassword);
}
