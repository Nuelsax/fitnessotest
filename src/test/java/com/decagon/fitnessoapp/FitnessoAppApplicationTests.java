package com.decagon.fitnessoapp;

import com.decagon.fitnessoapp.model.user.Person;
import com.decagon.fitnessoapp.repository.PersonRepository;
import com.decagon.fitnessoapp.security.PersonDetailsService;
import com.decagon.fitnessoapp.security.PersonNotFoundException;
import net.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(classes ={FitnessoAppApplicationTests.class})
class FitnessoAppApplicationTests {

    @Mock
    PersonRepository personRepository;

    @InjectMocks
    PersonDetailsService personDetailsService;

    public Person person;

    @Test
    void contextLoads() {
    }

    @Test @Order(1)
    public void test_getByResetPasswordToken(){
        String token = RandomString.make(64);
        person = new Person(1L, "stella@gmail.com", "ellaboye", "Stella", "Ajiboye", token);
        when(personRepository.findByResetPasswordToken(token)).thenReturn(Optional.ofNullable(person));
        assertEquals(person, personDetailsService.getByResetPasswordToken(token));
    }

    @Test @Order(2)
    public void test_updateResetPasswordToken() throws PersonNotFoundException{
        String token = RandomString.make(64);
        person = new Person(1L, "stella@gmail.com", "ellaboye", "Stella", "Ajiboye",token);
        String email = "stella@gmail.com";

        personDetailsService = mock(PersonDetailsService.class);
        doNothing().when(personDetailsService).updateResetPasswordToken(anyString(), anyString());
        personDetailsService.updateResetPasswordToken(token, email);
        verify(personDetailsService, times(1)).updateResetPasswordToken(token, email);
    }

    @Test @Order(3)
    public void test_saveCustomer(){
        String token = RandomString.make(64);
        person = new Person(1L,"stella@gmail.com", "ellaboye", "Stella", "Ajiboye", token);
        when(personRepository.save(person)).thenReturn(person);
        assertEquals(person, personDetailsService.savePerson(person));
    }

    @Test @Order(4)
    public void test_updatePassword() throws PersonNotFoundException{
        String token = RandomString.make(64);
        person = new Person(1L,"stella@gmail.com", "ellaboye", "Stella", "Ajiboye", token);
        String email = "stella@gmail.com";

        personDetailsService = mock(PersonDetailsService.class);
        doNothing().when(personDetailsService).updatePassword(any(), anyString());
        personDetailsService.updatePassword(person, email);
        verify(personDetailsService, times(1)).updatePassword(person, email);
    }
}
