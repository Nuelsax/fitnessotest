package com.decagon.fitnessoapp.service.serviceImplementation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.decagon.fitnessoapp.dto.ProductResponseDto;
import com.decagon.fitnessoapp.repository.IntangibleProductRepository;
import com.decagon.fitnessoapp.repository.TangibleProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {ProductServiceImpl.class})
@ExtendWith(SpringExtension.class)
class ProductServiceImplTest {
    @MockBean
    private IntangibleProductRepository intangibleProductRepository;

    @MockBean
    private ModelMapper modelMapper;

    @Autowired
    private ProductServiceImpl productServiceImpl;

    @MockBean
    private TangibleProductRepository tangibleProductRepository;

    @Test
    void testViewProductDetails() {
        ResponseEntity<ProductResponseDto> actualViewProductDetailsResult = this.productServiceImpl.viewProductDetails(123L,
                "Product Type");
        assertNull(actualViewProductDetailsResult.getBody());
        assertEquals("<400 BAD_REQUEST Bad Request,[]>", actualViewProductDetailsResult.toString());
        assertEquals(HttpStatus.BAD_REQUEST, actualViewProductDetailsResult.getStatusCode());
        assertTrue(actualViewProductDetailsResult.getHeaders().isEmpty());
    }
}

