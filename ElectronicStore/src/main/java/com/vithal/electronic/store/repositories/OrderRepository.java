package com.vithal.electronic.store.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vithal.electronic.store.entities.Order;
import com.vithal.electronic.store.entities.User;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, String> {

    List<Order> findByUser(User user);

}
