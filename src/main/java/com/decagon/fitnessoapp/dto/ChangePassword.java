package com.decagon.fitnessoapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChangePassword {

    private String currentPassword;
    private String newPassword;
    private String confirmPassword;
}