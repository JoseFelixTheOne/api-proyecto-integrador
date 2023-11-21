package com.cibertec.proyecto.integrador.repository;

import com.cibertec.proyecto.integrador.entity.RoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RoomRepository extends JpaRepository<RoomEntity, Integer> {

    public List<RoomEntity> searchByNameContains(String name);
    public List<RoomEntity> getRoomByFloor(int floor);
}
