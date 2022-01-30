package com.decagon.fitnessoapp.service.serviceImplementation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.decagon.fitnessoapp.Email.EmailService;
import com.decagon.fitnessoapp.dto.AuthRequest;
import com.decagon.fitnessoapp.dto.PersonRequest;
import com.decagon.fitnessoapp.dto.ResetPasswordRequest;
import com.decagon.fitnessoapp.dto.UpdatePersonDetails;
import com.decagon.fitnessoapp.exception.CustomServiceExceptions;
import com.decagon.fitnessoapp.exception.PersonNotFoundException;
import com.decagon.fitnessoapp.model.user.Person;
import com.decagon.fitnessoapp.model.user.ROLE_DETAIL;
import com.decagon.fitnessoapp.model.user.VerificationToken;
import com.decagon.fitnessoapp.repository.PersonRepository;
import com.decagon.fitnessoapp.repository.VerificationTokenRepository;
import com.decagon.fitnessoapp.security.JwtUtils;
import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.errors.MailjetSocketTimeoutException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.intercept.RunAsImplAuthenticationProvider;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.jaas.DefaultJaasAuthenticationProvider;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;

class PersonServiceImplTest {
    @Test
    void testRegister() throws MailjetException, MailjetSocketTimeoutException {
        ArrayList<AuthenticationProvider> authenticationProviderList = new ArrayList<>();
        authenticationProviderList.add(new RunAsImplAuthenticationProvider());
        ProviderManager authenticationManager = new ProviderManager(authenticationProviderList);
        VerificationTokenServiceImpl verificationTokenService = new VerificationTokenServiceImpl(
                mock(VerificationTokenRepository.class), mock(PersonRepository.class));

        Argon2PasswordEncoder bCryptPasswordEncoder = new Argon2PasswordEncoder();
        PersonRepository personRepository = mock(PersonRepository.class);
        EmailValidator emailValidator = new EmailValidator();
        ModelMapper modelMapper = new ModelMapper();
        EmailService emailSender = mock(EmailService.class);
        JwtUtils jwtUtils = new JwtUtils();
        PersonServiceImpl personServiceImpl = new PersonServiceImpl(verificationTokenService, bCryptPasswordEncoder,
                personRepository, emailValidator, modelMapper, emailSender, jwtUtils,
                new PersonDetailsService(mock(PersonRepository.class)), authenticationManager);

        PersonRequest personRequest = new PersonRequest();
        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        personRequest.setDateOfBirth(Date.from(atStartOfDayResult.atZone(ZoneId.of("UTC")).toInstant()));
        personRequest.setEmail("jane.doe@example.org");
        personRequest.setFirstName("Jane");
        personRequest.setGender("Gender");
        personRequest.setImage("Image");
        personRequest.setLastName("Doe");
        personRequest.setPassword("iloveyou");
        personRequest.setPhoneNumber("4105551212");
        personRequest.setRoleDetail(ROLE_DETAIL.ANONYMOUS);
        personRequest.setUserName("janedoe");
        assertThrows(CustomServiceExceptions.class, () -> personServiceImpl.register(personRequest));
    }



    @Test
    void testSendingEmail() throws MailjetException, MailjetSocketTimeoutException {
        Person person = new Person();
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
        person.setRoleDetail(ROLE_DETAIL.ANONYMOUS);
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
        person1.setRoleDetail(ROLE_DETAIL.ANONYMOUS);
        person1.setUserName("janedoe");
        person1.setVerifyEmail(true);
        PersonRepository personRepository = mock(PersonRepository.class);
        when(personRepository.findByEmail((String) any())).thenReturn(Optional.of(person1));
        EmailService emailService = mock(EmailService.class);
        doNothing().when(emailService).sendMessage((String) any(), (String) any(), (String) any());

        ArrayList<AuthenticationProvider> authenticationProviderList = new ArrayList<>();
        authenticationProviderList.add(new RunAsImplAuthenticationProvider());
        ProviderManager authenticationManager = new ProviderManager(authenticationProviderList);
        Argon2PasswordEncoder bCryptPasswordEncoder = new Argon2PasswordEncoder();
        EmailValidator emailValidator = new EmailValidator();
        ModelMapper modelMapper = new ModelMapper();
        JwtUtils jwtUtils = new JwtUtils();
        PersonServiceImpl personServiceImpl = new PersonServiceImpl(verificationTokenService, bCryptPasswordEncoder,
                personRepository, emailValidator, modelMapper, emailService, jwtUtils,
                new PersonDetailsService(mock(PersonRepository.class)), authenticationManager);

        PersonRequest personRequest = new PersonRequest();
        LocalDateTime atStartOfDayResult2 = LocalDate.of(1970, 1, 1).atStartOfDay();
        personRequest.setDateOfBirth(Date.from(atStartOfDayResult2.atZone(ZoneId.of("UTC")).toInstant()));
        personRequest.setEmail("jane.doe@example.org");
        personRequest.setFirstName("Jane");
        personRequest.setGender("Gender");
        personRequest.setImage("Image");
        personRequest.setLastName("Doe");
        personRequest.setPassword("iloveyou");
        personRequest.setPhoneNumber("4105551212");
        personRequest.setRoleDetail(ROLE_DETAIL.ANONYMOUS);
        personRequest.setUserName("janedoe");
        personServiceImpl.sendingEmail(personRequest);
        verify(verificationTokenRepository).save((VerificationToken) any());
        verify(personRepository).findByEmail((String) any());
        verify(emailService).sendMessage((String) any(), (String) any(), (String) any());
    }



    @Test
    void testLoginUser() throws Exception {
        ArrayList<AuthenticationProvider> authenticationProviderList = new ArrayList<>();
        authenticationProviderList.add(new RunAsImplAuthenticationProvider());
        ProviderManager authenticationManager = new ProviderManager(authenticationProviderList);
        VerificationTokenServiceImpl verificationTokenService = new VerificationTokenServiceImpl(
                mock(VerificationTokenRepository.class), mock(PersonRepository.class));

        Argon2PasswordEncoder bCryptPasswordEncoder = new Argon2PasswordEncoder();
        PersonRepository personRepository = mock(PersonRepository.class);
        EmailValidator emailValidator = new EmailValidator();
        ModelMapper modelMapper = new ModelMapper();
        EmailService emailSender = mock(EmailService.class);
        JwtUtils jwtUtils = new JwtUtils();
        PersonServiceImpl personServiceImpl = new PersonServiceImpl(verificationTokenService, bCryptPasswordEncoder,
                personRepository, emailValidator, modelMapper, emailSender, jwtUtils,
                new PersonDetailsService(mock(PersonRepository.class)), authenticationManager);

        AuthRequest authRequest = new AuthRequest();
        authRequest.setPassword("iloveyou");
        authRequest.setUsername("janedoe");
        assertThrows(Exception.class, () -> personServiceImpl.loginUser(authRequest));
    }


    @Test
    void testUpdateUserDetails() {
        Person person = new Person();
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
        person.setRoleDetail(ROLE_DETAIL.ANONYMOUS);
        person.setUserName("janedoe");
        person.setVerifyEmail(true);
        Optional<Person> ofResult = Optional.of(person);

        Person person1 = new Person();
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
        person1.setRoleDetail(ROLE_DETAIL.ANONYMOUS);
        person1.setUserName("janedoe");
        person1.setVerifyEmail(true);
        PersonRepository personRepository = mock(PersonRepository.class);
        when(personRepository.save((Person) any())).thenReturn(person1);
        when(personRepository.findPersonByUserName((String) any())).thenReturn(ofResult);

        ArrayList<AuthenticationProvider> authenticationProviderList = new ArrayList<>();
        authenticationProviderList.add(new RunAsImplAuthenticationProvider());
        ProviderManager authenticationManager = new ProviderManager(authenticationProviderList);
        VerificationTokenServiceImpl verificationTokenService = new VerificationTokenServiceImpl(
                mock(VerificationTokenRepository.class), mock(PersonRepository.class));

        Argon2PasswordEncoder bCryptPasswordEncoder = new Argon2PasswordEncoder();
        EmailValidator emailValidator = new EmailValidator();
        ModelMapper modelMapper = new ModelMapper();
        EmailService emailSender = mock(EmailService.class);
        JwtUtils jwtUtils = new JwtUtils();
        PersonServiceImpl personServiceImpl = new PersonServiceImpl(verificationTokenService, bCryptPasswordEncoder,
                personRepository, emailValidator, modelMapper, emailSender, jwtUtils,
                new PersonDetailsService(mock(PersonRepository.class)), authenticationManager);
        LocalDateTime atStartOfDayResult2 = LocalDate.of(1970, 1, 1).atStartOfDay();
        assertEquals("user details updated",
                personServiceImpl.updateUserDetails(new UpdatePersonDetails("janedoe", "Jane", "Doe", "jane.doe@example.org",
                        "Gender", Date.from(atStartOfDayResult2.atZone(ZoneId.of("UTC")).toInstant()), "4105551212")));
        verify(personRepository).findPersonByUserName((String) any());
        verify(personRepository).save((Person) any());
    }



    @Test
    void testResetPasswordToken() throws MailjetException, MailjetSocketTimeoutException {
        Person person = new Person();
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
        person.setRoleDetail(ROLE_DETAIL.ANONYMOUS);
        person.setUserName("janedoe");
        person.setVerifyEmail(true);
        Optional<Person> ofResult = Optional.of(person);

        Person person1 = new Person();
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
        person1.setRoleDetail(ROLE_DETAIL.ANONYMOUS);
        person1.setUserName("janedoe");
        person1.setVerifyEmail(true);
        PersonRepository personRepository = mock(PersonRepository.class);
        when(personRepository.save((Person) any())).thenReturn(person1);
        when(personRepository.findByEmail((String) any())).thenReturn(ofResult);
        EmailService emailService = mock(EmailService.class);
        doNothing().when(emailService).sendMessage((String) any(), (String) any(), (String) any());

        ArrayList<AuthenticationProvider> authenticationProviderList = new ArrayList<>();
        authenticationProviderList.add(new RunAsImplAuthenticationProvider());
        ProviderManager authenticationManager = new ProviderManager(authenticationProviderList);
        VerificationTokenServiceImpl verificationTokenService = new VerificationTokenServiceImpl(
                mock(VerificationTokenRepository.class), mock(PersonRepository.class));

        Argon2PasswordEncoder bCryptPasswordEncoder = new Argon2PasswordEncoder();
        EmailValidator emailValidator = new EmailValidator();
        ModelMapper modelMapper = new ModelMapper();
        JwtUtils jwtUtils = new JwtUtils();
        assertEquals("email sent",
                (new PersonServiceImpl(verificationTokenService, bCryptPasswordEncoder, personRepository, emailValidator,
                        modelMapper, emailService, jwtUtils, new PersonDetailsService(mock(PersonRepository.class)),
                        authenticationManager)).resetPasswordToken("jane.doe@example.org"));
        verify(personRepository).findByEmail((String) any());
        verify(personRepository).save((Person) any());
        verify(emailService).sendMessage((String) any(), (String) any(), (String) any());
    }



    @Test
    void testUpdateResetPassword() {
        PersonRepository personRepository = mock(PersonRepository.class);
        when(personRepository.findByResetPasswordToken((String) any()))
                .thenThrow(new CustomServiceExceptions("An error occurred"));

        ArrayList<AuthenticationProvider> authenticationProviderList = new ArrayList<>();
        authenticationProviderList.add(new RunAsImplAuthenticationProvider());
        ProviderManager authenticationManager = new ProviderManager(authenticationProviderList);
        VerificationTokenServiceImpl verificationTokenService = new VerificationTokenServiceImpl(
                mock(VerificationTokenRepository.class), mock(PersonRepository.class));

        Argon2PasswordEncoder bCryptPasswordEncoder = new Argon2PasswordEncoder();
        EmailValidator emailValidator = new EmailValidator();
        ModelMapper modelMapper = new ModelMapper();
        EmailService emailSender = mock(EmailService.class);
        JwtUtils jwtUtils = new JwtUtils();
        PersonServiceImpl personServiceImpl = new PersonServiceImpl(verificationTokenService, bCryptPasswordEncoder,
                personRepository, emailValidator, modelMapper, emailSender, jwtUtils,
                new PersonDetailsService(mock(PersonRepository.class)), authenticationManager);

        ResetPasswordRequest resetPasswordRequest = new ResetPasswordRequest();
        resetPasswordRequest.setConfirmPassword("iloveyou");
        resetPasswordRequest.setNewPassword("iloveyou");
        assertThrows(CustomServiceExceptions.class,
                () -> personServiceImpl.updateResetPassword(resetPasswordRequest, "ABC123"));
        verify(personRepository).findByResetPasswordToken((String) any());
    }

    @Test
    void testResetPasswordMailSender() throws MailjetException, MailjetSocketTimeoutException {
        EmailService emailService = mock(EmailService.class);
        doNothing().when(emailService).sendMessage((String) any(), (String) any(), (String) any());

        ArrayList<AuthenticationProvider> authenticationProviderList = new ArrayList<>();
        authenticationProviderList.add(new RunAsImplAuthenticationProvider());
        ProviderManager authenticationManager = new ProviderManager(authenticationProviderList);
        VerificationTokenServiceImpl verificationTokenService = new VerificationTokenServiceImpl(
                mock(VerificationTokenRepository.class), mock(PersonRepository.class));

        Argon2PasswordEncoder bCryptPasswordEncoder = new Argon2PasswordEncoder();
        PersonRepository personRepository = mock(PersonRepository.class);
        EmailValidator emailValidator = new EmailValidator();
        ModelMapper modelMapper = new ModelMapper();
        JwtUtils jwtUtils = new JwtUtils();
        (new PersonServiceImpl(verificationTokenService, bCryptPasswordEncoder, personRepository, emailValidator,
                modelMapper, emailService, jwtUtils, new PersonDetailsService(mock(PersonRepository.class)),
                authenticationManager)).resetPasswordMailSender("jane.doe@example.org", "ABC123");
        verify(emailService).sendMessage((String) any(), (String) any(), (String) any());
    }


}

