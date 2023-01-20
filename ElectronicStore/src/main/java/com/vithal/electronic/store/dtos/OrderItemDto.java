package com.vithal.electronic.store.dtos;

import lombok.*;

import javax.persistence.*;

import com.vithal.electronic.store.entities.Order;
import com.vithal.electronic.store.entities.Product;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OrderItemDto {


    private int orderItemId;

    private int quantity;

    private int totalPrice;

    private ProductDto product;


}
