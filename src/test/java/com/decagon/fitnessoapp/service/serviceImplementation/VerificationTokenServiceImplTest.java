package com.decagon.fitnessoapp.service.serviceImplementation;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.decagon.fitnessoapp.model.user.Person;
import com.decagon.fitnessoapp.model.user.Role;
import com.decagon.fitnessoapp.model.user.VerificationToken;
import com.decagon.fitnessoapp.repository.PersonRepository;
import com.decagon.fitnessoapp.repository.VerificationTokenRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {VerificationTokenServiceImpl.class})
@ExtendWith(SpringExtension.class)
class VerificationTokenServiceImplTest {
    @MockBean
    private PersonRepository personRepository;

    @MockBean
    private VerificationTokenRepository verificationTokenRepository;

    @Autowired
    private VerificationTokenServiceImpl verificationTokenServiceImpl;

    @Test
    void testSaveVerificationToken() {
        Person person = new Person();
        person.setAddress(new ArrayList<>());
        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        person.setDateOfBirth(Date.from(atStartOfDayResult.atZone(ZoneId.of("UTC")).toInstant()));
        person.setEmail("jane.doe@example.org");
        person.setFirstName("Jane");
        person.setGender("Gender");
        person.setId(123L);
        person.setImage("Image");
        person.setLastName("Doe");
        person.setPassword("iloveyou");
        person.setPhoneNumber("4105551212");
        person.setRole(Role.ANONYMOUS);
        person.setUserName("janedoe");
        person.setVerifyEmail(true);

        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setConfirmedAt(LocalDateTime.of(1, 1, 1, 1, 1));
        verificationToken.setCreatedAt(LocalDateTime.of(1, 1, 1, 1, 1));
        verificationToken.setExpiresAt(LocalDateTime.of(1, 1, 1, 1, 1));
        verificationToken.setId(123L);
        verificationToken.setPerson(person);
        verificationToken.setTokenCode("ABC123");
        when(this.verificationTokenRepository.save((VerificationToken) any())).thenReturn(verificationToken);

        Person person1 = new Person();
        person1.setAddress(new ArrayList<>());
        LocalDateTime atStartOfDayResult1 = LocalDate.of(1970, 1, 1).atStartOfDay();
        person1.setDateOfBirth(Date.from(atStartOfDayResult1.atZone(ZoneId.of("UTC")).toInstant()));
        person1.setEmail("jane.doe@example.org");
        person1.setFirstName("Jane");
        person1.setGender("Gender");
        person1.setId(123L);
        person1.setImage("Image");
        person1.setLastName("Doe");
        person1.setPassword("iloveyou");
        person1.setPhoneNumber("4105551212");
        person1.setRole(Role.ANONYMOUS);
        person1.setUserName("janedoe");
        person1.setVerifyEmail(true);
        this.verificationTokenServiceImpl.saveVerificationToken(person1);
        verify(this.verificationTokenRepository).save((VerificationToken) any());
    }

    @Test
    void testSaveVerificationToken2() {
        when(this.verificationTokenRepository.save((VerificationToken) any())).thenThrow(new IllegalStateException("foo"));

        Person person = new Person();
        person.setAddress(new ArrayList<>());
        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        person.setDateOfBirth(Date.from(atStartOfDayResult.atZone(ZoneId.of("UTC")).toInstant()));
        person.setEmail("jane.doe@example.org");
        person.setFirstName("Jane");
        person.setGender("Gender");
        person.setId(123L);
        person.setImage("Image");
        person.setLastName("Doe");
        person.setPassword("iloveyou");
        person.setPhoneNumber("4105551212");
        person.setRole(Role.ANONYMOUS);
        person.setUserName("janedoe");
        person.setVerifyEmail(true);
        assertThrows(IllegalStateException.class, () -> this.verificationTokenServiceImpl.saveVerificationToken(person));
        verify(this.verificationTokenRepository).save((VerificationToken) any());
    }

    @Test
    void testGetToken() {
        Person person = new Person();
        person.setAddress(new ArrayList<>());
        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        person.setDateOfBirth(Date.from(atStartOfDayResult.atZone(ZoneId.of("UTC")).toInstant()));
        person.setEmail("jane.doe@example.org");
        person.setFirstName("Jane");
        person.setGender("Gender");
        person.setId(123L);
        person.setImage("Image");
        person.setLastName("Doe");
        person.setPassword("iloveyou");
        person.setPhoneNumber("4105551212");
        person.setRole(Role.ANONYMOUS);
        person.setUserName("janedoe");
        person.setVerifyEmail(true);

        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setConfirmedAt(LocalDateTime.of(1, 1, 1, 1, 1));
        verificationToken.setCreatedAt(LocalDateTime.of(1, 1, 1, 1, 1));
        verificationToken.setExpiresAt(LocalDateTime.of(1, 1, 1, 1, 1));
        verificationToken.setId(123L);
        verificationToken.setPerson(person);
        verificationToken.setTokenCode("ABC123");
        Optional<VerificationToken> ofResult = Optional.of(verificationToken);
        when(this.verificationTokenRepository.findByTokenCode((String) any())).thenReturn(ofResult);
        Optional<VerificationToken> actualToken = this.verificationTokenServiceImpl.getToken("ABC123");
        assertSame(ofResult, actualToken);
        assertTrue(actualToken.isPresent());
        verify(this.verificationTokenRepository).findByTokenCode((String) any());
    }

    @Test
    void testGetToken2() {
        when(this.verificationTokenRepository.findByTokenCode((String) any())).thenThrow(new IllegalStateException("foo"));
        assertThrows(IllegalStateException.class, () -> this.verificationTokenServiceImpl.getToken("ABC123"));
        verify(this.verificationTokenRepository).findByTokenCode((String) any());
    }

    @Test
    void testConfirmToken() {
        Person person = new Person();
        person.setAddress(new ArrayList<>());
        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        person.setDateOfBirth(Date.from(atStartOfDayResult.atZone(ZoneId.of("UTC")).toInstant()));
        person.setEmail("jane.doe@example.org");
        person.setFirstName("Jane");
        person.setGender("Gender");
        person.setId(123L);
        person.setImage("Image");
        person.setLastName("Doe");
        person.setPassword("iloveyou");
        person.setPhoneNumber("4105551212");
        person.setRole(Role.ANONYMOUS);
        person.setUserName("janedoe");
        person.setVerifyEmail(true);

        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setConfirmedAt(LocalDateTime.of(1, 1, 1, 1, 1));
        verificationToken.setCreatedAt(LocalDateTime.of(1, 1, 1, 1, 1));
        verificationToken.setExpiresAt(LocalDateTime.of(1, 1, 1, 1, 1));
        verificationToken.setId(123L);
        verificationToken.setPerson(person);
        verificationToken.setTokenCode("ABC123");
        Optional<VerificationToken> ofResult = Optional.of(verificationToken);
        when(this.verificationTokenRepository.findByTokenCode((String) any())).thenReturn(ofResult);
        assertThrows(IllegalStateException.class, () -> this.verificationTokenServiceImpl.confirmToken("ABC123"));
        verify(this.verificationTokenRepository).findByTokenCode((String) any());
    }

    @Test
    void testConfirmToken2() {
        when(this.verificationTokenRepository.findByTokenCode((String) any())).thenThrow(new IllegalStateException("foo"));
        assertThrows(IllegalStateException.class, () -> this.verificationTokenServiceImpl.confirmToken("ABC123"));
        verify(this.verificationTokenRepository).findByTokenCode((String) any());
    }

    @Test
    void testConfirmToken3() {
        Person person = new Person();
        person.setAddress(new ArrayList<>());
        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        person.setDateOfBirth(Date.from(atStartOfDayResult.atZone(ZoneId.of("UTC")).toInstant()));
        person.setEmail("jane.doe@example.org");
        person.setFirstName("Jane");
        person.setGender("Gender");
        person.setId(123L);
        person.setImage("Image");
        person.setLastName("Doe");
        person.setPassword("iloveyou");
        person.setPhoneNumber("4105551212");
        person.setRole(Role.ANONYMOUS);
        person.setUserName("janedoe");
        person.setVerifyEmail(true);

        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setConfirmedAt(null);
        verificationToken.setCreatedAt(LocalDateTime.of(1, 1, 1, 1, 1));
        verificationToken.setExpiresAt(LocalDateTime.of(1, 1, 1, 1, 1));
        verificationToken.setId(123L);
        verificationToken.setPerson(person);
        verificationToken.setTokenCode("ABC123");
        Optional<VerificationToken> ofResult = Optional.of(verificationToken);
        when(this.verificationTokenRepository.findByTokenCode((String) any())).thenReturn(ofResult);
        assertThrows(IllegalStateException.class, () -> this.verificationTokenServiceImpl.confirmToken("ABC123"));
        verify(this.verificationTokenRepository).findByTokenCode((String) any());
    }

    @Test
    void testConfirmToken4() {
        when(this.verificationTokenRepository.findByTokenCode((String) any())).thenReturn(Optional.empty());
        assertThrows(IllegalStateException.class, () -> this.verificationTokenServiceImpl.confirmToken("ABC123"));
        verify(this.verificationTokenRepository).findByTokenCode((String) any());
    }
}

