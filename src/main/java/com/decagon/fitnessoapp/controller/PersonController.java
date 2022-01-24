package com.decagon.fitnessoapp.controller;

import com.decagon.fitnessoapp.model.dto.ChangePassword;
import com.decagon.fitnessoapp.model.dto.UpdatePersonDetails;
import com.decagon.fitnessoapp.service.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PersonController {

    private final PersonService personService;

    @PutMapping("/profile/edit/personinfo")
    public void editUserDetails(@RequestBody UpdatePersonDetails updatePersonDetails) {
        personService.updateUserDetails(updatePersonDetails);
    }

    @PutMapping("/profile/edit/password")
    public void editUserPassword(@RequestBody ChangePassword changePassword) {
        personService.updatePassword(changePassword);
    }
}
