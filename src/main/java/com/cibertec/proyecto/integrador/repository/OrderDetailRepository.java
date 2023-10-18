package com.cibertec.proyecto.integrador.repository;

import com.cibertec.proyecto.integrador.entity.OrderDetailEntity;
import com.cibertec.proyecto.integrador.entity.OrderDetailId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderDetailRepository extends JpaRepository<OrderDetailEntity, OrderDetailId> {
}
