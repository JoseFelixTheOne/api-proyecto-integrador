package com.cibertec.proyecto.integrador.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
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
    @NotNull(message = "El campo 'days' no puede estar vacío")
    private Double days;
    @NotNull(message = "El campor 'roomid' no puede estar vacío")
    private Integer roomid;
    @Column(name = "bookedtime")
    private LocalDate bookedtime = LocalDate.now();

    @Column(name = "startedtime")
    @FutureOrPresent(message = "'startedtime' debe ser una fecha mayor o igual a la actual")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @NotNull(message = "El campo 'startedtime' no puede estar vacío")
    private LocalDate startedtime;

    @Column(name = "endtime")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Future(message = "el campo 'endtime' debe ser una fecha mayor a la actual")
    private LocalDate endtime;
    private Boolean deleted = false;

    @ManyToOne
    @JoinColumn(name = "orderid", insertable = false, updatable = false)
    private Order order;

    @ManyToOne
    @JoinColumn(name = "roomid", insertable = false, updatable = false)
    private RoomEntity room;

    @AssertTrue(message = "La fecha 'endtime' debe ser mayor que 'startedtime'")
    public boolean isValidEndtime() {
        return !endtime.isAfter(startedtime);
    }

}
