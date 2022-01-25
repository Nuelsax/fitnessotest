package com.decagon.fitnessoapp.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.decagon.fitnessoapp.Email.EmailService;
import com.decagon.fitnessoapp.dto.AuthRequest;
import com.decagon.fitnessoapp.dto.ChangePassword;
import com.decagon.fitnessoapp.dto.PersonDto;
import com.decagon.fitnessoapp.dto.UpdatePersonDetails;
import com.decagon.fitnessoapp.exception.CustomServiceExceptions;
import com.decagon.fitnessoapp.model.user.Person;
import com.decagon.fitnessoapp.model.user.Role;
import com.decagon.fitnessoapp.repository.PersonRepository;
import com.decagon.fitnessoapp.repository.VerificationTokenRepository;
import com.decagon.fitnessoapp.security.JwtUtils;
import com.decagon.fitnessoapp.security.PersonDetailsService;
import com.decagon.fitnessoapp.service.PersonService;
import com.decagon.fitnessoapp.service.VerificationService;
import com.decagon.fitnessoapp.service.serviceImplementation.EmailValidator;
import com.decagon.fitnessoapp.service.serviceImplementation.VerificationTokenServiceImpl;
import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.errors.MailjetSocketTimeoutException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

import org.apache.catalina.connector.Response;

import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.access.intercept.RunAsImplAuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.jaas.DefaultJaasAuthenticationProvider;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;

class PersonControllerTest {
    @Test
    void testEditUserDetails() {
        PersonService personService = mock(PersonService.class);
        doNothing().when(personService).updateUserDetails((UpdatePersonDetails) any());
        VerificationTokenServiceImpl verificationTokenService = new VerificationTokenServiceImpl(
                mock(VerificationTokenRepository.class), mock(PersonRepository.class));

        VerificationTokenServiceImpl verificationTokenService1 = new VerificationTokenServiceImpl(
                mock(VerificationTokenRepository.class), mock(PersonRepository.class));

        Argon2PasswordEncoder bCryptPasswordEncoder = new Argon2PasswordEncoder();
        PersonRepository personRepository = mock(PersonRepository.class);
        EmailValidator emailValidator = new EmailValidator();
        PersonDetailsService personDetailsService = new PersonDetailsService(verificationTokenService1,
                bCryptPasswordEncoder, personRepository, emailValidator, new ModelMapper(), mock(EmailService.class));

        ProviderManager authenticationManager = new ProviderManager(new RunAsImplAuthenticationProvider());
        PersonController personController = new PersonController(personService, verificationTokenService,
                personDetailsService, authenticationManager, new JwtUtils());
        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        personController.editUserDetails(new UpdatePersonDetails("janedoe", "Jane", "Doe", "jane.doe@example.org", "Gender",
                Date.from(atStartOfDayResult.atZone(ZoneId.of("UTC")).toInstant())));
        verify(personService).updateUserDetails((UpdatePersonDetails) any());
    }

    @Test
    void testEditUserPassword() {
        PersonService personService = mock(PersonService.class);
        doNothing().when(personService).updateCurrentPassword((ChangePassword) any());
        VerificationTokenServiceImpl verificationTokenService = new VerificationTokenServiceImpl(
                mock(VerificationTokenRepository.class), mock(PersonRepository.class));

        VerificationTokenServiceImpl verificationTokenService1 = new VerificationTokenServiceImpl(
                mock(VerificationTokenRepository.class), mock(PersonRepository.class));

        Argon2PasswordEncoder bCryptPasswordEncoder = new Argon2PasswordEncoder();
        PersonRepository personRepository = mock(PersonRepository.class);
        EmailValidator emailValidator = new EmailValidator();
        PersonDetailsService personDetailsService = new PersonDetailsService(verificationTokenService1,
                bCryptPasswordEncoder, personRepository, emailValidator, new ModelMapper(), mock(EmailService.class));

        ProviderManager authenticationManager = new ProviderManager(new RunAsImplAuthenticationProvider());
        PersonController personController = new PersonController(personService, verificationTokenService,
                personDetailsService, authenticationManager, new JwtUtils());
        personController.editUserPassword(new ChangePassword("iloveyou", "iloveyou", "iloveyou"));
        verify(personService).updateCurrentPassword((ChangePassword) any());
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
        PersonService personService = mock(PersonService.class);
        when(personService.register((PersonDto) any())).thenReturn(person);
        VerificationTokenServiceImpl verificationTokenService = new VerificationTokenServiceImpl(
                mock(VerificationTokenRepository.class), mock(PersonRepository.class));

        VerificationTokenServiceImpl verificationTokenService1 = new VerificationTokenServiceImpl(
                mock(VerificationTokenRepository.class), mock(PersonRepository.class));

        Argon2PasswordEncoder bCryptPasswordEncoder = new Argon2PasswordEncoder();
        PersonRepository personRepository = mock(PersonRepository.class);
        EmailValidator emailValidator = new EmailValidator();
        PersonDetailsService personDetailsService = new PersonDetailsService(verificationTokenService1,
                bCryptPasswordEncoder, personRepository, emailValidator, new ModelMapper(), mock(EmailService.class));

        ProviderManager authenticationManager = new ProviderManager(new RunAsImplAuthenticationProvider());
        PersonController personController = new PersonController(personService, verificationTokenService,
                personDetailsService, authenticationManager, new JwtUtils());

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
        ResponseEntity<?> actualRegisterResult = personController.register(personDto);
        assertTrue(actualRegisterResult.hasBody());
        assertTrue(actualRegisterResult.getHeaders().isEmpty());
        assertEquals(HttpStatus.OK, actualRegisterResult.getStatusCode());
        verify(personService).register((PersonDto) any());
    }

    @Test
    void testRegister2() throws MailjetException, MailjetSocketTimeoutException {
        PersonService personService = mock(PersonService.class);
        when(personService.register((PersonDto) any())).thenThrow(new MailjetSocketTimeoutException("An error occurred"));
        VerificationTokenServiceImpl verificationTokenService = new VerificationTokenServiceImpl(
                mock(VerificationTokenRepository.class), mock(PersonRepository.class));

        VerificationTokenServiceImpl verificationTokenService1 = new VerificationTokenServiceImpl(
                mock(VerificationTokenRepository.class), mock(PersonRepository.class));

        Argon2PasswordEncoder bCryptPasswordEncoder = new Argon2PasswordEncoder();
        PersonRepository personRepository = mock(PersonRepository.class);
        EmailValidator emailValidator = new EmailValidator();
        PersonDetailsService personDetailsService = new PersonDetailsService(verificationTokenService1,
                bCryptPasswordEncoder, personRepository, emailValidator, new ModelMapper(), mock(EmailService.class));

        ProviderManager authenticationManager = new ProviderManager(new RunAsImplAuthenticationProvider());
        PersonController personController = new PersonController(personService, verificationTokenService,
                personDetailsService, authenticationManager, new JwtUtils());

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
        assertThrows(MailjetSocketTimeoutException.class, () -> personController.register(personDto));
        verify(personService).register((PersonDto) any());
    }

    @Test
    void testConfirm() {
        VerificationService verificationService = mock(VerificationService.class);
        when(verificationService.confirmToken((String) any())).thenReturn("ABC123");
        PersonService personService = mock(PersonService.class);
        VerificationTokenServiceImpl verificationTokenService = new VerificationTokenServiceImpl(
                mock(VerificationTokenRepository.class), mock(PersonRepository.class));

        Argon2PasswordEncoder bCryptPasswordEncoder = new Argon2PasswordEncoder();
        PersonRepository personRepository = mock(PersonRepository.class);
        EmailValidator emailValidator = new EmailValidator();
        PersonDetailsService personDetailsService = new PersonDetailsService(verificationTokenService,
                bCryptPasswordEncoder, personRepository, emailValidator, new ModelMapper(), mock(EmailService.class));

        ProviderManager authenticationManager = new ProviderManager(new RunAsImplAuthenticationProvider());
        assertEquals("ABC123", (new PersonController(personService, verificationService, personDetailsService,
                authenticationManager, new JwtUtils())).confirm("ABC123"));
        verify(verificationService).confirmToken((String) any());
    }

    @Test
    void testLogin() throws Exception {
        PersonService personService = mock(PersonService.class);
        VerificationTokenServiceImpl verificationTokenService = new VerificationTokenServiceImpl(
                mock(VerificationTokenRepository.class), mock(PersonRepository.class));

        VerificationTokenServiceImpl verificationTokenService1 = new VerificationTokenServiceImpl(
                mock(VerificationTokenRepository.class), mock(PersonRepository.class));

        Argon2PasswordEncoder bCryptPasswordEncoder = new Argon2PasswordEncoder();
        PersonRepository personRepository = mock(PersonRepository.class);
        EmailValidator emailValidator = new EmailValidator();
        PersonDetailsService personDetailsService = new PersonDetailsService(verificationTokenService1,
                bCryptPasswordEncoder, personRepository, emailValidator, new ModelMapper(), mock(EmailService.class));

        ProviderManager authenticationManager = new ProviderManager(new RunAsImplAuthenticationProvider());
        PersonController personController = new PersonController(personService, verificationTokenService,
                personDetailsService, authenticationManager, new JwtUtils());

        AuthRequest authRequest = new AuthRequest();
        authRequest.setPassword("iloveyou");
        authRequest.setUsername("janedoe");
        assertThrows(Exception.class, () -> personController.login(authRequest, new Response()));
    }

    @Test
    void testLogin2() throws Exception {
        PersonService personService = mock(PersonService.class);
        VerificationTokenServiceImpl verificationTokenService = new VerificationTokenServiceImpl(
                mock(VerificationTokenRepository.class), mock(PersonRepository.class));

        VerificationTokenServiceImpl verificationTokenService1 = new VerificationTokenServiceImpl(
                mock(VerificationTokenRepository.class), mock(PersonRepository.class));

        Argon2PasswordEncoder bCryptPasswordEncoder = new Argon2PasswordEncoder();
        PersonRepository personRepository = mock(PersonRepository.class);
        EmailValidator emailValidator = new EmailValidator();
        PersonDetailsService personDetailsService = new PersonDetailsService(verificationTokenService1,
                bCryptPasswordEncoder, personRepository, emailValidator, new ModelMapper(), mock(EmailService.class));

        ProviderManager authenticationManager = new ProviderManager(new DefaultJaasAuthenticationProvider());
        PersonController personController = new PersonController(personService, verificationTokenService,
                personDetailsService, authenticationManager, new JwtUtils());

        AuthRequest authRequest = new AuthRequest();
        authRequest.setPassword("iloveyou");
        authRequest.setUsername("janedoe");
        assertThrows(Exception.class, () -> personController.login(authRequest, new Response()));
    }

    @Test
    void testLogin3() throws Exception {
        PersonService personService = mock(PersonService.class);
        VerificationTokenServiceImpl verificationTokenService = new VerificationTokenServiceImpl(
                mock(VerificationTokenRepository.class), mock(PersonRepository.class));

        VerificationTokenServiceImpl verificationTokenService1 = new VerificationTokenServiceImpl(
                mock(VerificationTokenRepository.class), mock(PersonRepository.class));

        Argon2PasswordEncoder bCryptPasswordEncoder = new Argon2PasswordEncoder();
        PersonRepository personRepository = mock(PersonRepository.class);
        EmailValidator emailValidator = new EmailValidator();
        PersonDetailsService personDetailsService = new PersonDetailsService(verificationTokenService1,
                bCryptPasswordEncoder, personRepository, emailValidator, new ModelMapper(), mock(EmailService.class));

        PersonController personController = new PersonController(personService, verificationTokenService,
                personDetailsService, null, new JwtUtils());

        AuthRequest authRequest = new AuthRequest();
        authRequest.setPassword("iloveyou");
        authRequest.setUsername("janedoe");
        assertThrows(Exception.class, () -> personController.login(authRequest, new Response()));
    }

    @Test
    void testProcessForgotPassword() throws MailjetException, MailjetSocketTimeoutException {
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
        PersonDetailsService personDetailsService = new PersonDetailsService(verificationTokenService,
                bCryptPasswordEncoder, personRepository, emailValidator, new ModelMapper(), emailService);

        PersonService personService = mock(PersonService.class);
        VerificationTokenServiceImpl verificationTokenService1 = new VerificationTokenServiceImpl(
                mock(VerificationTokenRepository.class), mock(PersonRepository.class));

        ProviderManager authenticationManager = new ProviderManager(new RunAsImplAuthenticationProvider());
        PersonController personController = new PersonController(personService, verificationTokenService1,
                personDetailsService, authenticationManager, new JwtUtils());

        Person person2 = new Person();
        person2.setAddress(new ArrayList<>());
        LocalDateTime atStartOfDayResult2 = LocalDate.of(1970, 1, 1).atStartOfDay();
        person2.setDateOfBirth(Date.from(atStartOfDayResult2.atZone(ZoneId.of("UTC")).toInstant()));
        person2.setEmail("jane.doe@example.org");
        person2.setFirstName("Jane");
        person2.setGender("Gender");
        person2.setId(123L);
        person2.setImage("Image");
        person2.setLastName("Doe");
        person2.setPassword("iloveyou");
        person2.setPhoneNumber("4105551212");
        person2.setResetPasswordToken("ABC123");
        person2.setRole(Role.ANONYMOUS);
        person2.setUserName("janedoe");
        person2.setVerifyEmail(true);
        ResponseEntity<String> actualProcessForgotPasswordResult = personController.processForgotPassword(person2);
        assertEquals("forgot_password_form", actualProcessForgotPasswordResult.getBody());
        assertEquals("<202 ACCEPTED Accepted,forgot_password_form,[]>", actualProcessForgotPasswordResult.toString());
        assertEquals(HttpStatus.ACCEPTED, actualProcessForgotPasswordResult.getStatusCode());
        assertTrue(actualProcessForgotPasswordResult.getHeaders().isEmpty());
        verify(personRepository).findByEmail((String) any());
        verify(personRepository).save((Person) any());
        verify(emailService).sendMessage((String) any(), (String) any(), (String) any());
    }

    @Test
    void testProcessForgotPassword2() throws CustomServiceExceptions, MailjetException, MailjetSocketTimeoutException {
        PersonDetailsService personDetailsService = mock(PersonDetailsService.class);
        doNothing().when(personDetailsService).updateResetPasswordToken((String) any(), (String) any());
        PersonService personService = mock(PersonService.class);
        VerificationTokenServiceImpl verificationTokenService = new VerificationTokenServiceImpl(
                mock(VerificationTokenRepository.class), mock(PersonRepository.class));

        ProviderManager authenticationManager = new ProviderManager(new RunAsImplAuthenticationProvider());
        PersonController personController = new PersonController(personService, verificationTokenService,
                personDetailsService, authenticationManager, new JwtUtils());

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
        ResponseEntity<String> actualProcessForgotPasswordResult = personController.processForgotPassword(person);
        assertEquals("forgot_password_form", actualProcessForgotPasswordResult.getBody());
        assertEquals("<202 ACCEPTED Accepted,forgot_password_form,[]>", actualProcessForgotPasswordResult.toString());
        assertEquals(HttpStatus.ACCEPTED, actualProcessForgotPasswordResult.getStatusCode());
        assertTrue(actualProcessForgotPasswordResult.getHeaders().isEmpty());
        verify(personDetailsService).updateResetPasswordToken((String) any(), (String) any());
    }

    @Test
    void testShowResetPasswordForm() {
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
        PersonDetailsService personDetailsService = new PersonDetailsService(verificationTokenService,
                bCryptPasswordEncoder, personRepository, emailValidator, new ModelMapper(), mock(EmailService.class));

        PersonService personService = mock(PersonService.class);
        VerificationTokenServiceImpl verificationTokenService1 = new VerificationTokenServiceImpl(
                mock(VerificationTokenRepository.class), mock(PersonRepository.class));

        ProviderManager authenticationManager = new ProviderManager(new RunAsImplAuthenticationProvider());
        ResponseEntity<String> actualShowResetPasswordFormResult = (new PersonController(personService,
                verificationTokenService1, personDetailsService, authenticationManager, new JwtUtils()))
                .showResetPasswordForm("ABC123");
        assertEquals("reset_password_form", actualShowResetPasswordFormResult.getBody());
        assertEquals("<202 ACCEPTED Accepted,reset_password_form,[]>", actualShowResetPasswordFormResult.toString());
        assertEquals(HttpStatus.ACCEPTED, actualShowResetPasswordFormResult.getStatusCode());
        assertTrue(actualShowResetPasswordFormResult.getHeaders().isEmpty());
        verify(personRepository).findByResetPasswordToken((String) any());
    }

    @Test
    void testShowResetPasswordForm2() {
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
        PersonDetailsService personDetailsService = mock(PersonDetailsService.class);
        when(personDetailsService.getByResetPasswordToken((String) any())).thenReturn(person);
        PersonService personService = mock(PersonService.class);
        VerificationTokenServiceImpl verificationTokenService = new VerificationTokenServiceImpl(
                mock(VerificationTokenRepository.class), mock(PersonRepository.class));

        ProviderManager authenticationManager = new ProviderManager(new RunAsImplAuthenticationProvider());
        ResponseEntity<String> actualShowResetPasswordFormResult = (new PersonController(personService,
                verificationTokenService, personDetailsService, authenticationManager, new JwtUtils()))
                .showResetPasswordForm("ABC123");
        assertEquals("reset_password_form", actualShowResetPasswordFormResult.getBody());
        assertEquals("<202 ACCEPTED Accepted,reset_password_form,[]>", actualShowResetPasswordFormResult.toString());
        assertEquals(HttpStatus.ACCEPTED, actualShowResetPasswordFormResult.getStatusCode());
        assertTrue(actualShowResetPasswordFormResult.getHeaders().isEmpty());
        verify(personDetailsService).getByResetPasswordToken((String) any());
    }

    @Test
    void testProcessResetPassword() {
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
        PersonDetailsService personDetailsService = mock(PersonDetailsService.class);
        doNothing().when(personDetailsService).updatePassword((Person) any(), (String) any());
        when(personDetailsService.getByResetPasswordToken((String) any())).thenReturn(person);
        PersonService personService = mock(PersonService.class);
        VerificationTokenServiceImpl verificationTokenService = new VerificationTokenServiceImpl(
                mock(VerificationTokenRepository.class), mock(PersonRepository.class));

        ProviderManager authenticationManager = new ProviderManager(new RunAsImplAuthenticationProvider());
        PersonController personController = new PersonController(personService, verificationTokenService,
                personDetailsService, authenticationManager, new JwtUtils());

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
        ResponseEntity<String> actualProcessResetPasswordResult = personController.processResetPassword(person1, "ABC123",
                new MockHttpServletRequest());
        assertEquals("You have successfully changed your password. ", actualProcessResetPasswordResult.getBody());
        assertEquals("<201 CREATED Created,You have successfully changed your password. ,[]>",
                actualProcessResetPasswordResult.toString());
        assertEquals(HttpStatus.CREATED, actualProcessResetPasswordResult.getStatusCode());
        assertTrue(actualProcessResetPasswordResult.getHeaders().isEmpty());
        verify(personDetailsService).getByResetPasswordToken((String) any());
        verify(personDetailsService).updatePassword((Person) any(), (String) any());
    }
}

