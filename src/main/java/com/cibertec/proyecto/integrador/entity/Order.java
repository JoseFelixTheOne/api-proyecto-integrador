package com.cibertec.proyecto.integrador.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotNull(message = "El campo 'userid' es requerido.")
    private Integer userid ;
    @Positive(message = "El campo 'total' debe ser un número positivo.")
    private BigDecimal total;
    private LocalDateTime timestamp ;
    private Boolean active = true;
    private Boolean deleted = false;
    private String status = "SHOPPING CART";
    private String step = "step-0";

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @NotNull(message = "Debe haber al menos un 'orderDetail'.")
    private List<OrderDetailEntity> orderDetails;
}
