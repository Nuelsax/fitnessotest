package com.decagon.fitnessoapp.service;

import com.decagon.fitnessoapp.dto.PersonDto;
import com.decagon.fitnessoapp.model.user.Person;

public interface PersonService {

    Person register(PersonDto personDto);

    void sendingEmail(PersonDto personDto);

    String buildEmail(String name, String link);
}
