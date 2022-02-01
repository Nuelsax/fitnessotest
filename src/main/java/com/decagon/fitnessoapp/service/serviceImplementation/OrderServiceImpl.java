package com.decagon.fitnessoapp.service.serviceImplementation;

import com.decagon.fitnessoapp.dto.OrderResponse;
import com.decagon.fitnessoapp.model.product.Order;
import com.decagon.fitnessoapp.model.user.Person;
import com.decagon.fitnessoapp.repository.OrderRepository;
import com.decagon.fitnessoapp.repository.PersonRepository;
import com.decagon.fitnessoapp.service.OrderService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final PersonRepository personRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, PersonRepository personRepository,
                            ModelMapper modelMapper) {
        this.orderRepository = orderRepository;
        this.personRepository = personRepository;
        this.modelMapper = modelMapper;
    }


    @Override
    public ResponseEntity<?> getOrder(Authentication authentication) {

        Person person = personRepository.findPersonByUserName(authentication.getName())
                .orElseThrow(()-> new UsernameNotFoundException("Check getOrder at OrderServiceImpl: User Name does not Exist"));
        Order order = orderRepository.findOrderByPerson_Id(person.getId())
                .orElseThrow(()-> new NullPointerException("Order does not Exist"));
        OrderResponse orderResponse = new OrderResponse();
        modelMapper.map(order, orderResponse);
        return ResponseEntity.ok().body(orderResponse);
    }
}
