package com.decagon.fitnessoapp.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.decagon.fitnessoapp.Email.EmailService;
import com.decagon.fitnessoapp.dto.ChangePassword;
import com.decagon.fitnessoapp.dto.PersonDto;
import com.decagon.fitnessoapp.dto.UpdatePersonDetails;
import com.decagon.fitnessoapp.exception.CustomServiceExceptions;
import com.decagon.fitnessoapp.exceptions.PersonNotFoundException;
import com.decagon.fitnessoapp.model.user.Person;
import com.decagon.fitnessoapp.model.user.Role;
import com.decagon.fitnessoapp.model.user.VerificationToken;
import com.decagon.fitnessoapp.repository.PersonRepository;
import com.decagon.fitnessoapp.repository.VerificationTokenRepository;
import com.decagon.fitnessoapp.service.serviceImplementation.EmailValidator;
import com.decagon.fitnessoapp.service.serviceImplementation.VerificationTokenServiceImpl;
import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.errors.MailjetSocketTimeoutException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;

class PersonDetailsServiceTest {
    @Test
    void testLoadUserByUsername() throws UsernameNotFoundException {
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
        person.setResetPasswordToken("ABC123");
        person.setRole(Role.ANONYMOUS);
        person.setUserName("janedoe");
        person.setVerifyEmail(true);
        PersonRepository personRepository = mock(PersonRepository.class);
        when(personRepository.findByUserName((String) any())).thenReturn(Optional.of(person));
        VerificationTokenServiceImpl verificationTokenService = new VerificationTokenServiceImpl(
                mock(VerificationTokenRepository.class), mock(PersonRepository.class));

        Argon2PasswordEncoder bCryptPasswordEncoder = new Argon2PasswordEncoder();
        EmailValidator emailValidator = new EmailValidator();
        PersonDetails actualLoadUserByUsernameResult = (new PersonDetailsService(verificationTokenService,
                bCryptPasswordEncoder, personRepository, emailValidator, new ModelMapper(), mock(EmailService.class)))
                .loadUserByUsername("janedoe");
        Collection<? extends GrantedAuthority> authorities = actualLoadUserByUsernameResult.getAuthorities();
        assertEquals(1, authorities.size());
        assertTrue(actualLoadUserByUsernameResult.isEnabled());
        assertEquals("iloveyou", actualLoadUserByUsernameResult.getPassword());
        assertEquals("janedoe", actualLoadUserByUsernameResult.getUsername());
        assertEquals("ANONYMOUS", ((List<? extends GrantedAuthority>) authorities).get(0).getAuthority());
        verify(personRepository).findByUserName((String) any());
    }

    @Test
    void testLoadUserByUsername2() throws UsernameNotFoundException {
        PersonRepository personRepository = mock(PersonRepository.class);
        when(personRepository.findByUserName((String) any())).thenThrow(new CustomServiceExceptions("An error occurred"));
        VerificationTokenServiceImpl verificationTokenService = new VerificationTokenServiceImpl(
                mock(VerificationTokenRepository.class), mock(PersonRepository.class));

        Argon2PasswordEncoder bCryptPasswordEncoder = new Argon2PasswordEncoder();
        EmailValidator emailValidator = new EmailValidator();
        assertThrows(CustomServiceExceptions.class,
                () -> (new PersonDetailsService(verificationTokenService, bCryptPasswordEncoder, personRepository,
                        emailValidator, new ModelMapper(), mock(EmailService.class))).loadUserByUsername("janedoe"));
        verify(personRepository).findByUserName((String) any());
    }

    @Test
    void testLoadUserByUsername3() throws UsernameNotFoundException {
        PersonRepository personRepository = mock(PersonRepository.class);
        when(personRepository.findByUserName((String) any())).thenReturn(Optional.empty());
        VerificationTokenServiceImpl verificationTokenService = new VerificationTokenServiceImpl(
                mock(VerificationTokenRepository.class), mock(PersonRepository.class));

        Argon2PasswordEncoder bCryptPasswordEncoder = new Argon2PasswordEncoder();
        EmailValidator emailValidator = new EmailValidator();
        assertThrows(UsernameNotFoundException.class,
                () -> (new PersonDetailsService(verificationTokenService, bCryptPasswordEncoder, personRepository,
                        emailValidator, new ModelMapper(), mock(EmailService.class))).loadUserByUsername("janedoe"));
        verify(personRepository).findByUserName((String) any());
    }

    @Test
    void testLoadUserByUsername4() throws UsernameNotFoundException {
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
        person.setResetPasswordToken("ABC123");
        person.setRole(Role.ANONYMOUS);
        person.setUserName("janedoe");
        person.setVerifyEmail(true);
        PersonRepository personRepository = mock(PersonRepository.class);
        when(personRepository.findByUserName((String) any())).thenReturn(Optional.of(person));
        VerificationTokenServiceImpl verificationTokenService = new VerificationTokenServiceImpl(
                mock(VerificationTokenRepository.class), mock(PersonRepository.class));

        Argon2PasswordEncoder bCryptPasswordEncoder = new Argon2PasswordEncoder();
        EmailValidator emailValidator = new EmailValidator();
        PersonDetails actualLoadUserByUsernameResult = (new PersonDetailsService(verificationTokenService,
                bCryptPasswordEncoder, personRepository, emailValidator, new ModelMapper(), mock(EmailService.class)))
                .loadUserByUsername("janedoe");
        Collection<? extends GrantedAuthority> authorities = actualLoadUserByUsernameResult.getAuthorities();
        assertEquals(1, authorities.size());
        assertTrue(actualLoadUserByUsernameResult.isEnabled());
        assertEquals("iloveyou", actualLoadUserByUsernameResult.getPassword());
        assertEquals("janedoe", actualLoadUserByUsernameResult.getUsername());
        assertEquals("ANONYMOUS", ((List<? extends GrantedAuthority>) authorities).get(0).getAuthority());
        verify(personRepository).findByUserName((String) any());
    }

    @Test
    void testLoadUserByUsername5() throws UsernameNotFoundException {
        PersonRepository personRepository = mock(PersonRepository.class);
        when(personRepository.findByUserName((String) any())).thenThrow(new CustomServiceExceptions("An error occurred"));
        VerificationTokenServiceImpl verificationTokenService = new VerificationTokenServiceImpl(
                mock(VerificationTokenRepository.class), mock(PersonRepository.class));

        Argon2PasswordEncoder bCryptPasswordEncoder = new Argon2PasswordEncoder();
        EmailValidator emailValidator = new EmailValidator();
        assertThrows(CustomServiceExceptions.class,
                () -> (new PersonDetailsService(verificationTokenService, bCryptPasswordEncoder, personRepository,
                        emailValidator, new ModelMapper(), mock(EmailService.class))).loadUserByUsername("janedoe"));
        verify(personRepository).findByUserName((String) any());
    }

    @Test
    void testLoadUserByUsername6() throws UsernameNotFoundException {
        PersonRepository personRepository = mock(PersonRepository.class);
        when(personRepository.findByUserName((String) any())).thenReturn(Optional.empty());
        VerificationTokenServiceImpl verificationTokenService = new VerificationTokenServiceImpl(
                mock(VerificationTokenRepository.class), mock(PersonRepository.class));

        Argon2PasswordEncoder bCryptPasswordEncoder = new Argon2PasswordEncoder();
        EmailValidator emailValidator = new EmailValidator();
        assertThrows(UsernameNotFoundException.class,
                () -> (new PersonDetailsService(verificationTokenService, bCryptPasswordEncoder, personRepository,
                        emailValidator, new ModelMapper(), mock(EmailService.class))).loadUserByUsername("janedoe"));
        verify(personRepository).findByUserName((String) any());
    }

    @Test
    void testRegister() throws MailjetException, MailjetSocketTimeoutException {
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
        person.setResetPasswordToken("ABC123");
        person.setRole(Role.ANONYMOUS);
        person.setUserName("janedoe");
        person.setVerifyEmail(true);
        PersonRepository personRepository = mock(PersonRepository.class);
        when(personRepository.findByEmail((String) any())).thenReturn(Optional.of(person));
        VerificationTokenServiceImpl verificationTokenService = new VerificationTokenServiceImpl(
                mock(VerificationTokenRepository.class), mock(PersonRepository.class));

        Argon2PasswordEncoder bCryptPasswordEncoder = new Argon2PasswordEncoder();
        EmailValidator emailValidator = new EmailValidator();
        PersonDetailsService personDetailsService = new PersonDetailsService(verificationTokenService,
                bCryptPasswordEncoder, personRepository, emailValidator, new ModelMapper(), mock(EmailService.class));

        PersonDto personDto = new PersonDto();
        personDto.setAddress(new ArrayList<>());
        LocalDateTime atStartOfDayResult1 = LocalDate.of(1970, 1, 1).atStartOfDay();
        personDto.setDateOfBirth(Date.from(atStartOfDayResult1.atZone(ZoneId.of("UTC")).toInstant()));
        personDto.setEmail("jane.doe@example.org");
        personDto.setFirstName("Jane");
        personDto.setGender("Gender");
        personDto.setImage("Image");
        personDto.setLastName("Doe");
        personDto.setPassword("iloveyou");
        personDto.setPhoneNumber("4105551212");
        personDto.setRole(Role.ANONYMOUS);
        personDto.setUserName("janedoe");
        personDto.setVerifyEmail(true);
        assertThrows(CustomServiceExceptions.class, () -> personDetailsService.register(personDto));
        verify(personRepository).findByEmail((String) any());
    }

    @Test
    void testRegister2() throws MailjetException, MailjetSocketTimeoutException {
        PersonRepository personRepository = mock(PersonRepository.class);
        when(personRepository.findByEmail((String) any())).thenThrow(new CustomServiceExceptions("An error occurred"));
        VerificationTokenServiceImpl verificationTokenService = new VerificationTokenServiceImpl(
                mock(VerificationTokenRepository.class), mock(PersonRepository.class));

        Argon2PasswordEncoder bCryptPasswordEncoder = new Argon2PasswordEncoder();
        EmailValidator emailValidator = new EmailValidator();
        PersonDetailsService personDetailsService = new PersonDetailsService(verificationTokenService,
                bCryptPasswordEncoder, personRepository, emailValidator, new ModelMapper(), mock(EmailService.class));

        PersonDto personDto = new PersonDto();
        personDto.setAddress(new ArrayList<>());
        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        personDto.setDateOfBirth(Date.from(atStartOfDayResult.atZone(ZoneId.of("UTC")).toInstant()));
        personDto.setEmail("jane.doe@example.org");
        personDto.setFirstName("Jane");
        personDto.setGender("Gender");
        personDto.setImage("Image");
        personDto.setLastName("Doe");
        personDto.setPassword("iloveyou");
        personDto.setPhoneNumber("4105551212");
        personDto.setRole(Role.ANONYMOUS);
        personDto.setUserName("janedoe");
        personDto.setVerifyEmail(true);
        assertThrows(CustomServiceExceptions.class, () -> personDetailsService.register(personDto));
        verify(personRepository).findByEmail((String) any());
    }

    @Test
    void testRegister3() throws MailjetException, MailjetSocketTimeoutException {
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
        person.setResetPasswordToken("ABC123");
        person.setRole(Role.ANONYMOUS);
        person.setUserName("janedoe");
        person.setVerifyEmail(true);
        PersonRepository personRepository = mock(PersonRepository.class);
        when(personRepository.findByEmail((String) any())).thenReturn(Optional.of(person));
        EmailValidator emailValidator = mock(EmailValidator.class);
        when(emailValidator.test((String) any())).thenReturn(true);
        VerificationTokenServiceImpl verificationTokenService = new VerificationTokenServiceImpl(
                mock(VerificationTokenRepository.class), mock(PersonRepository.class));

        Argon2PasswordEncoder bCryptPasswordEncoder = new Argon2PasswordEncoder();
        PersonDetailsService personDetailsService = new PersonDetailsService(verificationTokenService,
                bCryptPasswordEncoder, personRepository, emailValidator, new ModelMapper(), mock(EmailService.class));

        PersonDto personDto = new PersonDto();
        personDto.setAddress(new ArrayList<>());
        LocalDateTime atStartOfDayResult1 = LocalDate.of(1970, 1, 1).atStartOfDay();
        personDto.setDateOfBirth(Date.from(atStartOfDayResult1.atZone(ZoneId.of("UTC")).toInstant()));
        personDto.setEmail("jane.doe@example.org");
        personDto.setFirstName("Jane");
        personDto.setGender("Gender");
        personDto.setImage("Image");
        personDto.setLastName("Doe");
        personDto.setPassword("iloveyou");
        personDto.setPhoneNumber("4105551212");
        personDto.setRole(Role.ANONYMOUS);
        personDto.setUserName("janedoe");
        personDto.setVerifyEmail(true);
        assertThrows(CustomServiceExceptions.class, () -> personDetailsService.register(personDto));
        verify(personRepository).findByEmail((String) any());
        verify(emailValidator).test((String) any());
    }

    @Test
    void testRegister4() throws MailjetException, MailjetSocketTimeoutException {
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
        person.setResetPasswordToken("ABC123");
        person.setRole(Role.ANONYMOUS);
        person.setUserName("janedoe");
        person.setVerifyEmail(true);
        PersonRepository personRepository = mock(PersonRepository.class);
        when(personRepository.findByEmail((String) any())).thenReturn(Optional.of(person));
        EmailValidator emailValidator = mock(EmailValidator.class);
        when(emailValidator.test((String) any())).thenReturn(false);
        VerificationTokenServiceImpl verificationTokenService = new VerificationTokenServiceImpl(
                mock(VerificationTokenRepository.class), mock(PersonRepository.class));

        Argon2PasswordEncoder bCryptPasswordEncoder = new Argon2PasswordEncoder();
        PersonDetailsService personDetailsService = new PersonDetailsService(verificationTokenService,
                bCryptPasswordEncoder, personRepository, emailValidator, new ModelMapper(), mock(EmailService.class));

        PersonDto personDto = new PersonDto();
        personDto.setAddress(new ArrayList<>());
        LocalDateTime atStartOfDayResult1 = LocalDate.of(1970, 1, 1).atStartOfDay();
        personDto.setDateOfBirth(Date.from(atStartOfDayResult1.atZone(ZoneId.of("UTC")).toInstant()));
        personDto.setEmail("jane.doe@example.org");
        personDto.setFirstName("Jane");
        personDto.setGender("Gender");
        personDto.setImage("Image");
        personDto.setLastName("Doe");
        personDto.setPassword("iloveyou");
        personDto.setPhoneNumber("4105551212");
        personDto.setRole(Role.ANONYMOUS);
        personDto.setUserName("janedoe");
        personDto.setVerifyEmail(true);
        assertThrows(CustomServiceExceptions.class, () -> personDetailsService.register(personDto));
        verify(emailValidator).test((String) any());
    }

    @Test
    void testRegister5() throws MailjetException, MailjetSocketTimeoutException {
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
        person.setResetPasswordToken("ABC123");
        person.setRole(Role.ANONYMOUS);
        person.setUserName("janedoe");
        person.setVerifyEmail(true);
        PersonRepository personRepository = mock(PersonRepository.class);
        when(personRepository.findByEmail((String) any())).thenReturn(Optional.of(person));
        VerificationTokenServiceImpl verificationTokenService = new VerificationTokenServiceImpl(
                mock(VerificationTokenRepository.class), mock(PersonRepository.class));

        Argon2PasswordEncoder bCryptPasswordEncoder = new Argon2PasswordEncoder();
        EmailValidator emailValidator = new EmailValidator();
        PersonDetailsService personDetailsService = new PersonDetailsService(verificationTokenService,
                bCryptPasswordEncoder, personRepository, emailValidator, new ModelMapper(), mock(EmailService.class));

        PersonDto personDto = new PersonDto();
        personDto.setAddress(new ArrayList<>());
        LocalDateTime atStartOfDayResult1 = LocalDate.of(1970, 1, 1).atStartOfDay();
        personDto.setDateOfBirth(Date.from(atStartOfDayResult1.atZone(ZoneId.of("UTC")).toInstant()));
        personDto.setEmail("jane.doe@example.org");
        personDto.setFirstName("Jane");
        personDto.setGender("Gender");
        personDto.setImage("Image");
        personDto.setLastName("Doe");
        personDto.setPassword("iloveyou");
        personDto.setPhoneNumber("4105551212");
        personDto.setRole(Role.ANONYMOUS);
        personDto.setUserName("janedoe");
        personDto.setVerifyEmail(true);
        assertThrows(CustomServiceExceptions.class, () -> personDetailsService.register(personDto));
        verify(personRepository).findByEmail((String) any());
    }

    @Test
    void testRegister6() throws MailjetException, MailjetSocketTimeoutException {
        PersonRepository personRepository = mock(PersonRepository.class);
        when(personRepository.findByEmail((String) any())).thenThrow(new CustomServiceExceptions("An error occurred"));
        VerificationTokenServiceImpl verificationTokenService = new VerificationTokenServiceImpl(
                mock(VerificationTokenRepository.class), mock(PersonRepository.class));

        Argon2PasswordEncoder bCryptPasswordEncoder = new Argon2PasswordEncoder();
        EmailValidator emailValidator = new EmailValidator();
        PersonDetailsService personDetailsService = new PersonDetailsService(verificationTokenService,
                bCryptPasswordEncoder, personRepository, emailValidator, new ModelMapper(), mock(EmailService.class));

        PersonDto personDto = new PersonDto();
        personDto.setAddress(new ArrayList<>());
        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        personDto.setDateOfBirth(Date.from(atStartOfDayResult.atZone(ZoneId.of("UTC")).toInstant()));
        personDto.setEmail("jane.doe@example.org");
        personDto.setFirstName("Jane");
        personDto.setGender("Gender");
        personDto.setImage("Image");
        personDto.setLastName("Doe");
        personDto.setPassword("iloveyou");
        personDto.setPhoneNumber("4105551212");
        personDto.setRole(Role.ANONYMOUS);
        personDto.setUserName("janedoe");
        personDto.setVerifyEmail(true);
        assertThrows(CustomServiceExceptions.class, () -> personDetailsService.register(personDto));
        verify(personRepository).findByEmail((String) any());
    }

    @Test
    void testRegister7() throws MailjetException, MailjetSocketTimeoutException {
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
        person.setResetPasswordToken("ABC123");
        person.setRole(Role.ANONYMOUS);
        person.setUserName("janedoe");
        person.setVerifyEmail(true);
        PersonRepository personRepository = mock(PersonRepository.class);
        when(personRepository.findByEmail((String) any())).thenReturn(Optional.of(person));
        EmailValidator emailValidator = mock(EmailValidator.class);
        when(emailValidator.test((String) any())).thenReturn(true);
        VerificationTokenServiceImpl verificationTokenService = new VerificationTokenServiceImpl(
                mock(VerificationTokenRepository.class), mock(PersonRepository.class));

        Argon2PasswordEncoder bCryptPasswordEncoder = new Argon2PasswordEncoder();
        PersonDetailsService personDetailsService = new PersonDetailsService(verificationTokenService,
                bCryptPasswordEncoder, personRepository, emailValidator, new ModelMapper(), mock(EmailService.class));

        PersonDto personDto = new PersonDto();
        personDto.setAddress(new ArrayList<>());
        LocalDateTime atStartOfDayResult1 = LocalDate.of(1970, 1, 1).atStartOfDay();
        personDto.setDateOfBirth(Date.from(atStartOfDayResult1.atZone(ZoneId.of("UTC")).toInstant()));
        personDto.setEmail("jane.doe@example.org");
        personDto.setFirstName("Jane");
        personDto.setGender("Gender");
        personDto.setImage("Image");
        personDto.setLastName("Doe");
        personDto.setPassword("iloveyou");
        personDto.setPhoneNumber("4105551212");
        personDto.setRole(Role.ANONYMOUS);
        personDto.setUserName("janedoe");
        personDto.setVerifyEmail(true);
        assertThrows(CustomServiceExceptions.class, () -> personDetailsService.register(personDto));
        verify(personRepository).findByEmail((String) any());
        verify(emailValidator).test((String) any());
    }

    @Test
    void testRegister8() throws MailjetException, MailjetSocketTimeoutException {
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
        person.setResetPasswordToken("ABC123");
        person.setRole(Role.ANONYMOUS);
        person.setUserName("janedoe");
        person.setVerifyEmail(true);
        PersonRepository personRepository = mock(PersonRepository.class);
        when(personRepository.findByEmail((String) any())).thenReturn(Optional.of(person));
        EmailValidator emailValidator = mock(EmailValidator.class);
        when(emailValidator.test((String) any())).thenReturn(false);
        VerificationTokenServiceImpl verificationTokenService = new VerificationTokenServiceImpl(
                mock(VerificationTokenRepository.class), mock(PersonRepository.class));

        Argon2PasswordEncoder bCryptPasswordEncoder = new Argon2PasswordEncoder();
        PersonDetailsService personDetailsService = new PersonDetailsService(verificationTokenService,
                bCryptPasswordEncoder, personRepository, emailValidator, new ModelMapper(), mock(EmailService.class));

        PersonDto personDto = new PersonDto();
        personDto.setAddress(new ArrayList<>());
        LocalDateTime atStartOfDayResult1 = LocalDate.of(1970, 1, 1).atStartOfDay();
        personDto.setDateOfBirth(Date.from(atStartOfDayResult1.atZone(ZoneId.of("UTC")).toInstant()));
        personDto.setEmail("jane.doe@example.org");
        personDto.setFirstName("Jane");
        personDto.setGender("Gender");
        personDto.setImage("Image");
        personDto.setLastName("Doe");
        personDto.setPassword("iloveyou");
        personDto.setPhoneNumber("4105551212");
        personDto.setRole(Role.ANONYMOUS);
        personDto.setUserName("janedoe");
        personDto.setVerifyEmail(true);
        assertThrows(CustomServiceExceptions.class, () -> personDetailsService.register(personDto));
        verify(emailValidator).test((String) any());
    }

    @Test
    void testSendingEmail() throws MailjetException, MailjetSocketTimeoutException {
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
        person.setResetPasswordToken("ABC123");
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
        VerificationTokenRepository verificationTokenRepository = mock(VerificationTokenRepository.class);
        when(verificationTokenRepository.save((VerificationToken) any())).thenReturn(verificationToken);
        VerificationTokenServiceImpl verificationTokenService = new VerificationTokenServiceImpl(
                verificationTokenRepository, mock(PersonRepository.class));

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
        person1.setResetPasswordToken("ABC123");
        person1.setRole(Role.ANONYMOUS);
        person1.setUserName("janedoe");
        person1.setVerifyEmail(true);
        PersonRepository personRepository = mock(PersonRepository.class);
        when(personRepository.findByEmail((String) any())).thenReturn(Optional.of(person1));
        EmailService emailService = mock(EmailService.class);
        doNothing().when(emailService).sendMessage((String) any(), (String) any(), (String) any());
        Argon2PasswordEncoder bCryptPasswordEncoder = new Argon2PasswordEncoder();
        EmailValidator emailValidator = new EmailValidator();
        PersonDetailsService personDetailsService = new PersonDetailsService(verificationTokenService,
                bCryptPasswordEncoder, personRepository, emailValidator, new ModelMapper(), emailService);

        PersonDto personDto = new PersonDto();
        personDto.setAddress(new ArrayList<>());
        LocalDateTime atStartOfDayResult2 = LocalDate.of(1970, 1, 1).atStartOfDay();
        personDto.setDateOfBirth(Date.from(atStartOfDayResult2.atZone(ZoneId.of("UTC")).toInstant()));
        personDto.setEmail("jane.doe@example.org");
        personDto.setFirstName("Jane");
        personDto.setGender("Gender");
        personDto.setImage("Image");
        personDto.setLastName("Doe");
        personDto.setPassword("iloveyou");
        personDto.setPhoneNumber("4105551212");
        personDto.setRole(Role.ANONYMOUS);
        personDto.setUserName("janedoe");
        personDto.setVerifyEmail(true);
        personDetailsService.sendingEmail(personDto);
        verify(verificationTokenRepository).save((VerificationToken) any());
        verify(personRepository).findByEmail((String) any());
        verify(emailService).sendMessage((String) any(), (String) any(), (String) any());
    }

    @Test
    void testSendingEmail2() throws MailjetException, MailjetSocketTimeoutException {
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
        person.setResetPasswordToken("ABC123");
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
        VerificationTokenRepository verificationTokenRepository = mock(VerificationTokenRepository.class);
        when(verificationTokenRepository.save((VerificationToken) any())).thenReturn(verificationToken);
        VerificationTokenServiceImpl verificationTokenService = new VerificationTokenServiceImpl(
                verificationTokenRepository, mock(PersonRepository.class));

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
        person1.setResetPasswordToken("ABC123");
        person1.setRole(Role.ANONYMOUS);
        person1.setUserName("janedoe");
        person1.setVerifyEmail(true);
        PersonRepository personRepository = mock(PersonRepository.class);
        when(personRepository.findByEmail((String) any())).thenReturn(Optional.of(person1));
        EmailService emailService = mock(EmailService.class);
        doThrow(new CustomServiceExceptions("An error occurred")).when(emailService)
                .sendMessage((String) any(), (String) any(), (String) any());
        Argon2PasswordEncoder bCryptPasswordEncoder = new Argon2PasswordEncoder();
        EmailValidator emailValidator = new EmailValidator();
        PersonDetailsService personDetailsService = new PersonDetailsService(verificationTokenService,
                bCryptPasswordEncoder, personRepository, emailValidator, new ModelMapper(), emailService);

        PersonDto personDto = new PersonDto();
        personDto.setAddress(new ArrayList<>());
        LocalDateTime atStartOfDayResult2 = LocalDate.of(1970, 1, 1).atStartOfDay();
        personDto.setDateOfBirth(Date.from(atStartOfDayResult2.atZone(ZoneId.of("UTC")).toInstant()));
        personDto.setEmail("jane.doe@example.org");
        personDto.setFirstName("Jane");
        personDto.setGender("Gender");
        personDto.setImage("Image");
        personDto.setLastName("Doe");
        personDto.setPassword("iloveyou");
        personDto.setPhoneNumber("4105551212");
        personDto.setRole(Role.ANONYMOUS);
        personDto.setUserName("janedoe");
        personDto.setVerifyEmail(true);
        assertThrows(CustomServiceExceptions.class, () -> personDetailsService.sendingEmail(personDto));
        verify(verificationTokenRepository).save((VerificationToken) any());
        verify(personRepository).findByEmail((String) any());
        verify(emailService).sendMessage((String) any(), (String) any(), (String) any());
    }

    @Test
    void testSendingEmail3() throws MailjetException, MailjetSocketTimeoutException {
        VerificationTokenServiceImpl verificationTokenServiceImpl = mock(VerificationTokenServiceImpl.class);
        when(verificationTokenServiceImpl.saveVerificationToken((Person) any())).thenReturn("ABC123");

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
        person.setResetPasswordToken("ABC123");
        person.setRole(Role.ANONYMOUS);
        person.setUserName("janedoe");
        person.setVerifyEmail(true);
        PersonRepository personRepository = mock(PersonRepository.class);
        when(personRepository.findByEmail((String) any())).thenReturn(Optional.of(person));
        EmailService emailService = mock(EmailService.class);
        doNothing().when(emailService).sendMessage((String) any(), (String) any(), (String) any());
        Argon2PasswordEncoder bCryptPasswordEncoder = new Argon2PasswordEncoder();
        EmailValidator emailValidator = new EmailValidator();
        PersonDetailsService personDetailsService = new PersonDetailsService(verificationTokenServiceImpl,
                bCryptPasswordEncoder, personRepository, emailValidator, new ModelMapper(), emailService);

        PersonDto personDto = new PersonDto();
        personDto.setAddress(new ArrayList<>());
        LocalDateTime atStartOfDayResult1 = LocalDate.of(1970, 1, 1).atStartOfDay();
        personDto.setDateOfBirth(Date.from(atStartOfDayResult1.atZone(ZoneId.of("UTC")).toInstant()));
        personDto.setEmail("jane.doe@example.org");
        personDto.setFirstName("Jane");
        personDto.setGender("Gender");
        personDto.setImage("Image");
        personDto.setLastName("Doe");
        personDto.setPassword("iloveyou");
        personDto.setPhoneNumber("4105551212");
        personDto.setRole(Role.ANONYMOUS);
        personDto.setUserName("janedoe");
        personDto.setVerifyEmail(true);
        personDetailsService.sendingEmail(personDto);
        verify(verificationTokenServiceImpl).saveVerificationToken((Person) any());
        verify(personRepository).findByEmail((String) any());
        verify(emailService).sendMessage((String) any(), (String) any(), (String) any());
    }

    @Test
    void testSendingEmail4() throws MailjetException, MailjetSocketTimeoutException {
        VerificationTokenServiceImpl verificationTokenServiceImpl = mock(VerificationTokenServiceImpl.class);
        when(verificationTokenServiceImpl.saveVerificationToken((Person) any())).thenReturn("ABC123");
        PersonRepository personRepository = mock(PersonRepository.class);
        when(personRepository.findByEmail((String) any())).thenReturn(Optional.empty());
        EmailService emailService = mock(EmailService.class);
        doNothing().when(emailService).sendMessage((String) any(), (String) any(), (String) any());
        Argon2PasswordEncoder bCryptPasswordEncoder = new Argon2PasswordEncoder();
        EmailValidator emailValidator = new EmailValidator();
        PersonDetailsService personDetailsService = new PersonDetailsService(verificationTokenServiceImpl,
                bCryptPasswordEncoder, personRepository, emailValidator, new ModelMapper(), emailService);

        PersonDto personDto = new PersonDto();
        personDto.setAddress(new ArrayList<>());
        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        personDto.setDateOfBirth(Date.from(atStartOfDayResult.atZone(ZoneId.of("UTC")).toInstant()));
        personDto.setEmail("jane.doe@example.org");
        personDto.setFirstName("Jane");
        personDto.setGender("Gender");
        personDto.setImage("Image");
        personDto.setLastName("Doe");
        personDto.setPassword("iloveyou");
        personDto.setPhoneNumber("4105551212");
        personDto.setRole(Role.ANONYMOUS);
        personDto.setUserName("janedoe");
        personDto.setVerifyEmail(true);
        assertThrows(CustomServiceExceptions.class, () -> personDetailsService.sendingEmail(personDto));
        verify(personRepository).findByEmail((String) any());
    }

    @Test
    void testSendingEmail5() throws MailjetException, MailjetSocketTimeoutException {
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
        person.setResetPasswordToken("ABC123");
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
        VerificationTokenRepository verificationTokenRepository = mock(VerificationTokenRepository.class);
        when(verificationTokenRepository.save((VerificationToken) any())).thenReturn(verificationToken);
        VerificationTokenServiceImpl verificationTokenService = new VerificationTokenServiceImpl(
                verificationTokenRepository, mock(PersonRepository.class));

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
        person1.setResetPasswordToken("ABC123");
        person1.setRole(Role.ANONYMOUS);
        person1.setUserName("janedoe");
        person1.setVerifyEmail(true);
        PersonRepository personRepository = mock(PersonRepository.class);
        when(personRepository.findByEmail((String) any())).thenReturn(Optional.of(person1));
        EmailService emailService = mock(EmailService.class);
        doNothing().when(emailService).sendMessage((String) any(), (String) any(), (String) any());
        Argon2PasswordEncoder bCryptPasswordEncoder = new Argon2PasswordEncoder();
        EmailValidator emailValidator = new EmailValidator();
        PersonDetailsService personDetailsService = new PersonDetailsService(verificationTokenService,
                bCryptPasswordEncoder, personRepository, emailValidator, new ModelMapper(), emailService);

        PersonDto personDto = new PersonDto();
        personDto.setAddress(new ArrayList<>());
        LocalDateTime atStartOfDayResult2 = LocalDate.of(1970, 1, 1).atStartOfDay();
        personDto.setDateOfBirth(Date.from(atStartOfDayResult2.atZone(ZoneId.of("UTC")).toInstant()));
        personDto.setEmail("jane.doe@example.org");
        personDto.setFirstName("Jane");
        personDto.setGender("Gender");
        personDto.setImage("Image");
        personDto.setLastName("Doe");
        personDto.setPassword("iloveyou");
        personDto.setPhoneNumber("4105551212");
        personDto.setRole(Role.ANONYMOUS);
        personDto.setUserName("janedoe");
        personDto.setVerifyEmail(true);
        personDetailsService.sendingEmail(personDto);
        verify(verificationTokenRepository).save((VerificationToken) any());
        verify(personRepository).findByEmail((String) any());
        verify(emailService).sendMessage((String) any(), (String) any(), (String) any());
    }

    @Test
    void testSendingEmail6() throws MailjetException, MailjetSocketTimeoutException {
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
        person.setResetPasswordToken("ABC123");
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
        VerificationTokenRepository verificationTokenRepository = mock(VerificationTokenRepository.class);
        when(verificationTokenRepository.save((VerificationToken) any())).thenReturn(verificationToken);
        VerificationTokenServiceImpl verificationTokenService = new VerificationTokenServiceImpl(
                verificationTokenRepository, mock(PersonRepository.class));

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
        person1.setResetPasswordToken("ABC123");
        person1.setRole(Role.ANONYMOUS);
        person1.setUserName("janedoe");
        person1.setVerifyEmail(true);
        PersonRepository personRepository = mock(PersonRepository.class);
        when(personRepository.findByEmail((String) any())).thenReturn(Optional.of(person1));
        EmailService emailService = mock(EmailService.class);
        doThrow(new CustomServiceExceptions("An error occurred")).when(emailService)
                .sendMessage((String) any(), (String) any(), (String) any());
        Argon2PasswordEncoder bCryptPasswordEncoder = new Argon2PasswordEncoder();
        EmailValidator emailValidator = new EmailValidator();
        PersonDetailsService personDetailsService = new PersonDetailsService(verificationTokenService,
                bCryptPasswordEncoder, personRepository, emailValidator, new ModelMapper(), emailService);

        PersonDto personDto = new PersonDto();
        personDto.setAddress(new ArrayList<>());
        LocalDateTime atStartOfDayResult2 = LocalDate.of(1970, 1, 1).atStartOfDay();
        personDto.setDateOfBirth(Date.from(atStartOfDayResult2.atZone(ZoneId.of("UTC")).toInstant()));
        personDto.setEmail("jane.doe@example.org");
        personDto.setFirstName("Jane");
        personDto.setGender("Gender");
        personDto.setImage("Image");
        personDto.setLastName("Doe");
        personDto.setPassword("iloveyou");
        personDto.setPhoneNumber("4105551212");
        personDto.setRole(Role.ANONYMOUS);
        personDto.setUserName("janedoe");
        personDto.setVerifyEmail(true);
        assertThrows(CustomServiceExceptions.class, () -> personDetailsService.sendingEmail(personDto));
        verify(verificationTokenRepository).save((VerificationToken) any());
        verify(personRepository).findByEmail((String) any());
        verify(emailService).sendMessage((String) any(), (String) any(), (String) any());
    }

    @Test
    void testSendingEmail7() throws MailjetException, MailjetSocketTimeoutException {
        VerificationTokenServiceImpl verificationTokenServiceImpl = mock(VerificationTokenServiceImpl.class);
        when(verificationTokenServiceImpl.saveVerificationToken((Person) any())).thenReturn("ABC123");

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
        person.setResetPasswordToken("ABC123");
        person.setRole(Role.ANONYMOUS);
        person.setUserName("janedoe");
        person.setVerifyEmail(true);
        PersonRepository personRepository = mock(PersonRepository.class);
        when(personRepository.findByEmail((String) any())).thenReturn(Optional.of(person));
        EmailService emailService = mock(EmailService.class);
        doNothing().when(emailService).sendMessage((String) any(), (String) any(), (String) any());
        Argon2PasswordEncoder bCryptPasswordEncoder = new Argon2PasswordEncoder();
        EmailValidator emailValidator = new EmailValidator();
        PersonDetailsService personDetailsService = new PersonDetailsService(verificationTokenServiceImpl,
                bCryptPasswordEncoder, personRepository, emailValidator, new ModelMapper(), emailService);

        PersonDto personDto = new PersonDto();
        personDto.setAddress(new ArrayList<>());
        LocalDateTime atStartOfDayResult1 = LocalDate.of(1970, 1, 1).atStartOfDay();
        personDto.setDateOfBirth(Date.from(atStartOfDayResult1.atZone(ZoneId.of("UTC")).toInstant()));
        personDto.setEmail("jane.doe@example.org");
        personDto.setFirstName("Jane");
        personDto.setGender("Gender");
        personDto.setImage("Image");
        personDto.setLastName("Doe");
        personDto.setPassword("iloveyou");
        personDto.setPhoneNumber("4105551212");
        personDto.setRole(Role.ANONYMOUS);
        personDto.setUserName("janedoe");
        personDto.setVerifyEmail(true);
        personDetailsService.sendingEmail(personDto);
        verify(verificationTokenServiceImpl).saveVerificationToken((Person) any());
        verify(personRepository).findByEmail((String) any());
        verify(emailService).sendMessage((String) any(), (String) any(), (String) any());
    }

    @Test
    void testSendingEmail8() throws MailjetException, MailjetSocketTimeoutException {
        VerificationTokenServiceImpl verificationTokenServiceImpl = mock(VerificationTokenServiceImpl.class);
        when(verificationTokenServiceImpl.saveVerificationToken((Person) any())).thenReturn("ABC123");
        PersonRepository personRepository = mock(PersonRepository.class);
        when(personRepository.findByEmail((String) any())).thenReturn(Optional.empty());
        EmailService emailService = mock(EmailService.class);
        doNothing().when(emailService).sendMessage((String) any(), (String) any(), (String) any());
        Argon2PasswordEncoder bCryptPasswordEncoder = new Argon2PasswordEncoder();
        EmailValidator emailValidator = new EmailValidator();
        PersonDetailsService personDetailsService = new PersonDetailsService(verificationTokenServiceImpl,
                bCryptPasswordEncoder, personRepository, emailValidator, new ModelMapper(), emailService);

        PersonDto personDto = new PersonDto();
        personDto.setAddress(new ArrayList<>());
        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        personDto.setDateOfBirth(Date.from(atStartOfDayResult.atZone(ZoneId.of("UTC")).toInstant()));
        personDto.setEmail("jane.doe@example.org");
        personDto.setFirstName("Jane");
        personDto.setGender("Gender");
        personDto.setImage("Image");
        personDto.setLastName("Doe");
        personDto.setPassword("iloveyou");
        personDto.setPhoneNumber("4105551212");
        personDto.setRole(Role.ANONYMOUS);
        personDto.setUserName("janedoe");
        personDto.setVerifyEmail(true);
        assertThrows(CustomServiceExceptions.class, () -> personDetailsService.sendingEmail(personDto));
        verify(personRepository).findByEmail((String) any());
    }

    @Test
    void testUpdateResetPasswordToken() throws CustomServiceExceptions, MailjetException, MailjetSocketTimeoutException {
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
        person.setResetPasswordToken("ABC123");
        person.setRole(Role.ANONYMOUS);
        person.setUserName("janedoe");
        person.setVerifyEmail(true);
        Optional<Person> ofResult = Optional.of(person);

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
        person1.setResetPasswordToken("ABC123");
        person1.setRole(Role.ANONYMOUS);
        person1.setUserName("janedoe");
        person1.setVerifyEmail(true);
        PersonRepository personRepository = mock(PersonRepository.class);
        when(personRepository.save((Person) any())).thenReturn(person1);
        when(personRepository.findByEmail((String) any())).thenReturn(ofResult);
        EmailService emailService = mock(EmailService.class);
        doNothing().when(emailService).sendMessage((String) any(), (String) any(), (String) any());
        VerificationTokenServiceImpl verificationTokenService = new VerificationTokenServiceImpl(
                mock(VerificationTokenRepository.class), mock(PersonRepository.class));

        Argon2PasswordEncoder bCryptPasswordEncoder = new Argon2PasswordEncoder();
        EmailValidator emailValidator = new EmailValidator();
        (new PersonDetailsService(verificationTokenService, bCryptPasswordEncoder, personRepository, emailValidator,
                new ModelMapper(), emailService)).updateResetPasswordToken("ABC123", "jane.doe@example.org");
        verify(personRepository).findByEmail((String) any());
        verify(personRepository).save((Person) any());
        verify(emailService).sendMessage((String) any(), (String) any(), (String) any());
    }

    @Test
    void testUpdateResetPasswordToken2() throws CustomServiceExceptions, MailjetException, MailjetSocketTimeoutException {
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
        person.setResetPasswordToken("ABC123");
        person.setRole(Role.ANONYMOUS);
        person.setUserName("janedoe");
        person.setVerifyEmail(true);
        Optional<Person> ofResult = Optional.of(person);

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
        person1.setResetPasswordToken("ABC123");
        person1.setRole(Role.ANONYMOUS);
        person1.setUserName("janedoe");
        person1.setVerifyEmail(true);
        PersonRepository personRepository = mock(PersonRepository.class);
        when(personRepository.save((Person) any())).thenReturn(person1);
        when(personRepository.findByEmail((String) any())).thenReturn(ofResult);
        EmailService emailService = mock(EmailService.class);
        doNothing().when(emailService).sendMessage((String) any(), (String) any(), (String) any());
        VerificationTokenServiceImpl verificationTokenService = new VerificationTokenServiceImpl(
                mock(VerificationTokenRepository.class), mock(PersonRepository.class));

        Argon2PasswordEncoder bCryptPasswordEncoder = new Argon2PasswordEncoder();
        EmailValidator emailValidator = new EmailValidator();
        (new PersonDetailsService(verificationTokenService, bCryptPasswordEncoder, personRepository, emailValidator,
                new ModelMapper(), emailService)).updateResetPasswordToken("ABC123", "jane.doe@example.org");
        verify(personRepository).findByEmail((String) any());
        verify(personRepository).save((Person) any());
        verify(emailService).sendMessage((String) any(), (String) any(), (String) any());
    }

    @Test
    void testGetByResetPasswordToken() {
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
        person.setResetPasswordToken("ABC123");
        person.setRole(Role.ANONYMOUS);
        person.setUserName("janedoe");
        person.setVerifyEmail(true);
        PersonRepository personRepository = mock(PersonRepository.class);
        when(personRepository.findByResetPasswordToken((String) any())).thenReturn(Optional.of(person));
        VerificationTokenServiceImpl verificationTokenService = new VerificationTokenServiceImpl(
                mock(VerificationTokenRepository.class), mock(PersonRepository.class));

        Argon2PasswordEncoder bCryptPasswordEncoder = new Argon2PasswordEncoder();
        EmailValidator emailValidator = new EmailValidator();
        assertSame(person, (new PersonDetailsService(verificationTokenService, bCryptPasswordEncoder, personRepository,
                emailValidator, new ModelMapper(), mock(EmailService.class))).getByResetPasswordToken("ABC123"));
        verify(personRepository).findByResetPasswordToken((String) any());
    }

    @Test
    void testGetByResetPasswordToken2() {
        PersonRepository personRepository = mock(PersonRepository.class);
        when(personRepository.findByResetPasswordToken((String) any()))
                .thenThrow(new CustomServiceExceptions("An error occurred"));
        VerificationTokenServiceImpl verificationTokenService = new VerificationTokenServiceImpl(
                mock(VerificationTokenRepository.class), mock(PersonRepository.class));

        Argon2PasswordEncoder bCryptPasswordEncoder = new Argon2PasswordEncoder();
        EmailValidator emailValidator = new EmailValidator();
        assertThrows(CustomServiceExceptions.class,
                () -> (new PersonDetailsService(verificationTokenService, bCryptPasswordEncoder, personRepository,
                        emailValidator, new ModelMapper(), mock(EmailService.class))).getByResetPasswordToken("ABC123"));
        verify(personRepository).findByResetPasswordToken((String) any());
    }

    @Test
    void testGetByResetPasswordToken3() {
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
        person.setResetPasswordToken("ABC123");
        person.setRole(Role.ANONYMOUS);
        person.setUserName("janedoe");
        person.setVerifyEmail(true);
        PersonRepository personRepository = mock(PersonRepository.class);
        when(personRepository.findByResetPasswordToken((String) any())).thenReturn(Optional.of(person));
        VerificationTokenServiceImpl verificationTokenService = new VerificationTokenServiceImpl(
                mock(VerificationTokenRepository.class), mock(PersonRepository.class));

        Argon2PasswordEncoder bCryptPasswordEncoder = new Argon2PasswordEncoder();
        EmailValidator emailValidator = new EmailValidator();
        assertSame(person, (new PersonDetailsService(verificationTokenService, bCryptPasswordEncoder, personRepository,
                emailValidator, new ModelMapper(), mock(EmailService.class))).getByResetPasswordToken("ABC123"));
        verify(personRepository).findByResetPasswordToken((String) any());
    }

    @Test
    void testGetByResetPasswordToken4() {
        PersonRepository personRepository = mock(PersonRepository.class);
        when(personRepository.findByResetPasswordToken((String) any()))
                .thenThrow(new CustomServiceExceptions("An error occurred"));
        VerificationTokenServiceImpl verificationTokenService = new VerificationTokenServiceImpl(
                mock(VerificationTokenRepository.class), mock(PersonRepository.class));

        Argon2PasswordEncoder bCryptPasswordEncoder = new Argon2PasswordEncoder();
        EmailValidator emailValidator = new EmailValidator();
        assertThrows(CustomServiceExceptions.class,
                () -> (new PersonDetailsService(verificationTokenService, bCryptPasswordEncoder, personRepository,
                        emailValidator, new ModelMapper(), mock(EmailService.class))).getByResetPasswordToken("ABC123"));
        verify(personRepository).findByResetPasswordToken((String) any());
    }

    @Test
    void testUpdateUserDetails() {
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
        person.setResetPasswordToken("ABC123");
        person.setRole(Role.ANONYMOUS);
        person.setUserName("janedoe");
        person.setVerifyEmail(true);
        Optional<Person> ofResult = Optional.of(person);

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
        person1.setResetPasswordToken("ABC123");
        person1.setRole(Role.ANONYMOUS);
        person1.setUserName("janedoe");
        person1.setVerifyEmail(true);
        PersonRepository personRepository = mock(PersonRepository.class);
        when(personRepository.save((Person) any())).thenReturn(person1);
        when(personRepository.findPersonByUserName((String) any())).thenReturn(ofResult);
        VerificationTokenServiceImpl verificationTokenService = new VerificationTokenServiceImpl(
                mock(VerificationTokenRepository.class), mock(PersonRepository.class));

        Argon2PasswordEncoder bCryptPasswordEncoder = new Argon2PasswordEncoder();
        EmailValidator emailValidator = new EmailValidator();
        PersonDetailsService personDetailsService = new PersonDetailsService(verificationTokenService,
                bCryptPasswordEncoder, personRepository, emailValidator, new ModelMapper(), mock(EmailService.class));
        LocalDateTime atStartOfDayResult2 = LocalDate.of(1970, 1, 1).atStartOfDay();
        personDetailsService.updateUserDetails(new UpdatePersonDetails("janedoe", "Jane", "Doe", "jane.doe@example.org",
                "Gender", Date.from(atStartOfDayResult2.atZone(ZoneId.of("UTC")).toInstant())));
        verify(personRepository).findPersonByUserName((String) any());
        verify(personRepository).save((Person) any());
    }

    @Test
    void testUpdateUserDetails2() {
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
        person.setResetPasswordToken("ABC123");
        person.setRole(Role.ANONYMOUS);
        person.setUserName("janedoe");
        person.setVerifyEmail(true);
        Optional<Person> ofResult = Optional.of(person);
        PersonRepository personRepository = mock(PersonRepository.class);
        when(personRepository.save((Person) any())).thenThrow(new CustomServiceExceptions("An error occurred"));
        when(personRepository.findPersonByUserName((String) any())).thenReturn(ofResult);
        VerificationTokenServiceImpl verificationTokenService = new VerificationTokenServiceImpl(
                mock(VerificationTokenRepository.class), mock(PersonRepository.class));

        Argon2PasswordEncoder bCryptPasswordEncoder = new Argon2PasswordEncoder();
        EmailValidator emailValidator = new EmailValidator();
        PersonDetailsService personDetailsService = new PersonDetailsService(verificationTokenService,
                bCryptPasswordEncoder, personRepository, emailValidator, new ModelMapper(), mock(EmailService.class));
        LocalDateTime atStartOfDayResult1 = LocalDate.of(1970, 1, 1).atStartOfDay();
        assertThrows(CustomServiceExceptions.class,
                () -> personDetailsService.updateUserDetails(new UpdatePersonDetails("janedoe", "Jane", "Doe",
                        "jane.doe@example.org", "Gender", Date.from(atStartOfDayResult1.atZone(ZoneId.of("UTC")).toInstant()))));
        verify(personRepository).findPersonByUserName((String) any());
        verify(personRepository).save((Person) any());
    }

    @Test
    void testUpdateUserDetails3() {
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
        person.setResetPasswordToken("ABC123");
        person.setRole(Role.ANONYMOUS);
        person.setUserName("janedoe");
        person.setVerifyEmail(true);
        PersonRepository personRepository = mock(PersonRepository.class);
        when(personRepository.save((Person) any())).thenReturn(person);
        when(personRepository.findPersonByUserName((String) any())).thenReturn(Optional.empty());
        VerificationTokenServiceImpl verificationTokenService = new VerificationTokenServiceImpl(
                mock(VerificationTokenRepository.class), mock(PersonRepository.class));

        Argon2PasswordEncoder bCryptPasswordEncoder = new Argon2PasswordEncoder();
        EmailValidator emailValidator = new EmailValidator();
        PersonDetailsService personDetailsService = new PersonDetailsService(verificationTokenService,
                bCryptPasswordEncoder, personRepository, emailValidator, new ModelMapper(), mock(EmailService.class));
        LocalDateTime atStartOfDayResult1 = LocalDate.of(1970, 1, 1).atStartOfDay();
        assertThrows(PersonNotFoundException.class,
                () -> personDetailsService.updateUserDetails(new UpdatePersonDetails("janedoe", "Jane", "Doe",
                        "jane.doe@example.org", "Gender", Date.from(atStartOfDayResult1.atZone(ZoneId.of("UTC")).toInstant()))));
        verify(personRepository).findPersonByUserName((String) any());
    }

    @Test
    void testUpdateUserDetails4() {
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
        person.setResetPasswordToken("ABC123");
        person.setRole(Role.ANONYMOUS);
        person.setUserName("janedoe");
        person.setVerifyEmail(true);
        Optional<Person> ofResult = Optional.of(person);

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
        person1.setResetPasswordToken("ABC123");
        person1.setRole(Role.ANONYMOUS);
        person1.setUserName("janedoe");
        person1.setVerifyEmail(true);
        PersonRepository personRepository = mock(PersonRepository.class);
        when(personRepository.save((Person) any())).thenReturn(person1);
        when(personRepository.findPersonByUserName((String) any())).thenReturn(ofResult);
        VerificationTokenServiceImpl verificationTokenService = new VerificationTokenServiceImpl(
                mock(VerificationTokenRepository.class), mock(PersonRepository.class));

        Argon2PasswordEncoder bCryptPasswordEncoder = new Argon2PasswordEncoder();
        EmailValidator emailValidator = new EmailValidator();
        PersonDetailsService personDetailsService = new PersonDetailsService(verificationTokenService,
                bCryptPasswordEncoder, personRepository, emailValidator, new ModelMapper(), mock(EmailService.class));
        UpdatePersonDetails updatePersonDetails = mock(UpdatePersonDetails.class);
        when(updatePersonDetails.getDateOfBirth()).thenThrow(new CustomServiceExceptions("An error occurred"));
        when(updatePersonDetails.getGender()).thenReturn("Gender");
        when(updatePersonDetails.getEmail()).thenReturn("jane.doe@example.org");
        when(updatePersonDetails.getLastName()).thenReturn("Doe");
        when(updatePersonDetails.getFirstName()).thenReturn("Jane");
        when(updatePersonDetails.getUserName()).thenReturn("janedoe");
        assertThrows(CustomServiceExceptions.class, () -> personDetailsService.updateUserDetails(updatePersonDetails));
        verify(personRepository).findPersonByUserName((String) any());
        verify(updatePersonDetails).getDateOfBirth();
        verify(updatePersonDetails).getEmail();
        verify(updatePersonDetails).getFirstName();
        verify(updatePersonDetails).getGender();
        verify(updatePersonDetails).getLastName();
        verify(updatePersonDetails).getUserName();
    }

    @Test
    void testUpdateUserDetails5() {
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
        person.setResetPasswordToken("ABC123");
        person.setRole(Role.ANONYMOUS);
        person.setUserName("janedoe");
        person.setVerifyEmail(true);
        Optional<Person> ofResult = Optional.of(person);

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
        person1.setResetPasswordToken("ABC123");
        person1.setRole(Role.ANONYMOUS);
        person1.setUserName("janedoe");
        person1.setVerifyEmail(true);
        PersonRepository personRepository = mock(PersonRepository.class);
        when(personRepository.save((Person) any())).thenReturn(person1);
        when(personRepository.findPersonByUserName((String) any())).thenReturn(ofResult);
        VerificationTokenServiceImpl verificationTokenService = new VerificationTokenServiceImpl(
                mock(VerificationTokenRepository.class), mock(PersonRepository.class));

        Argon2PasswordEncoder bCryptPasswordEncoder = new Argon2PasswordEncoder();
        EmailValidator emailValidator = new EmailValidator();
        PersonDetailsService personDetailsService = new PersonDetailsService(verificationTokenService,
                bCryptPasswordEncoder, personRepository, emailValidator, new ModelMapper(), mock(EmailService.class));
        LocalDateTime atStartOfDayResult2 = LocalDate.of(1970, 1, 1).atStartOfDay();
        personDetailsService.updateUserDetails(new UpdatePersonDetails("janedoe", "Jane", "Doe", "jane.doe@example.org",
                "Gender", Date.from(atStartOfDayResult2.atZone(ZoneId.of("UTC")).toInstant())));
        verify(personRepository).findPersonByUserName((String) any());
        verify(personRepository).save((Person) any());
    }

    @Test
    void testUpdateUserDetails6() {
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
        person.setResetPasswordToken("ABC123");
        person.setRole(Role.ANONYMOUS);
        person.setUserName("janedoe");
        person.setVerifyEmail(true);
        Optional<Person> ofResult = Optional.of(person);
        PersonRepository personRepository = mock(PersonRepository.class);
        when(personRepository.save((Person) any())).thenThrow(new CustomServiceExceptions("An error occurred"));
        when(personRepository.findPersonByUserName((String) any())).thenReturn(ofResult);
        VerificationTokenServiceImpl verificationTokenService = new VerificationTokenServiceImpl(
                mock(VerificationTokenRepository.class), mock(PersonRepository.class));

        Argon2PasswordEncoder bCryptPasswordEncoder = new Argon2PasswordEncoder();
        EmailValidator emailValidator = new EmailValidator();
        PersonDetailsService personDetailsService = new PersonDetailsService(verificationTokenService,
                bCryptPasswordEncoder, personRepository, emailValidator, new ModelMapper(), mock(EmailService.class));
        LocalDateTime atStartOfDayResult1 = LocalDate.of(1970, 1, 1).atStartOfDay();
        assertThrows(CustomServiceExceptions.class,
                () -> personDetailsService.updateUserDetails(new UpdatePersonDetails("janedoe", "Jane", "Doe",
                        "jane.doe@example.org", "Gender", Date.from(atStartOfDayResult1.atZone(ZoneId.of("UTC")).toInstant()))));
        verify(personRepository).findPersonByUserName((String) any());
        verify(personRepository).save((Person) any());
    }

    @Test
    void testUpdateUserDetails7() {
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
        person.setResetPasswordToken("ABC123");
        person.setRole(Role.ANONYMOUS);
        person.setUserName("janedoe");
        person.setVerifyEmail(true);
        PersonRepository personRepository = mock(PersonRepository.class);
        when(personRepository.save((Person) any())).thenReturn(person);
        when(personRepository.findPersonByUserName((String) any())).thenReturn(Optional.empty());
        VerificationTokenServiceImpl verificationTokenService = new VerificationTokenServiceImpl(
                mock(VerificationTokenRepository.class), mock(PersonRepository.class));

        Argon2PasswordEncoder bCryptPasswordEncoder = new Argon2PasswordEncoder();
        EmailValidator emailValidator = new EmailValidator();
        PersonDetailsService personDetailsService = new PersonDetailsService(verificationTokenService,
                bCryptPasswordEncoder, personRepository, emailValidator, new ModelMapper(), mock(EmailService.class));
        LocalDateTime atStartOfDayResult1 = LocalDate.of(1970, 1, 1).atStartOfDay();
        assertThrows(PersonNotFoundException.class,
                () -> personDetailsService.updateUserDetails(new UpdatePersonDetails("janedoe", "Jane", "Doe",
                        "jane.doe@example.org", "Gender", Date.from(atStartOfDayResult1.atZone(ZoneId.of("UTC")).toInstant()))));
        verify(personRepository).findPersonByUserName((String) any());
    }

    @Test
    void testUpdateUserDetails8() {
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
        person.setResetPasswordToken("ABC123");
        person.setRole(Role.ANONYMOUS);
        person.setUserName("janedoe");
        person.setVerifyEmail(true);
        Optional<Person> ofResult = Optional.of(person);

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
        person1.setResetPasswordToken("ABC123");
        person1.setRole(Role.ANONYMOUS);
        person1.setUserName("janedoe");
        person1.setVerifyEmail(true);
        PersonRepository personRepository = mock(PersonRepository.class);
        when(personRepository.save((Person) any())).thenReturn(person1);
        when(personRepository.findPersonByUserName((String) any())).thenReturn(ofResult);
        VerificationTokenServiceImpl verificationTokenService = new VerificationTokenServiceImpl(
                mock(VerificationTokenRepository.class), mock(PersonRepository.class));

        Argon2PasswordEncoder bCryptPasswordEncoder = new Argon2PasswordEncoder();
        EmailValidator emailValidator = new EmailValidator();
        PersonDetailsService personDetailsService = new PersonDetailsService(verificationTokenService,
                bCryptPasswordEncoder, personRepository, emailValidator, new ModelMapper(), mock(EmailService.class));
        UpdatePersonDetails updatePersonDetails = mock(UpdatePersonDetails.class);
        when(updatePersonDetails.getDateOfBirth()).thenThrow(new CustomServiceExceptions("An error occurred"));
        when(updatePersonDetails.getGender()).thenReturn("Gender");
        when(updatePersonDetails.getEmail()).thenReturn("jane.doe@example.org");
        when(updatePersonDetails.getLastName()).thenReturn("Doe");
        when(updatePersonDetails.getFirstName()).thenReturn("Jane");
        when(updatePersonDetails.getUserName()).thenReturn("janedoe");
        assertThrows(CustomServiceExceptions.class, () -> personDetailsService.updateUserDetails(updatePersonDetails));
        verify(personRepository).findPersonByUserName((String) any());
        verify(updatePersonDetails).getDateOfBirth();
        verify(updatePersonDetails).getEmail();
        verify(updatePersonDetails).getFirstName();
        verify(updatePersonDetails).getGender();
        verify(updatePersonDetails).getLastName();
        verify(updatePersonDetails).getUserName();
    }

    @Test
    void testUpdateCurrentPassword() {
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
        person.setResetPasswordToken("ABC123");
        person.setRole(Role.ANONYMOUS);
        person.setUserName("janedoe");
        person.setVerifyEmail(true);
        Optional<Person> ofResult = Optional.of(person);

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
        person1.setResetPasswordToken("ABC123");
        person1.setRole(Role.ANONYMOUS);
        person1.setUserName("janedoe");
        person1.setVerifyEmail(true);
        PersonRepository personRepository = mock(PersonRepository.class);
        when(personRepository.save((Person) any())).thenReturn(person1);
        when(personRepository.findPersonByPassword((String) any())).thenReturn(ofResult);
        VerificationTokenServiceImpl verificationTokenService = new VerificationTokenServiceImpl(
                mock(VerificationTokenRepository.class), mock(PersonRepository.class));

        Argon2PasswordEncoder bCryptPasswordEncoder = new Argon2PasswordEncoder();
        EmailValidator emailValidator = new EmailValidator();
        PersonDetailsService personDetailsService = new PersonDetailsService(verificationTokenService,
                bCryptPasswordEncoder, personRepository, emailValidator, new ModelMapper(), mock(EmailService.class));
        personDetailsService.updateCurrentPassword(new ChangePassword("iloveyou", "iloveyou", "iloveyou"));
        verify(personRepository).findPersonByPassword((String) any());
        verify(personRepository).save((Person) any());
    }

    @Test
    void testUpdateCurrentPassword2() {
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
        person.setResetPasswordToken("ABC123");
        person.setRole(Role.ANONYMOUS);
        person.setUserName("janedoe");
        person.setVerifyEmail(true);
        Optional<Person> ofResult = Optional.of(person);
        PersonRepository personRepository = mock(PersonRepository.class);
        when(personRepository.save((Person) any())).thenThrow(new CustomServiceExceptions("An error occurred"));
        when(personRepository.findPersonByPassword((String) any())).thenReturn(ofResult);
        VerificationTokenServiceImpl verificationTokenService = new VerificationTokenServiceImpl(
                mock(VerificationTokenRepository.class), mock(PersonRepository.class));

        Argon2PasswordEncoder bCryptPasswordEncoder = new Argon2PasswordEncoder();
        EmailValidator emailValidator = new EmailValidator();
        PersonDetailsService personDetailsService = new PersonDetailsService(verificationTokenService,
                bCryptPasswordEncoder, personRepository, emailValidator, new ModelMapper(), mock(EmailService.class));
        assertThrows(CustomServiceExceptions.class,
                () -> personDetailsService.updateCurrentPassword(new ChangePassword("iloveyou", "iloveyou", "iloveyou")));
        verify(personRepository).findPersonByPassword((String) any());
        verify(personRepository).save((Person) any());
    }

    @Test
    void testUpdateCurrentPassword3() {
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
        person.setResetPasswordToken("ABC123");
        person.setRole(Role.ANONYMOUS);
        person.setUserName("janedoe");
        person.setVerifyEmail(true);
        PersonRepository personRepository = mock(PersonRepository.class);
        when(personRepository.save((Person) any())).thenReturn(person);
        when(personRepository.findPersonByPassword((String) any())).thenReturn(Optional.empty());
        VerificationTokenServiceImpl verificationTokenService = new VerificationTokenServiceImpl(
                mock(VerificationTokenRepository.class), mock(PersonRepository.class));

        Argon2PasswordEncoder bCryptPasswordEncoder = new Argon2PasswordEncoder();
        EmailValidator emailValidator = new EmailValidator();
        PersonDetailsService personDetailsService = new PersonDetailsService(verificationTokenService,
                bCryptPasswordEncoder, personRepository, emailValidator, new ModelMapper(), mock(EmailService.class));
        assertThrows(PersonNotFoundException.class,
                () -> personDetailsService.updateCurrentPassword(new ChangePassword("iloveyou", "iloveyou", "iloveyou")));
        verify(personRepository).findPersonByPassword((String) any());
    }

    @Test
    void testUpdateCurrentPassword4() {
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
        person.setResetPasswordToken("ABC123");
        person.setRole(Role.ANONYMOUS);
        person.setUserName("janedoe");
        person.setVerifyEmail(true);
        Optional<Person> ofResult = Optional.of(person);

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
        person1.setResetPasswordToken("ABC123");
        person1.setRole(Role.ANONYMOUS);
        person1.setUserName("janedoe");
        person1.setVerifyEmail(true);
        PersonRepository personRepository = mock(PersonRepository.class);
        when(personRepository.save((Person) any())).thenReturn(person1);
        when(personRepository.findPersonByPassword((String) any())).thenReturn(ofResult);
        VerificationTokenServiceImpl verificationTokenService = new VerificationTokenServiceImpl(
                mock(VerificationTokenRepository.class), mock(PersonRepository.class));

        Argon2PasswordEncoder bCryptPasswordEncoder = new Argon2PasswordEncoder();
        EmailValidator emailValidator = new EmailValidator();
        PersonDetailsService personDetailsService = new PersonDetailsService(verificationTokenService,
                bCryptPasswordEncoder, personRepository, emailValidator, new ModelMapper(), mock(EmailService.class));
        personDetailsService.updateCurrentPassword(new ChangePassword("iloveyou", "New Password", "iloveyou"));
        verify(personRepository).findPersonByPassword((String) any());
    }

    @Test
    void testUpdateCurrentPassword5() {
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
        person.setResetPasswordToken("ABC123");
        person.setRole(Role.ANONYMOUS);
        person.setUserName("janedoe");
        person.setVerifyEmail(true);
        Optional<Person> ofResult = Optional.of(person);

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
        person1.setResetPasswordToken("ABC123");
        person1.setRole(Role.ANONYMOUS);
        person1.setUserName("janedoe");
        person1.setVerifyEmail(true);
        PersonRepository personRepository = mock(PersonRepository.class);
        when(personRepository.save((Person) any())).thenReturn(person1);
        when(personRepository.findPersonByPassword((String) any())).thenReturn(ofResult);
        VerificationTokenServiceImpl verificationTokenService = new VerificationTokenServiceImpl(
                mock(VerificationTokenRepository.class), mock(PersonRepository.class));

        Argon2PasswordEncoder bCryptPasswordEncoder = new Argon2PasswordEncoder();
        EmailValidator emailValidator = new EmailValidator();
        PersonDetailsService personDetailsService = new PersonDetailsService(verificationTokenService,
                bCryptPasswordEncoder, personRepository, emailValidator, new ModelMapper(), mock(EmailService.class));
        ChangePassword changePassword = mock(ChangePassword.class);
        when(changePassword.getConfirmPassword()).thenThrow(new CustomServiceExceptions("An error occurred"));
        when(changePassword.getNewPassword()).thenReturn("iloveyou");
        when(changePassword.getCurrentPassword()).thenReturn("iloveyou");
        assertThrows(CustomServiceExceptions.class, () -> personDetailsService.updateCurrentPassword(changePassword));
        verify(personRepository).findPersonByPassword((String) any());
        verify(changePassword).getConfirmPassword();
        verify(changePassword).getCurrentPassword();
        verify(changePassword).getNewPassword();
    }

    @Test
    void testUpdateCurrentPassword6() {
        PersonRepository personRepository = mock(PersonRepository.class);
        when(personRepository.findPersonByPassword((String) any()))
                .thenThrow(new CustomServiceExceptions("An error occurred"));
        VerificationTokenServiceImpl verificationTokenService = new VerificationTokenServiceImpl(
                mock(VerificationTokenRepository.class), mock(PersonRepository.class));

        Argon2PasswordEncoder bCryptPasswordEncoder = new Argon2PasswordEncoder();
        EmailValidator emailValidator = new EmailValidator();
        PersonDetailsService personDetailsService = new PersonDetailsService(verificationTokenService,
                bCryptPasswordEncoder, personRepository, emailValidator, new ModelMapper(), mock(EmailService.class));
        assertThrows(CustomServiceExceptions.class,
                () -> personDetailsService.updateCurrentPassword(new ChangePassword("iloveyou", "iloveyou", "iloveyou")));
        verify(personRepository).findPersonByPassword((String) any());
    }

    @Test
    void testBuildEmail() {
        VerificationTokenServiceImpl verificationTokenService = new VerificationTokenServiceImpl(
                mock(VerificationTokenRepository.class), mock(PersonRepository.class));

        Argon2PasswordEncoder bCryptPasswordEncoder = new Argon2PasswordEncoder();
        PersonRepository personRepository = mock(PersonRepository.class);
        EmailValidator emailValidator = new EmailValidator();
        assertEquals("<div style=\"font-family:Helvetica,Arial,sans-serif;font-size:16px;margin:0;color:#0b0c0c\">\n" + "\n"
                        + "<span style=\"display:none;font-size:1px;color:#fff;max-height:0\"></span>\n" + "\n"
                        + "  <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;min-width:100%;width:100%"
                        + "!important\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" + "    <tbody><tr>\n"
                        + "      <td width=\"100%\" height=\"53\" bgcolor=\"#0b0c0c\">\n" + "        \n"
                        + "        <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;max-width:580px\""
                        + " cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\">\n" + "          <tbody><tr>\n"
                        + "            <td width=\"70\" bgcolor=\"#0b0c0c\" valign=\"middle\">\n"
                        + "                <table role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border"
                        + "-collapse:collapse\">\n" + "                  <tbody><tr>\n"
                        + "                    <td style=\"padding-left:10px\">\n" + "                  \n"
                        + "                    </td>\n"
                        + "                    <td style=\"font-size:28px;line-height:1.315789474;Margin-top:4px;padding-left:10px\">"
                        + "\n"
                        + "                      <span style=\"font-family:Helvetica,Arial,sans-serif;font-weight:700;color:#ffffff"
                        + ";text-decoration:none;vertical-align:top;display:inline-block\">Confirm your email</span>\n"
                        + "                    </td>\n" + "                  </tr>\n" + "                </tbody></table>\n"
                        + "              </a>\n" + "            </td>\n" + "          </tr>\n" + "        </tbody></table>\n"
                        + "        \n" + "      </td>\n" + "    </tr>\n" + "  </tbody></table>\n"
                        + "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\""
                        + " cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\""
                        + " width=\"100%\">\n" + "    <tbody><tr>\n" + "      <td width=\"10\" height=\"10\" valign=\"middle\"></td>\n"
                        + "      <td>\n" + "        \n"
                        + "                <table role=\"presentation\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\""
                        + " style=\"border-collapse:collapse\">\n" + "                  <tbody><tr>\n"
                        + "                    <td bgcolor=\"#1D70B8\" width=\"100%\" height=\"10\"></td>\n"
                        + "                  </tr>\n" + "                </tbody></table>\n" + "        \n" + "      </td>\n"
                        + "      <td width=\"10\" valign=\"middle\" height=\"10\"></td>\n" + "    </tr>\n" + "  </tbody></table>\n"
                        + "\n" + "\n" + "\n"
                        + "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\""
                        + " cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\""
                        + " width=\"100%\">\n" + "    <tbody><tr>\n" + "      <td height=\"30\"><br></td>\n" + "    </tr>\n"
                        + "    <tr>\n" + "      <td width=\"10\" valign=\"middle\"><br></td>\n"
                        + "      <td style=\"font-family:Helvetica,Arial,sans-serif;font-size:19px;line-height:1.315789474;max"
                        + "-width:560px\">\n" + "        \n"
                        + "            <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Hi Name,</p><p"
                        + " style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> Thank you for registering."
                        + " Please click on the below link to activate your account: </p><blockquote style=\"Margin:0 0 20px"
                        + " 0;border-left:10px solid #b1b4b6;padding:15px 0 0.1px 15px;font-size:19px;line-height:25px\"><p"
                        + " style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> <a href=\"Link\">Activate"
                        + " Now</a> </p></blockquote>\n" + " Link will expire in 24 hours. <p>See you soon</p>        \n"
                        + "      </td>\n" + "      <td width=\"10\" valign=\"middle\"><br></td>\n" + "    </tr>\n" + "    <tr>\n"
                        + "      <td height=\"30\"><br></td>\n" + "    </tr>\n"
                        + "  </tbody></table><div class=\"yj6qo\"></div><div class=\"adL\">\n" + "\n" + "</div></div>",
                (new PersonDetailsService(verificationTokenService, bCryptPasswordEncoder, personRepository, emailValidator,
                        new ModelMapper(), mock(EmailService.class))).buildEmail("Name", "Link"));
    }

    @Test
    void testBuildEmail2() {
        VerificationTokenServiceImpl verificationTokenService = new VerificationTokenServiceImpl(
                mock(VerificationTokenRepository.class), mock(PersonRepository.class));

        Argon2PasswordEncoder bCryptPasswordEncoder = new Argon2PasswordEncoder();
        PersonRepository personRepository = mock(PersonRepository.class);
        EmailValidator emailValidator = new EmailValidator();
        assertEquals("<div style=\"font-family:Helvetica,Arial,sans-serif;font-size:16px;margin:0;color:#0b0c0c\">\n" + "\n"
                        + "<span style=\"display:none;font-size:1px;color:#fff;max-height:0\"></span>\n" + "\n"
                        + "  <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;min-width:100%;width:100%"
                        + "!important\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" + "    <tbody><tr>\n"
                        + "      <td width=\"100%\" height=\"53\" bgcolor=\"#0b0c0c\">\n" + "        \n"
                        + "        <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;max-width:580px\""
                        + " cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\">\n" + "          <tbody><tr>\n"
                        + "            <td width=\"70\" bgcolor=\"#0b0c0c\" valign=\"middle\">\n"
                        + "                <table role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border"
                        + "-collapse:collapse\">\n" + "                  <tbody><tr>\n"
                        + "                    <td style=\"padding-left:10px\">\n" + "                  \n"
                        + "                    </td>\n"
                        + "                    <td style=\"font-size:28px;line-height:1.315789474;Margin-top:4px;padding-left:10px\">"
                        + "\n"
                        + "                      <span style=\"font-family:Helvetica,Arial,sans-serif;font-weight:700;color:#ffffff"
                        + ";text-decoration:none;vertical-align:top;display:inline-block\">Confirm your email</span>\n"
                        + "                    </td>\n" + "                  </tr>\n" + "                </tbody></table>\n"
                        + "              </a>\n" + "            </td>\n" + "          </tr>\n" + "        </tbody></table>\n"
                        + "        \n" + "      </td>\n" + "    </tr>\n" + "  </tbody></table>\n"
                        + "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\""
                        + " cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\""
                        + " width=\"100%\">\n" + "    <tbody><tr>\n" + "      <td width=\"10\" height=\"10\" valign=\"middle\"></td>\n"
                        + "      <td>\n" + "        \n"
                        + "                <table role=\"presentation\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\""
                        + " style=\"border-collapse:collapse\">\n" + "                  <tbody><tr>\n"
                        + "                    <td bgcolor=\"#1D70B8\" width=\"100%\" height=\"10\"></td>\n"
                        + "                  </tr>\n" + "                </tbody></table>\n" + "        \n" + "      </td>\n"
                        + "      <td width=\"10\" valign=\"middle\" height=\"10\"></td>\n" + "    </tr>\n" + "  </tbody></table>\n"
                        + "\n" + "\n" + "\n"
                        + "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\""
                        + " cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\""
                        + " width=\"100%\">\n" + "    <tbody><tr>\n" + "      <td height=\"30\"><br></td>\n" + "    </tr>\n"
                        + "    <tr>\n" + "      <td width=\"10\" valign=\"middle\"><br></td>\n"
                        + "      <td style=\"font-family:Helvetica,Arial,sans-serif;font-size:19px;line-height:1.315789474;max"
                        + "-width:560px\">\n" + "        \n"
                        + "            <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Hi Name,</p><p"
                        + " style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> Thank you for registering."
                        + " Please click on the below link to activate your account: </p><blockquote style=\"Margin:0 0 20px"
                        + " 0;border-left:10px solid #b1b4b6;padding:15px 0 0.1px 15px;font-size:19px;line-height:25px\"><p"
                        + " style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> <a href=\"Link\">Activate"
                        + " Now</a> </p></blockquote>\n" + " Link will expire in 24 hours. <p>See you soon</p>        \n"
                        + "      </td>\n" + "      <td width=\"10\" valign=\"middle\"><br></td>\n" + "    </tr>\n" + "    <tr>\n"
                        + "      <td height=\"30\"><br></td>\n" + "    </tr>\n"
                        + "  </tbody></table><div class=\"yj6qo\"></div><div class=\"adL\">\n" + "\n" + "</div></div>",
                (new PersonDetailsService(verificationTokenService, bCryptPasswordEncoder, personRepository, emailValidator,
                        new ModelMapper(), mock(EmailService.class))).buildEmail("Name", "Link"));
    }
}

