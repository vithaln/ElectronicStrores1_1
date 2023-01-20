package com.vithal.electronic.store.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vithal.electronic.store.entities.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem,Integer> {
}
