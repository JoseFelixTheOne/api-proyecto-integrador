package com.cibertec.proyecto.integrador.controller;

import com.cibertec.proyecto.integrador.entity.Order;
import com.cibertec.proyecto.integrador.entity.OrderDetailEntity;
import com.cibertec.proyecto.integrador.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<?> createOrderWithDetails(@RequestBody Order order) {
        List<OrderDetailEntity> orderDetails = order.getOrderDetails();

        if (orderDetails == null || orderDetails.isEmpty()) {
            return ResponseEntity.badRequest().body("La orden debe tener al menos un detalle.");
        }

        Order createdOrder = orderService.shoppingCart(order, orderDetails);

        return ResponseEntity.ok(createdOrder);
    }

    @GetMapping("/shopping-cart/{userId}")
    public ResponseEntity<?> getLastShoppingCart(@PathVariable Integer userId) {
        Order lastShoppingCart = orderService.getLastShoppingCart(userId);

        if (lastShoppingCart == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(lastShoppingCart);
    }

    @PutMapping("/shopping-cart/add-details/{orderId}")
    public ResponseEntity<?> addToShoppingCart(@PathVariable Integer orderId, @RequestBody List<OrderDetailEntity> addedDetails) {
        Order order = new Order();
        order.setId(orderId);

        Order updatedOrder = orderService.addToShoppingCart(order, addedDetails);

        if (updatedOrder == null) {
            return ResponseEntity.badRequest().body("No se pudo agregar los detalles a la orden de compra.");
        }

        return ResponseEntity.ok(updatedOrder);
    }

    @PutMapping("/shopping-cart/remove-details/{orderId}")
    public ResponseEntity<?> removeFromShoppingCart(@PathVariable Integer orderId, @RequestBody List<Integer> removedDetailIds) {
        Order order = new Order();
        order.setId(orderId);

        Order updatedOrder = orderService.removeFromShoppingCart(order, removedDetailIds);

        if (updatedOrder == null) {
            return ResponseEntity.badRequest().body("No se pudieron eliminar los detalles de la orden de compra.");
        }

        return ResponseEntity.ok(updatedOrder);
    }
}
