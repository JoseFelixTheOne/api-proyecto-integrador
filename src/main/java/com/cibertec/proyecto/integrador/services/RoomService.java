package com.cibertec.proyecto.integrador.services;

import com.cibertec.proyecto.integrador.entity.RoomEntity;

import java.util.List;

public interface RoomService {
    public List<RoomEntity> listarRooms();
    public RoomEntity getRoomToId(int id);
    public List<RoomEntity> getRoomToName(String name);
    public List<RoomEntity> getRoomToFloor(int floor);

}
