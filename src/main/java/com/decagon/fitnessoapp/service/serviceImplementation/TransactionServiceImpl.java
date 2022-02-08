package com.decagon.fitnessoapp.service.serviceImplementation;

import com.decagon.fitnessoapp.Email.API;
import com.decagon.fitnessoapp.dto.transactionDto.TransactionRequestDTO;
import com.decagon.fitnessoapp.dto.transactionDto.TransactionResponseDTO;
import com.decagon.fitnessoapp.dto.transactionDto.response.PaymentResponse;
import com.decagon.fitnessoapp.dto.transactionDto.response.TransVerificationResponse;
import com.decagon.fitnessoapp.service.TransactionService;
import lombok.AllArgsConstructor;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private final RestTemplate restTemplate;
    @Value("${website.address}")
    private String website;
    @Value("${server.port}")
    private Integer port;

    @Override
    public TransactionResponseDTO initializeTransaction(
           TransactionRequestDTO transactionRequestDTO){

        final String referenceGenerator = "fitnesso-app-" + RandomString.make(16);

        transactionRequestDTO.setReference(referenceGenerator);
        transactionRequestDTO.setCallback_url("http://" + website + ":" + port + "/transaction/success");
        transactionRequestDTO.setMetadata(Map.of("cancel_action", "http://" + website + ":" + port + "/transaction/fail",
                "card_number", 1234567));
        transactionRequestDTO.setChannels(List.of("CARD"));

        final String url = "https://api.paystack.co/transaction/initialize";
        HttpHeaders headers = new HttpHeaders();
        String key = API.API_KEY_PAYMENT;
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + key);
        HttpEntity<TransactionRequestDTO> entity =
                new HttpEntity<>(transactionRequestDTO,headers);
        ResponseEntity<TransactionResponseDTO> response =
                restTemplate.postForEntity(url, entity,TransactionResponseDTO.class);
        return response.getBody();
    }

    @Override
    public TransVerificationResponse confirmation(String reference) {
        final String url = "https://api.paystack.co/transaction/verify/" + reference;
        HttpHeaders headers = new HttpHeaders();
        String key = API.API_KEY_PAYMENT;
        headers.set("Authorization", "Bearer " + key);
        HttpEntity<TransVerificationResponse> entity = new HttpEntity<>(headers);
        ResponseEntity<TransVerificationResponse> response = restTemplate.exchange(url, HttpMethod.GET,entity,TransVerificationResponse.class);
        return response.getBody();
    }

    public PaymentResponse printStatus(TransVerificationResponse transaction){
        if(transaction.getData().getMessage().equals("success")){
            return PaymentResponse.builder().message("Payment was Successful").status("OK").build();
        }
        return PaymentResponse.builder().message("Transaction Failed").build();
    }

}
