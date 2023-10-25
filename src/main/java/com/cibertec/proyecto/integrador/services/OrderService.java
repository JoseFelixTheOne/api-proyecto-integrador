package com.cibertec.proyecto.integrador.services;

import com.cibertec.proyecto.integrador.entity.Order;
import com.cibertec.proyecto.integrador.entity.OrderDetailEntity;

import java.util.List;

public interface OrderService {
    Order shoppingCart(Order order, List<OrderDetailEntity> orderDetails);
    Order getLastShoppingCart(Integer userId);
    Order addToShoppingCart(Order order, List<OrderDetailEntity> addedDetails);
    Order removeFromShoppingCart(Order order, List<Integer> removedDetailIds);
    Order changeToOrder(Integer orderId);
    Order addToOrder(Order order, List<OrderDetailEntity> addedDetails);
    Order removeFromOrder(Order order, List<Integer> removedDetailIds);
    Order backToShoppingCart(Integer orderId);
}

