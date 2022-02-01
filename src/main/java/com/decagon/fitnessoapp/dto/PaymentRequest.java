package com.decagon.fitnessoapp.dto;

import lombok.Data;

@Data
public class PaymentRequest {

    private Long cardNumber;

    private String expiringDate;

    private Integer cvvNumber;
}
