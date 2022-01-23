package com.decagon.fitnessoapp.controller;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

import com.decagon.fitnessoapp.dto.PersonDto;
import com.decagon.fitnessoapp.model.user.Person;
import com.decagon.fitnessoapp.model.user.Role;
import com.decagon.fitnessoapp.service.PersonService;
import com.decagon.fitnessoapp.service.VerificationService;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
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
    void testRegister() throws Exception {
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
        when(this.personService.register((PersonDto) any())).thenReturn(person);

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
        String content = (new ObjectMapper()).writeValueAsString(personDto);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/person/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MockMvcBuilders.standaloneSetup(this.personController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"id\":123,\"userName\":\"janedoe\",\"firstName\":\"Jane\",\"lastName\":\"Doe\",\"email\":\"jane.doe@example.org\","
                                        + "\"password\":\"iloveyou\",\"phoneNumber\":\"4105551212\",\"address\":[],\"role\":\"ANONYMOUS\",\"verifyEmail\":true,"
                                        + "\"gender\":\"Gender\",\"dateOfBirth\":0,\"image\":\"Image\"}"));
    }
}

