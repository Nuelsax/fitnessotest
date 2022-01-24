package com.decagon.fitnessoapp.service;

import com.decagon.fitnessoapp.model.dto.ChangePassword;
import com.decagon.fitnessoapp.model.dto.UpdatePersonDetails;


public interface PersonService {
    void updatePassword(ChangePassword changePassword);
    void updateUserDetails(UpdatePersonDetails updatePersonDetails);
}
