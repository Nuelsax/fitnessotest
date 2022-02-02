package com.decagon.fitnessoapp.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class CheckOutResponse {
    private String message;
    protected LocalDateTime timestamp;
}
