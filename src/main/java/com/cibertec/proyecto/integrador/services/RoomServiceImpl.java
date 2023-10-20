package com.cibertec.proyecto.integrador.services;

import com.cibertec.proyecto.integrador.entity.RoomEntity;
import com.cibertec.proyecto.integrador.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomServiceImpl implements RoomService {

    @Autowired
    private RoomRepository roomRepository;

    @Override
    public List<RoomEntity> listarRooms() {
        return roomRepository.findAll();
    }

    @Override
    public RoomEntity getRoomToId(int id) {
        return roomRepository.findById(id).orElse(null);
    }

    @Override
    public List<RoomEntity> getRoomToName(String name) {
        return roomRepository.getRoomByName(name);
    }

    @Override
    public List<RoomEntity> getRoomToFloor(int floor) {
        return roomRepository.getRoomByFloor(floor);
    }
}
