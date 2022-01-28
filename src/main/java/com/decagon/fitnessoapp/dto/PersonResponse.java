package com.decagon.fitnessoapp.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
public class PersonResponse {
    private String firstName;
    private String lastName;
    private String email;
}
