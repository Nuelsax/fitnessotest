package com.decagon.fitnessoapp.dto.transactionDto;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@Builder
public class InitializeTransactionRequestDTO {

    private String amount;
    private String email;
    private String reference;
    private String callback_url;
    private Map<?, ?> metadata;
    private List<String> channels;
    private Integer transaction_charge;
}
