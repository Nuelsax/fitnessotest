package com.decagon.fitnessoapp.dto.transactionDto.response;

import com.decagon.fitnessoapp.model.product.TestCart;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TestCartResponse {
    private List<TestCart> cartList;
    private TestCart testCart;
    private String message;
}
