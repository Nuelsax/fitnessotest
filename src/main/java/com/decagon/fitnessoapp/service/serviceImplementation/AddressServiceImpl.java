package com.decagon.fitnessoapp.service.serviceImplementation;

import com.decagon.fitnessoapp.dto.AddressRequest;
import com.decagon.fitnessoapp.exception.PersonNotFoundException;
import com.decagon.fitnessoapp.model.user.Address;
import com.decagon.fitnessoapp.model.user.Person;
import com.decagon.fitnessoapp.repository.AddressRepository;
import com.decagon.fitnessoapp.repository.PersonRepository;
import com.decagon.fitnessoapp.service.AddressService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class AddressServiceImpl implements AddressService {

    public final AddressRepository addressRepository;
    public final PersonRepository personRepository;
    public final ModelMapper modelMapper;

    @Autowired
    public AddressServiceImpl(AddressRepository addressRepository, PersonRepository personRepository, ModelMapper modelMapper) {
        this.addressRepository = addressRepository;
        this.personRepository = personRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public ResponseEntity<?> createAddress(AddressRequest addressRequest) {
        Person person = personRepository.findByUserName(addressRequest.getUserName())
                .orElseThrow(() -> new PersonNotFoundException("You have to register first"));
        Address address = new Address();
        modelMapper.map(addressRequest, address);
        address.setPerson(person);
        addressRepository.save(address);
        return ResponseEntity.ok().body("Address added successfully");
    }


}
