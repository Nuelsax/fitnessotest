package com.decagon.fitnessoapp.service.serviceImplementation;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.decagon.fitnessoapp.model.product.ORDER_STATUS;
import com.decagon.fitnessoapp.model.product.Order;
import com.decagon.fitnessoapp.repository.OrderRepository;
import com.decagon.fitnessoapp.repository.PersonRepository;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {OrderServiceImpl.class})
@ExtendWith(SpringExtension.class)
class OrderServiceImplTest {
    @MockBean
    private ModelMapper modelMapper;

    @MockBean
    private OrderRepository orderRepository;

    @Autowired
    private OrderServiceImpl orderServiceImpl;

    @MockBean
    private PersonRepository personRepository;

    @Test
    void testGetAllOrders() {
        when(this.orderRepository.findAll()).thenReturn(new ArrayList<>());
        assertTrue(this.orderServiceImpl.getAllOrders(1).toList().isEmpty());
        verify(this.orderRepository).findAll();
    }

    @Test
    void testGetOrdersByStatus() {
        when(this.orderRepository.findAllByOrderStatus((ORDER_STATUS) any())).thenReturn(new ArrayList<>());
        assertTrue(this.orderServiceImpl.getOrdersByStatus(ORDER_STATUS.PENDING, 1).toList().isEmpty());
        verify(this.orderRepository).findAllByOrderStatus((ORDER_STATUS) any());
    }
}

