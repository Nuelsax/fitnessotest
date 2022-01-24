package com.decagon.fitnessoapp;

import com.decagon.fitnessoapp.model.dto.ChangePassword;
import com.decagon.fitnessoapp.model.dto.UpdatePersonDetails;
import com.decagon.fitnessoapp.service.serviceImplementation.PersonServiceImpl;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

import static org.mockito.Mockito.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(classes = {UpdatePersonDetailsTest.class})
public class UpdatePersonDetailsTest {
    @Test
    void contextLoads() {
    }

    PersonServiceImpl personServiceImpl;

    @Test
    @Order(1)
    public void test_updateUserDetails(){

        Date date = new Date("08/07/2019");
        UpdatePersonDetails user = new UpdatePersonDetails("amara", "Amara", "Ojiakor", "amara@gamil.com", "female", date);

        personServiceImpl = mock(PersonServiceImpl.class);
        doNothing().when(personServiceImpl).updateUserDetails(any());
        personServiceImpl.updateUserDetails(user);
        verify(personServiceImpl, times(1)).updateUserDetails(user);

    }

    @Test
    @Order(2)
    public void test_updateUserPassword(){

        ChangePassword password = new ChangePassword("amara", "ammy", "ammy");

        personServiceImpl = mock(PersonServiceImpl.class);
        doNothing().when(personServiceImpl).updatePassword(any());
        personServiceImpl.updatePassword(password);
        verify(personServiceImpl, times(1)).updatePassword(password);

    }

}
