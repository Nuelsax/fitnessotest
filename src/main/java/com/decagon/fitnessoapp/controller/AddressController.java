package com.decagon.fitnessoapp.controller;

import com.decagon.fitnessoapp.dto.AddressRequest;
import com.decagon.fitnessoapp.service.AddressService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/address")
@AllArgsConstructor
public class AddressController {

    public final AddressService addressService;

    @PostMapping("/addAddress")
    public ResponseEntity<?> addAddress(@RequestBody AddressRequest addressRequest){
        return addressService.createAddress(addressRequest);
    }
}
