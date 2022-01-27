package com.decagon.fitnessoapp.dto;

import com.decagon.fitnessoapp.model.user.Address;
import com.decagon.fitnessoapp.model.user.Role;
import lombok.Data;
import java.util.Date;
import java.util.List;

@Data
public class PersonResponse {

    private String userName;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String phoneNumber;
    private List<Address> address;
    private String gender;
    private Date dateOfBirth;
}
