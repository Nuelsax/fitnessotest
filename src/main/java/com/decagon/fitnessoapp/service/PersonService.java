package com.decagon.fitnessoapp.service;

import com.decagon.fitnessoapp.model.dto.ChangePassword;
import com.decagon.fitnessoapp.model.dto.UpdatePersonDetails;
import com.decagon.fitnessoapp.dto.PersonDto;
import com.decagon.fitnessoapp.model.user.Person;


public interface PersonService {
    default void updatePassword(ChangePassword changePassword) {

    }

    default void updateUserDetails(UpdatePersonDetails updatePersonDetails) {

    }


    default Person register(PersonDto personDto) {
        return null;
    }

    default void sendingEmail(PersonDto personDto) {

    }

    default String buildEmail(String name, String link) {
        return null;
    }
}
