package com.decagon.fitnessoapp.service;

import com.decagon.fitnessoapp.dto.transactionDto.TransactionRequestDTO;
import com.decagon.fitnessoapp.dto.transactionDto.TransactionResponseDTO;
import com.decagon.fitnessoapp.dto.transactionDto.response.TransVerificationResponse;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface TransactionService {
    TransactionResponseDTO initializeTransaction(TransactionRequestDTO initializeTransactionRequestDTO);

    TransVerificationResponse confirmation(String reference);
}
