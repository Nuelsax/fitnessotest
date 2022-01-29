package com.decagon.fitnessoapp.service.serviceImplementation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.decagon.fitnessoapp.dto.AddressRequest;
import com.decagon.fitnessoapp.exception.PersonNotFoundException;
import com.decagon.fitnessoapp.model.user.Address;
import com.decagon.fitnessoapp.model.user.Person;
import com.decagon.fitnessoapp.model.user.ROLE_DETAIL;
import com.decagon.fitnessoapp.repository.AddressRepository;
import com.decagon.fitnessoapp.repository.PersonRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {AddressServiceImpl.class})
@ExtendWith(SpringExtension.class)
class AddressServiceImplTest {
    @MockBean
    private AddressRepository addressRepository;

    @Autowired
    private AddressServiceImpl addressServiceImpl;

    @MockBean
    private ModelMapper modelMapper;

    @MockBean
    private PersonRepository personRepository;

    @Test
    void testCreateAddress() {
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
        when(this.personRepository.findByUserName((String) any())).thenReturn(ofResult);
        doNothing().when(this.modelMapper).map((Object) any(), (Object) any());

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

        Address address = new Address();
        address.setCity("Oxford");
        address.setCountry("GB");
        address.setId(123L);
        address.setPerson(person1);
        address.setState("MD");
        address.setStreetDetail("Street Detail");
        address.setZipCode("21654");
        when(this.addressRepository.save((Address) any())).thenReturn(address);

        AddressRequest addressRequest = new AddressRequest();
        addressRequest.setCity("Oxford");
        addressRequest.setCountry("GB");
        addressRequest.setState("MD");
        addressRequest.setStreetDetail("Street Detail");
        addressRequest.setUserName("janedoe");
        addressRequest.setZipCode("21654");
        ResponseEntity<?> actualCreateAddressResult = this.addressServiceImpl.createAddress(addressRequest);
        assertEquals("Address added successfully", actualCreateAddressResult.getBody());
        assertEquals("<200 OK OK,Address added successfully,[]>", actualCreateAddressResult.toString());
        assertEquals(HttpStatus.OK, actualCreateAddressResult.getStatusCode());
        assertTrue(actualCreateAddressResult.getHeaders().isEmpty());
        verify(this.personRepository).findByUserName((String) any());
        verify(this.modelMapper).map((Object) any(), (Object) any());
        verify(this.addressRepository).save((Address) any());
    }

}

