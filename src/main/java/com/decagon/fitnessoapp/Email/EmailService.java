package com.decagon.fitnessoapp.Email;

import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.errors.MailjetSocketTimeoutException;

public interface EmailService {

void sendMessage(String subject, String email, String text) throws MailjetException, MailjetSocketTimeoutException;
}
