package com.cibertec.proyecto.integrador.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
public class Order {
    private Integer id;
    private Integer userid ;
    private BigDecimal total;
    private LocalDateTime timestamp ;
    private Boolean active ;
    private Boolean deleted ;
    private String status ;
    private String step;
}
