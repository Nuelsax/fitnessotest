package com.decagon.fitnessoapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.Access;
import java.util.Date;

@Data
@AllArgsConstructor
public class UpdatePersonDetails {
    private String userName;
    private String firstName;
    private String lastName;
    private String email;
    private String gender;
    private Date dateOfBirth;
}
