package com.cibertec.proyecto.integrador.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
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
    private int roomid;
    @Column(name = "bookedtime")
    @Temporal(TemporalType.DATE)
    private Date bookedtime;

    @Column(name = "startedtime")
    @Temporal(TemporalType.DATE)
    private Date startedtime;

    @Column(name = "endtime")
    @Temporal(TemporalType.DATE)
    private Date endtime;
    private Boolean deleted;

    @ManyToOne
    @JoinColumn(name = "orderid", insertable = false, updatable = false)
    private Order order;

    @ManyToOne
    @JoinColumn(name = "roomid", insertable = false, updatable = false)
    private RoomEntity room;
}
