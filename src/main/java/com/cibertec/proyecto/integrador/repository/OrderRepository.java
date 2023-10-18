package com.cibertec.proyecto.integrador.repository;

import com.cibertec.proyecto.integrador.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Integer> {
}
