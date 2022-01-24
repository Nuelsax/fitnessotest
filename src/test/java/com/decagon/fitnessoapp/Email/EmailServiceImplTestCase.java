package com.decagon.fitnessoapp.Email;



import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.errors.MailjetSocketTimeoutException;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class EmailServiceImplTestCase {


    @Test
    public void sendMessage() throws MailjetSocketTimeoutException, MailjetException{
        String email ="chika@decagon.dev";
        String text ="how are you doing?";
        String Cname ="chika";
        EmailServiceImpl emailService;
        emailService = mock(EmailServiceImpl.class);
        doNothing().when(emailService).sendMessage(email,text,Cname);
        emailService.sendMessage(email,text,Cname);
        verify(emailService,times(1)).sendMessage(email,text,Cname);

    }
}
