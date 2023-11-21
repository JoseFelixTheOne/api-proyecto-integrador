package com.cibertec.proyecto.integrador.controller;

import com.cibertec.proyecto.integrador.entity.Order;
import com.cibertec.proyecto.integrador.entity.OrderDetailEntity;
import com.cibertec.proyecto.integrador.services.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/shopping-cart")
    public ResponseEntity<Order> createOrderWithDetails(@Valid @RequestBody Order order) {
        List<OrderDetailEntity> orderDetails = order.getOrderDetails();
        Order createdOrder = orderService.shoppingCart(order, orderDetails);
        return ResponseEntity.ok(createdOrder);
    }

    @GetMapping("/{userId}/shopping-cart")
    public ResponseEntity<Order> getLastShoppingCart(@PathVariable Integer userId) {
        Order lastShoppingCart = orderService.getLastShoppingCart(userId);
        return ResponseEntity.ok(lastShoppingCart);
    }

    @PutMapping("/shopping-cart/{orderId}/add-details")
    public ResponseEntity<Order> addToShoppingCart(@PathVariable Integer orderId, @Valid @RequestBody List<OrderDetailEntity> addedDetails) {
        Order order = new Order();
        order.setId(orderId);

        Order updatedOrder = orderService.addToShoppingCart(order, addedDetails);

        return ResponseEntity.ok(updatedOrder);
    }

    @PutMapping("/shopping-cart/{orderId}/remove-details")
    public ResponseEntity<Order> removeFromShoppingCart(@PathVariable Integer orderId, @RequestBody List<Integer> removedDetailIds) {
        Order order = new Order();
        order.setId(orderId);

        Order updatedOrder = orderService.removeFromShoppingCart(order, removedDetailIds);

        return ResponseEntity.ok(updatedOrder);
    }

    @PutMapping("/{orderId}/change-to-order")
    public ResponseEntity<Order> changeToOrder(@PathVariable Integer orderId) {
        Order order = orderService.changeToOrder(orderId);
        return ResponseEntity.ok(order);
    }
    @PutMapping("/{orderId}/add-details")
    public ResponseEntity<Order> addToOrder(@PathVariable Integer orderId, @RequestBody List<OrderDetailEntity> addedDetails) {
        Order order = new Order();
        order.setId(orderId);

        Order updatedOrder = orderService.addToOrder(order, addedDetails);

        return ResponseEntity.ok(updatedOrder);
    }
    @PutMapping("/{orderId}/remove-details")
    public ResponseEntity<Order> removeFromOrder(@PathVariable Integer orderId, @RequestBody List<Integer> removedDetailIds) {
        Order order = new Order();
        order.setId(orderId);

        Order updatedOrder = orderService.removeFromOrder(order, removedDetailIds);

        return ResponseEntity.ok(updatedOrder);
    }
    @PutMapping("/{orderId}/back-to-shopping-cart")
    public ResponseEntity<Order> backToShoppingCart(@PathVariable Integer orderId) {
        Order order = orderService.backToShoppingCart(orderId);
        return ResponseEntity.ok(order);
    }
    @PutMapping("/{orderId}/change-to-booked")
    public ResponseEntity<Order> changeToBooked(@PathVariable Integer orderId) {
        Order order = orderService.changeToBooked(orderId);
        return ResponseEntity.ok(order);
    }
}
