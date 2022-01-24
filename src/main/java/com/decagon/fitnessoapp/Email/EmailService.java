package com.decagon.fitnessoapp.Email;

import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.errors.MailjetSocketTimeoutException;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface EmailService {
//    ResponseEntity<String> sendMessage(String email, String text) throws UnirestException;
void sendMessage(String email, String text, String Cname) throws MailjetException, MailjetSocketTimeoutException;
}
