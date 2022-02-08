package com.decagon.fitnessoapp.dto.transactionDto;


import lombok.Data;

@Data
public class InitializeTransactionResponseDTO {
    private String status;
    private String message;
    private PaymentData data;
}
