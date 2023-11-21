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
    public ResponseEntity<?> addToShoppingCart(@PathVariable Integer orderId, @RequestBody List<OrderDetailEntity> addedDetails) {
        Order order = new Order();
        order.setId(orderId);

        Order updatedOrder = orderService.addToShoppingCart(order, addedDetails);

        if (updatedOrder == null) {
            return ResponseEntity.badRequest().body("No se pudo agregar los detalles a la orden de compra.");
        }

        return ResponseEntity.ok(updatedOrder);
    }

    @PutMapping("/shopping-cart/{orderId}/remove-details")
    public ResponseEntity<?> removeFromShoppingCart(@PathVariable Integer orderId, @RequestBody List<Integer> removedDetailIds) {
        Order order = new Order();
        order.setId(orderId);

        Order updatedOrder = orderService.removeFromShoppingCart(order, removedDetailIds);

        if (updatedOrder == null) {
            return ResponseEntity.badRequest().body("No se pudieron eliminar los detalles de la orden de compra.");
        }

        return ResponseEntity.ok(updatedOrder);
    }

    @PutMapping("/{orderId}/change-to-order")
    public ResponseEntity<?> changeToOrder(@PathVariable Integer orderId) {
        Order order = orderService.changeToOrder(orderId);
        if (order == null) {
            return ResponseEntity.badRequest().body("No se pudo actualizar el estado de la orden de compra.");
        }
        return ResponseEntity.ok(order);
    }
    @PutMapping("/{orderId}/add-details")
    public ResponseEntity<?> addToOrder(@PathVariable Integer orderId, @RequestBody List<OrderDetailEntity> addedDetails) {
        Order order = new Order();
        order.setId(orderId);

        Order updatedOrder = orderService.addToOrder(order, addedDetails);

        if (updatedOrder == null) {
            return ResponseEntity.badRequest().body("No se pudo agregar los detalles a la orden de compra.");
        }

        return ResponseEntity.ok(updatedOrder);
    }
    @PutMapping("/{orderId}/remove-details")
    public ResponseEntity<?> removeFromOrder(@PathVariable Integer orderId, @RequestBody List<Integer> removedDetailIds) {
        Order order = new Order();
        order.setId(orderId);

        Order updatedOrder = orderService.removeFromOrder(order, removedDetailIds);

        if (updatedOrder == null) {
            return ResponseEntity.badRequest().body("No se pudieron eliminar los detalles de la orden de compra.");
        }

        return ResponseEntity.ok(updatedOrder);
    }
    @PutMapping("/{orderId}/back-to-shopping-cart")
    public ResponseEntity<?> backToShoppingCart(@PathVariable Integer orderId) {
        Order order = orderService.backToShoppingCart(orderId);
        if (order == null) {
            return ResponseEntity.badRequest().body("No se pudo actualizar el estado de la orden de compra.");
        }
        return ResponseEntity.ok(order);
    }
    @PutMapping("/{orderId}/change-to-booked")
    public ResponseEntity<?> changeToBooked(@PathVariable Integer orderId) {
        try {
            Order order = orderService.changeToBooked(orderId);
            if (order == null) {
                return ResponseEntity.badRequest().body("No se pudo actualizar el estado de la orden de compra.");
            }
            return ResponseEntity.ok(order);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
