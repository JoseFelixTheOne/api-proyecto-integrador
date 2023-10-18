package com.cibertec.proyecto.integrador.repository;

import com.cibertec.proyecto.integrador.entity.RoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<RoomEntity, Integer> {
}
