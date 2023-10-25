package com.cibertec.proyecto.integrador.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Entity
@Table(name = "rooms")
@Getter
@Setter
public class RoomEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    private String description;
    private int floor;
    private boolean active = true;
    private boolean deleted = false;

    @OneToMany(mappedBy = "room", fetch = FetchType.LAZY)
    private List<OrderDetailEntity> orderDetails;

}

