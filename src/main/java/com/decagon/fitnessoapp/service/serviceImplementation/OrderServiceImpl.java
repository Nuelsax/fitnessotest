package com.decagon.fitnessoapp.service.serviceImplementation;

import com.decagon.fitnessoapp.dto.OrderResponse;
import com.decagon.fitnessoapp.dto.UserProductDto;
import com.decagon.fitnessoapp.model.product.ORDER_STATUS;
import com.decagon.fitnessoapp.model.product.Order;
import com.decagon.fitnessoapp.model.user.Person;
import com.decagon.fitnessoapp.repository.OrderRepository;
import com.decagon.fitnessoapp.repository.PersonRepository;
import com.decagon.fitnessoapp.service.OrderService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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
    public ResponseEntity<OrderResponse> getOrder(Authentication authentication) {

        Person person = personRepository.findPersonByUserName(authentication.getName())
                .orElseThrow(()-> new UsernameNotFoundException("Check getOrder at OrderServiceImpl: User Name does not Exist"));
        Order order = orderRepository.findOrderByPerson_Id(person.getId())
                .orElseThrow(()-> new NullPointerException("Order does not Exist"));
        OrderResponse orderResponse = new OrderResponse();
        modelMapper.map(order, orderResponse);
        return ResponseEntity.ok().body(orderResponse);
    }

    @Override
    public Page<OrderResponse> getAllOrders(int pageNo) {
        int pageSize = 10;
        int skipCount = (pageNo - 1) * pageSize;

        List<OrderResponse> orderList = getOrderList()
                .stream()
                .skip(skipCount)
                .limit(pageSize)
                .collect(Collectors.toList());

        Pageable orderPage = PageRequest.of(pageNo, pageSize, Sort.by("productName").ascending());

        return new PageImpl<>(orderList, orderPage, orderList.size());
    }

    private List<OrderResponse> getOrderList() {
        List<Order> orderList = orderRepository.findAll();

        return orderList.stream()
                .map(x -> modelMapper.map(x, OrderResponse.class))
                .collect(Collectors.toList());
    }

    @Override
    public Page<OrderResponse> getOrdersByStatus(ORDER_STATUS status, int pageNo) {
        int pageSize = 10;
        int skipCount = (pageNo - 1) * pageSize;

        List<OrderResponse> orderList = getOrderListByStatus(status)
                .stream()
                .skip(skipCount)
                .limit(pageSize)
                .collect(Collectors.toList());

        Pageable orderPage = PageRequest.of(pageNo, pageSize, Sort.by("productName").ascending());

        return new PageImpl<>(orderList, orderPage, orderList.size());
    }

    private List<OrderResponse> getOrderListByStatus(ORDER_STATUS status) {
        List<Order> orderList = orderRepository.findAllByOrderStatus(status);

        return orderList.stream()
                .map(x -> modelMapper.map(x, OrderResponse.class))
                .collect(Collectors.toList());
    }
}
