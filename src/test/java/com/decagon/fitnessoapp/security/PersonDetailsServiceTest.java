package com.decagon.fitnessoapp.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.decagon.fitnessoapp.Email.EmailService;
import com.decagon.fitnessoapp.dto.ChangePassword;
import com.decagon.fitnessoapp.dto.PersonRequest;
import com.decagon.fitnessoapp.dto.UpdatePersonDetails;
import com.decagon.fitnessoapp.exception.CustomServiceExceptions;
import com.decagon.fitnessoapp.exception.PersonNotFoundException;
import com.decagon.fitnessoapp.model.user.Person;
import com.decagon.fitnessoapp.model.user.Role;
import com.decagon.fitnessoapp.repository.PersonRepository;
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
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {PersonDetailsService.class, String.class})
@ExtendWith(SpringExtension.class)
class PersonDetailsServiceTest {
    @MockBean
    private EmailService emailService;

    @MockBean
    private EmailValidator emailValidator;

    @MockBean
    private ModelMapper modelMapper;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @Autowired
    private PersonDetailsService personDetailsService;

    @MockBean
    private PersonRepository personRepository;

    @MockBean
    private VerificationTokenServiceImpl verificationTokenServiceImpl;

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
        Optional<Person> ofResult = Optional.of(person);
        when(this.personRepository.findByUserName((String) any())).thenReturn(ofResult);
        PersonDetails actualLoadUserByUsernameResult = this.personDetailsService.loadUserByUsername("janedoe");
        Collection<? extends GrantedAuthority> authorities = actualLoadUserByUsernameResult.getAuthorities();
        assertEquals(1, authorities.size());
        assertTrue(actualLoadUserByUsernameResult.isEnabled());
        assertEquals("iloveyou", actualLoadUserByUsernameResult.getPassword());
        assertEquals("janedoe", actualLoadUserByUsernameResult.getUsername());
        assertEquals("ANONYMOUS", ((List<? extends GrantedAuthority>) authorities).get(0).getAuthority());
        verify(this.personRepository).findByUserName((String) any());
    }

    @Test
    void testLoadUserByUsername2() throws UsernameNotFoundException {
        when(this.personRepository.findByUserName((String) any()))
                .thenThrow(new CustomServiceExceptions("An error occurred"));
        assertThrows(CustomServiceExceptions.class, () -> this.personDetailsService.loadUserByUsername("janedoe"));
        verify(this.personRepository).findByUserName((String) any());
    }

    @Test
    void testLoadUserByUsername3() throws UsernameNotFoundException {
        when(this.personRepository.findByUserName((String) any())).thenReturn(Optional.empty());
        assertThrows(UsernameNotFoundException.class, () -> this.personDetailsService.loadUserByUsername("janedoe"));
        verify(this.personRepository).findByUserName((String) any());
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
        Optional<Person> ofResult = Optional.of(person);
        when(this.personRepository.findByEmail((String) any())).thenReturn(ofResult);
        when(this.emailValidator.test((String) any())).thenReturn(true);

        PersonRequest personDto = new PersonRequest();
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
        assertThrows(CustomServiceExceptions.class, () -> this.personDetailsService.register(personDto));
        verify(this.personRepository).findByEmail((String) any());
        verify(this.emailValidator).test((String) any());
    }

    @Test
    void testRegister2() throws MailjetException, MailjetSocketTimeoutException {
        when(this.personRepository.findByEmail((String) any())).thenThrow(new CustomServiceExceptions("An error occurred"));
        when(this.emailValidator.test((String) any())).thenReturn(true);

        PersonRequest personDto = new PersonRequest();
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
        assertThrows(CustomServiceExceptions.class, () -> this.personDetailsService.register(personDto));
        verify(this.personRepository).findByEmail((String) any());
        verify(this.emailValidator).test((String) any());
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
        when(this.personRepository.save((Person) any())).thenReturn(person);
        when(this.personRepository.findByEmail((String) any())).thenReturn(Optional.empty());
        when(this.passwordEncoder.encode((CharSequence) any())).thenReturn("secret");
        doNothing().when(this.modelMapper).map((Object) any(), (Object) any());
        when(this.emailValidator.test((String) any())).thenReturn(true);

        PersonRequest personDto = new PersonRequest();
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
        assertThrows(CustomServiceExceptions.class, () -> this.personDetailsService.register(personDto));
        verify(this.personRepository, atLeast(1)).findByEmail((String) any());
        verify(this.personRepository).save((Person) any());
        verify(this.passwordEncoder).encode((CharSequence) any());
        verify(this.modelMapper).map((Object) any(), (Object) any());
        verify(this.emailValidator).test((String) any());
    }

    @Test
    void testRegister4() throws MailjetException, MailjetSocketTimeoutException {
        when(this.personRepository.save((Person) any())).thenThrow(new CustomServiceExceptions("An error occurred"));
        when(this.personRepository.findByEmail((String) any())).thenReturn(Optional.empty());
        when(this.passwordEncoder.encode((CharSequence) any())).thenReturn("secret");
        doNothing().when(this.modelMapper).map((Object) any(), (Object) any());
        when(this.emailValidator.test((String) any())).thenReturn(true);

        PersonRequest personDto = new PersonRequest();
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
        assertThrows(CustomServiceExceptions.class, () -> this.personDetailsService.register(personDto));
        verify(this.personRepository).findByEmail((String) any());
        verify(this.personRepository).save((Person) any());
        verify(this.passwordEncoder).encode((CharSequence) any());
        verify(this.modelMapper).map((Object) any(), (Object) any());
        verify(this.emailValidator).test((String) any());
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
        when(this.personRepository.save((Person) any())).thenReturn(person);
        when(this.personRepository.findByEmail((String) any())).thenReturn(Optional.empty());
        when(this.passwordEncoder.encode((CharSequence) any())).thenReturn("secret");
        doNothing().when(this.modelMapper).map((Object) any(), (Object) any());
        when(this.emailValidator.test((String) any())).thenReturn(false);

        PersonRequest personDto = new PersonRequest();
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
        assertThrows(CustomServiceExceptions.class, () -> this.personDetailsService.register(personDto));
        verify(this.emailValidator).test((String) any());
    }

    @Test
    void testSendingEmail() throws MailjetException, MailjetSocketTimeoutException {
        when(this.verificationTokenServiceImpl.saveVerificationToken((Person) any())).thenReturn("ABC123");

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
        when(this.personRepository.findByEmail((String) any())).thenReturn(ofResult);
        doNothing().when(this.emailService).sendMessage((String) any(), (String) any(), (String) any());

        PersonRequest personDto = new PersonRequest();
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
        this.personDetailsService.sendingEmail(personDto);
        verify(this.verificationTokenServiceImpl).saveVerificationToken((Person) any());
        verify(this.personRepository).findByEmail((String) any());
        verify(this.emailService).sendMessage((String) any(), (String) any(), (String) any());
    }

    @Test
    void testSendingEmail2() throws MailjetException, MailjetSocketTimeoutException {
        when(this.verificationTokenServiceImpl.saveVerificationToken((Person) any())).thenReturn("ABC123");

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
        when(this.personRepository.findByEmail((String) any())).thenReturn(ofResult);
        doThrow(new CustomServiceExceptions("An error occurred")).when(this.emailService)
                .sendMessage((String) any(), (String) any(), (String) any());

        PersonRequest personDto = new PersonRequest();
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
        assertThrows(CustomServiceExceptions.class, () -> this.personDetailsService.sendingEmail(personDto));
        verify(this.verificationTokenServiceImpl).saveVerificationToken((Person) any());
        verify(this.personRepository).findByEmail((String) any());
        verify(this.emailService).sendMessage((String) any(), (String) any(), (String) any());
    }

    @Test
    void testSendingEmail3() throws MailjetException, MailjetSocketTimeoutException {
        when(this.verificationTokenServiceImpl.saveVerificationToken((Person) any())).thenReturn("ABC123");
        when(this.personRepository.findByEmail((String) any())).thenReturn(Optional.empty());
        doNothing().when(this.emailService).sendMessage((String) any(), (String) any(), (String) any());

        PersonRequest personDto = new PersonRequest();
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
        assertThrows(CustomServiceExceptions.class, () -> this.personDetailsService.sendingEmail(personDto));
        verify(this.personRepository).findByEmail((String) any());
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
        when(this.personRepository.save((Person) any())).thenReturn(person1);
        when(this.personRepository.findByEmail((String) any())).thenReturn(ofResult);
        doNothing().when(this.emailService).sendMessage((String) any(), (String) any(), (String) any());
        this.personDetailsService.updateResetPasswordToken("ABC123", "jane.doe@example.org");
        verify(this.personRepository).findByEmail((String) any());
        verify(this.personRepository).save((Person) any());
        verify(this.emailService).sendMessage((String) any(), (String) any(), (String) any());
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
        Optional<Person> ofResult = Optional.of(person);
        when(this.personRepository.findByResetPasswordToken((String) any())).thenReturn(ofResult);
        assertSame(person, this.personDetailsService.getByResetPasswordToken("ABC123"));
        verify(this.personRepository).findByResetPasswordToken((String) any());
    }

    @Test
    void testGetByResetPasswordToken2() {
        when(this.personRepository.findByResetPasswordToken((String) any()))
                .thenThrow(new CustomServiceExceptions("An error occurred"));
        assertThrows(CustomServiceExceptions.class, () -> this.personDetailsService.getByResetPasswordToken("ABC123"));
        verify(this.personRepository).findByResetPasswordToken((String) any());
    }

    @Test
    void testUpdatePassword() {
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
        when(this.personRepository.save((Person) any())).thenReturn(person);
        when(this.passwordEncoder.encode((CharSequence) any())).thenReturn("secret");

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
        this.personDetailsService.updatePassword(person1, "iloveyou");
        verify(this.personRepository).save((Person) any());
        verify(this.passwordEncoder).encode((CharSequence) any());
        assertNull(person1.getResetPasswordToken());
        assertEquals("secret", person1.getPassword());
    }

    @Test
    void testUpdatePassword2() {
        when(this.personRepository.save((Person) any())).thenThrow(new CustomServiceExceptions("An error occurred"));
        when(this.passwordEncoder.encode((CharSequence) any())).thenReturn("secret");

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
        assertThrows(CustomServiceExceptions.class, () -> this.personDetailsService.updatePassword(person, "iloveyou"));
        verify(this.personRepository).save((Person) any());
        verify(this.passwordEncoder).encode((CharSequence) any());
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
        when(this.personRepository.save((Person) any())).thenReturn(person1);
        when(this.personRepository.findPersonByUserName((String) any())).thenReturn(ofResult);
        LocalDateTime atStartOfDayResult2 = LocalDate.of(1970, 1, 1).atStartOfDay();
        this.personDetailsService.updateUserDetails(new UpdatePersonDetails("janedoe", "Jane", "Doe",
                "jane.doe@example.org", "Gender", Date.from(atStartOfDayResult2.atZone(ZoneId.of("UTC")).toInstant())));
        verify(this.personRepository).findPersonByUserName((String) any());
        verify(this.personRepository).save((Person) any());
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
        when(this.personRepository.save((Person) any())).thenThrow(new CustomServiceExceptions("An error occurred"));
        when(this.personRepository.findPersonByUserName((String) any())).thenReturn(ofResult);
        LocalDateTime atStartOfDayResult1 = LocalDate.of(1970, 1, 1).atStartOfDay();
        assertThrows(CustomServiceExceptions.class,
                () -> this.personDetailsService.updateUserDetails(new UpdatePersonDetails("janedoe", "Jane", "Doe",
                        "jane.doe@example.org", "Gender", Date.from(atStartOfDayResult1.atZone(ZoneId.of("UTC")).toInstant()))));
        verify(this.personRepository).findPersonByUserName((String) any());
        verify(this.personRepository).save((Person) any());
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
        when(this.personRepository.save((Person) any())).thenReturn(person);
        when(this.personRepository.findPersonByUserName((String) any())).thenReturn(Optional.empty());
        LocalDateTime atStartOfDayResult1 = LocalDate.of(1970, 1, 1).atStartOfDay();
        assertThrows(PersonNotFoundException.class,
                () -> this.personDetailsService.updateUserDetails(new UpdatePersonDetails("janedoe", "Jane", "Doe",
                        "jane.doe@example.org", "Gender", Date.from(atStartOfDayResult1.atZone(ZoneId.of("UTC")).toInstant()))));
        verify(this.personRepository).findPersonByUserName((String) any());
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
        when(this.personRepository.save((Person) any())).thenReturn(person1);
        when(this.personRepository.findPersonByPassword((String) any())).thenReturn(ofResult);
        when(this.passwordEncoder.encode((CharSequence) any())).thenReturn("secret");
        this.personDetailsService.updateCurrentPassword(new ChangePassword("iloveyou", "iloveyou", "iloveyou"));
        verify(this.personRepository).findPersonByPassword((String) any());
        verify(this.personRepository).save((Person) any());
        verify(this.passwordEncoder).encode((CharSequence) any());
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
        when(this.personRepository.save((Person) any())).thenThrow(new CustomServiceExceptions("An error occurred"));
        when(this.personRepository.findPersonByPassword((String) any())).thenReturn(ofResult);
        when(this.passwordEncoder.encode((CharSequence) any())).thenReturn("secret");
        assertThrows(CustomServiceExceptions.class,
                () -> this.personDetailsService.updateCurrentPassword(new ChangePassword("iloveyou", "iloveyou", "iloveyou")));
        verify(this.personRepository).findPersonByPassword((String) any());
        verify(this.personRepository).save((Person) any());
        verify(this.passwordEncoder).encode((CharSequence) any());
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
        when(this.personRepository.save((Person) any())).thenReturn(person);
        when(this.personRepository.findPersonByPassword((String) any())).thenReturn(Optional.empty());
        when(this.passwordEncoder.encode((CharSequence) any())).thenReturn("secret");
        assertThrows(PersonNotFoundException.class,
                () -> this.personDetailsService.updateCurrentPassword(new ChangePassword("iloveyou", "iloveyou", "iloveyou")));
        verify(this.personRepository).findPersonByPassword((String) any());
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
        when(this.personRepository.save((Person) any())).thenReturn(person1);
        when(this.personRepository.findPersonByPassword((String) any())).thenReturn(ofResult);
        when(this.passwordEncoder.encode((CharSequence) any())).thenReturn("secret");
        this.personDetailsService.updateCurrentPassword(new ChangePassword("iloveyou", "New Password", "iloveyou"));
        verify(this.personRepository).findPersonByPassword((String) any());
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
        when(this.personRepository.save((Person) any())).thenReturn(person1);
        when(this.personRepository.findPersonByPassword((String) any())).thenReturn(ofResult);
        when(this.passwordEncoder.encode((CharSequence) any())).thenReturn("secret");
        ChangePassword changePassword = mock(ChangePassword.class);
        when(changePassword.getConfirmPassword()).thenThrow(new CustomServiceExceptions("An error occurred"));
        when(changePassword.getNewPassword()).thenReturn("iloveyou");
        when(changePassword.getCurrentPassword()).thenReturn("iloveyou");
        assertThrows(CustomServiceExceptions.class, () -> this.personDetailsService.updateCurrentPassword(changePassword));
        verify(this.personRepository).findPersonByPassword((String) any());
        verify(changePassword).getConfirmPassword();
        verify(changePassword).getCurrentPassword();
        verify(changePassword).getNewPassword();
    }

    @Test
    void testBuildEmail() {
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
                this.personDetailsService.buildEmail("Name", "Link"));
    }
}

