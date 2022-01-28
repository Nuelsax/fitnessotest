package com.decagon.fitnessoapp.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthResponse {
    private String token;
//    todo: francis
    private String role;
}
