package com.decagon.fitnessoapp.controller;

import com.decagon.fitnessoapp.dto.transactionDto.TransactionRequestDTO;
import com.decagon.fitnessoapp.dto.transactionDto.TransactionResponseDTO;
import com.decagon.fitnessoapp.dto.transactionDto.response.PaymentResponse;
import com.decagon.fitnessoapp.dto.transactionDto.response.TransVerificationResponse;
import com.decagon.fitnessoapp.service.TransactionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transaction")
@AllArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping("/payment")
    public ResponseEntity<TransactionResponseDTO> initializeTransaction(
            @RequestBody TransactionRequestDTO initializeTransactionRequestDTO) throws JsonProcessingException {
        return ResponseEntity.ok(transactionService.initializeTransaction(initializeTransactionRequestDTO));
    }

    @GetMapping("/confirm-payment/{reference}")
    public ResponseEntity<TransVerificationResponse> confirmPayment(@PathVariable("reference") String reference){
        return ResponseEntity.ok(transactionService.confirmation(reference));
    }

    @GetMapping("/success")
    public ResponseEntity<PaymentResponse> checkTransactionStatus(){
        return ResponseEntity.ok(PaymentResponse.builder()
                .message("Transaction Successful").status("paid").build());
    }

    @GetMapping("/fail")
    public ResponseEntity<PaymentResponse> transactionDecline(){
        return ResponseEntity.ok(PaymentResponse.builder()
                .message("Transaction Failed").status("Pending").build());
    }
}
