package com.vithal.electronic.store.dtos;

import lombok.*;

import javax.persistence.*;

import com.vithal.electronic.store.entities.Cart;
import com.vithal.electronic.store.entities.CartItem;
import com.vithal.electronic.store.entities.Product;
import com.vithal.electronic.store.entities.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartItemDto {

    private int cartItemId;
    private ProductDto product;
    private int quantity;
    private int totalPrice;

}
