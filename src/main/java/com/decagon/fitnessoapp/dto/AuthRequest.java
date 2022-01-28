package com.decagon.fitnessoapp.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class AuthRequest {
    private String username;
    private String password;
}
