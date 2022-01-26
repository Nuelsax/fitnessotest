package com.decagon.fitnessoapp.service.serviceImplementation;

import org.springframework.stereotype.Service;

public interface EmailSender {

    void send(String subject, String to, String email);
}
