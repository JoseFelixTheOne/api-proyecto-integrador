package com.cibertec.proyecto.integrador.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "order_detail")
@IdClass(OrderDetailId.class)
@Getter
@Setter
public class OrderDetailEntity {
    @Id
    @Column(name = "id")
    private int id;
    @Id
    private int orderid;
    private BigDecimal subtotal;
    private BigDecimal price;
    private double days;
    @NotNull(message = "El campor 'roomid' no puede estar vacío")
    private Integer roomid;
    @Column(name = "bookedtime")
    @Temporal(TemporalType.DATE)
    private LocalDate bookedtime = LocalDate.now();

    @Column(name = "startedtime")
    @Temporal(TemporalType.DATE)
    @FutureOrPresent(message = "'startedtime' debe ser una fecha mayor o igual a la actual")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate startedtime;

    @Column(name = "endtime")
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate endtime;
    private Boolean deleted = false;

    @ManyToOne
    @JoinColumn(name = "orderid", insertable = false, updatable = false)
    private Order order;

    @ManyToOne
    @JoinColumn(name = "roomid", insertable = false, updatable = false)
    private RoomEntity room;

    @AssertTrue(message = "La fecha 'endtime' debe ser mayor a 'startedtime'")
    public boolean isValidEndtime() {
        return endtime == null || endtime.isAfter(startedtime);
    }
}
