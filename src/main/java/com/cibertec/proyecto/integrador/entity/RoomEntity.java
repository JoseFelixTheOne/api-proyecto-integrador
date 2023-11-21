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
    private double price;
    private boolean active;
    private boolean deleted;
    private int hotelid;
    private String image;


}
