package com.cibertec.proyecto.integrador.services;

import com.cibertec.proyecto.integrador.entity.Order;
import com.cibertec.proyecto.integrador.entity.OrderDetailEntity;
import com.cibertec.proyecto.integrador.entity.RoomEntity;
import com.cibertec.proyecto.integrador.repository.RoomRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import java.time.LocalDateTime;

@Service
public class OrderServiceImpl implements OrderService {
    private final JdbcTemplate jdbcTemplate;
    private final RoomRepository roomRepository;
    private int lastOrderDetailId = 0;

    @Autowired
    public OrderServiceImpl(JdbcTemplate jdbcTemplate, RoomRepository roomRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.roomRepository = roomRepository;
    }

    @Override
    @Transactional
    public Order createOrder(Order order, List<OrderDetailEntity> orderDetails) {
        LocalDateTime currentDateTime = LocalDateTime.now();
        order.setTimestamp(currentDateTime);

        jdbcTemplate.update("INSERT INTO orders (userId, total, timestamp, active, deleted, status, step) VALUES (?, ?, ?, ?, ?, ?, ?)",
                order.getUserid(), order.getTotal(), currentDateTime, order.getActive(), order.getDeleted(), order.getStatus(), order.getStep());

        String selectLastOrderIdSQL = "SELECT MAX(id) FROM orders";
        Integer orderId = jdbcTemplate.queryForObject(selectLastOrderIdSQL, Integer.class);
        order.setId(orderId);

        for (OrderDetailEntity detail : orderDetails) {
            int nextOrderDetailId = getNextOrderDetailId();
            detail.setId(nextOrderDetailId);
            detail.setOrderid(orderId != null ? orderId : 0);

            if (detail.getStartedtime() == null) {

            }
            if (detail.getEndtime() == null) {

            }

            jdbcTemplate.update("INSERT INTO order_detail (id, orderId, subtotal, price, days, roomId, bookedTime, startedTime, endTime) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)",
                    detail.getId(), orderId, detail.getSubtotal(), detail.getPrice(), detail.getDays(), detail.getRoomid(), detail.getBookedtime(), detail.getStartedtime(), detail.getEndtime());

            RoomEntity room = roomRepository.findById(detail.getRoomid()).orElse(null);
            if (room != null) {
                room.setActive(false);
                roomRepository.save(room);
            } else {

            }
        }

        return order;
    }

    private int getNextOrderDetailId() {
        lastOrderDetailId++;
        return lastOrderDetailId;
    }
}
