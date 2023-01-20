package com.vithal.electronic.store.services;

import org.springframework.data.domain.Pageable;

import com.vithal.electronic.store.dtos.CreateOrderRequest;
import com.vithal.electronic.store.dtos.OrderDto;
import com.vithal.electronic.store.dtos.PageableResponse;
import com.vithal.electronic.store.entities.Order;

import java.util.List;

public interface OrderService {

    //create order
    OrderDto createOrder(CreateOrderRequest orderDto);

    //remove order
    void removeOrder(String orderId);

    //get orders of user
    List<OrderDto> getOrdersOfUser(String userId);

    //get orders
    PageableResponse<OrderDto> getOrders(int pageNumber, int pageSize, String sortBy, String sortDir);

    //order methods(logic) related to order

}
