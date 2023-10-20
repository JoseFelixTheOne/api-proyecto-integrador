package com.cibertec.proyecto.integrador.services;

import com.cibertec.proyecto.integrador.entity.Order;
import com.cibertec.proyecto.integrador.entity.OrderDetailEntity;

import java.util.List;

public interface OrderService {
    Order shoppingCart(Order order, List<OrderDetailEntity> orderDetails);
    Order getLastShoppingCart(Integer userId);
}

