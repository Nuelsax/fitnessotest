package com.decagon.fitnessoapp.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.decagon.fitnessoapp.Email.EmailService;
import com.decagon.fitnessoapp.dto.AuthRequest;
import com.decagon.fitnessoapp.dto.ChangePasswordRequest;
import com.decagon.fitnessoapp.dto.EmailRequest;
import com.decagon.fitnessoapp.dto.PersonRequest;
import com.decagon.fitnessoapp.dto.PersonResponse;
import com.decagon.fitnessoapp.dto.ResetPasswordRequest;
import com.decagon.fitnessoapp.dto.UpdatePersonRequest;
import com.decagon.fitnessoapp.model.user.Person;
import com.decagon.fitnessoapp.model.user.ROLE_DETAIL;
import com.decagon.fitnessoapp.repository.PersonRepository;
import com.decagon.fitnessoapp.repository.VerificationTokenRepository;
import com.decagon.fitnessoapp.security.JwtUtils;
import com.decagon.fitnessoapp.service.PersonService;
import com.decagon.fitnessoapp.service.VerificationService;
import com.decagon.fitnessoapp.service.serviceImplementation.EmailValidator;
import com.decagon.fitnessoapp.service.serviceImplementation.PersonDetailsService;
import com.decagon.fitnessoapp.service.serviceImplementation.PersonServiceImpl;
import com.decagon.fitnessoapp.service.serviceImplementation.VerificationTokenServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.intercept.RunAsImplAuthenticationProvider;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ContextConfiguration(classes = {PersonController.class})
@ExtendWith(SpringExtension.class)
class PersonControllerTest {
    @Autowired
    private PersonController personController;

    @MockBean
    private PersonService personService;

    @MockBean
    private VerificationService verificationService;

    @Test
    void testAdminProcessResetPassword() throws Exception {
        when(this.personService.resetPasswordToken((String) any())).thenReturn("ABC123");

        EmailRequest emailRequest = new EmailRequest();
        emailRequest.setEmail("jane.doe@example.org");
        String content = (new ObjectMapper()).writeValueAsString(emailRequest);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/person/admin/reset_password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MockMvcBuilders.standaloneSetup(this.personController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(MockMvcResultMatchers.content().string("ABC123"));
    }

    @Test
    void testEditUserDetails() {
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
        PersonServiceImpl personService = new PersonServiceImpl(verificationTokenService, bCryptPasswordEncoder,
                personRepository, emailValidator, modelMapper, emailSender, jwtUtils,
                new PersonDetailsService(mock(PersonRepository.class)), authenticationManager);

        PersonController personController = new PersonController(personService,
                new VerificationTokenServiceImpl(mock(VerificationTokenRepository.class), mock(PersonRepository.class)));
        LocalDateTime atStartOfDayResult2 = LocalDate.of(1970, 1, 1).atStartOfDay();
        ResponseEntity<PersonResponse> actualEditUserDetailsResult = personController
                .editUserDetails(new UpdatePersonRequest("janedoe", "Jane", "Doe", "jane.doe@example.org", "Gender",
                        Date.from(atStartOfDayResult2.atZone(ZoneId.of("UTC")).toInstant()), "4105551212"));
        PersonResponse personResponse = new PersonResponse();
        ModelMapper mapper = new ModelMapper();
        mapper.map(personResponse, actualEditUserDetailsResult.getBody());
        assertEquals(personResponse, actualEditUserDetailsResult.getBody());
        assertEquals("<200 OK OK,"+personResponse+",[]>", actualEditUserDetailsResult.toString());
        assertEquals(HttpStatus.OK, actualEditUserDetailsResult.getStatusCode());
        assertTrue(actualEditUserDetailsResult.getHeaders().isEmpty());
        verify(personRepository).findPersonByUserName((String) any());
        verify(personRepository).save((Person) any());
    }

    @Test
    void testEditUserPassword() {
        PersonServiceImpl personServiceImpl = mock(PersonServiceImpl.class);
        when(personServiceImpl.updateCurrentPassword((ChangePasswordRequest) any())).thenReturn("2020-03-01");
        PersonController personController = new PersonController(personServiceImpl,
                new VerificationTokenServiceImpl(mock(VerificationTokenRepository.class), mock(PersonRepository.class)));
        ResponseEntity<String> actualEditUserPasswordResult = personController
                .editUserPassword(new ChangePasswordRequest("iloveyou", "iloveyou", "iloveyou","iloveyou"));
        assertEquals("2020-03-01", actualEditUserPasswordResult.getBody());
        assertEquals("<200 OK OK,2020-03-01,[]>", actualEditUserPasswordResult.toString());
        assertEquals(HttpStatus.OK, actualEditUserPasswordResult.getStatusCode());
        assertTrue(actualEditUserPasswordResult.getHeaders().isEmpty());
        verify(personServiceImpl).updateCurrentPassword((ChangePasswordRequest) any());
    }

    @Test
    void testRegister() throws Exception {
        PersonResponse personResponse = new PersonResponse();
        personResponse.setEmail("jane.doe@example.org");
        personResponse.setFirstName("Jane");
        personResponse.setLastName("Doe");
        when(this.personService.register((PersonRequest) any())).thenReturn(personResponse);

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
        String content = (new ObjectMapper()).writeValueAsString(personRequest);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/person/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MockMvcBuilders.standaloneSetup(this.personController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string("{\"firstName\":\"Jane\",\"lastName\":\"Doe\",\"email\":\"jane.doe@example.org\"}"));
    }

    @Test
    void testRegisterAdmin() throws Exception {
        PersonResponse personResponse = new PersonResponse();
        personResponse.setEmail("jane.doe@example.org");
        personResponse.setFirstName("Jane");
        personResponse.setLastName("Doe");
        when(this.personService.register((PersonRequest) any())).thenReturn(personResponse);

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
        String content = (new ObjectMapper()).writeValueAsString(personRequest);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/person/admin/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MockMvcBuilders.standaloneSetup(this.personController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string("{\"firstName\":\"Jane\",\"lastName\":\"Doe\",\"email\":\"jane.doe@example.org\"}"));
    }

    @Test
    void testAdminUpdatePassword() throws Exception {
        when(this.personService.updateResetPassword((ResetPasswordRequest) any(), (String) any())).thenReturn("2020-03-01");

        ResetPasswordRequest resetPasswordRequest = new ResetPasswordRequest();
        resetPasswordRequest.setConfirmPassword("iloveyou");
        resetPasswordRequest.setNewPassword("iloveyou");
        String content = (new ObjectMapper()).writeValueAsString(resetPasswordRequest);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/person/admin/update_password")
                .param("token", "foo")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MockMvcBuilders.standaloneSetup(this.personController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(MockMvcResultMatchers.content().string("2020-03-01"));
    }

    @Test
    void testConfirm() throws Exception {
        when(this.verificationService.confirmToken((String) any())).thenReturn("ABC123");
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/person/confirm").param("token", "foo");
        MockMvcBuilders.standaloneSetup(this.personController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(MockMvcResultMatchers.content().string("ABC123"));
    }

    @Test
    void testLogin() throws Exception {
        when(this.personService.loginUser((AuthRequest) any())).thenReturn(new ResponseEntity<>(HttpStatus.CONTINUE));

        AuthRequest authRequest = new AuthRequest();
        authRequest.setPassword("iloveyou");
        authRequest.setUsername("janedoe");
        String content = (new ObjectMapper()).writeValueAsString(authRequest);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/person/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(this.personController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(100));
    }

    @Test
    void testProcessResetPassword() throws Exception {
        when(this.personService.resetPasswordToken((String) any())).thenReturn("ABC123");

        EmailRequest emailRequest = new EmailRequest();
        emailRequest.setEmail("jane.doe@example.org");
        String content = (new ObjectMapper()).writeValueAsString(emailRequest);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/person/reset_password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MockMvcBuilders.standaloneSetup(this.personController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(MockMvcResultMatchers.content().string("ABC123"));
    }

    @Test
    void testUpdatePassword() throws Exception {
        when(this.personService.updateResetPassword((ResetPasswordRequest) any(), (String) any())).thenReturn("2020-03-01");

        ResetPasswordRequest resetPasswordRequest = new ResetPasswordRequest();
        resetPasswordRequest.setConfirmPassword("iloveyou");
        resetPasswordRequest.setNewPassword("iloveyou");
        String content = (new ObjectMapper()).writeValueAsString(resetPasswordRequest);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/person/update_password")
                .param("token", "foo")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MockMvcBuilders.standaloneSetup(this.personController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(MockMvcResultMatchers.content().string("2020-03-01"));
    }
}

