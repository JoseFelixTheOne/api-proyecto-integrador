package com.cibertec.proyecto.integrador.services;

import com.cibertec.proyecto.integrador.entity.Order;
import com.cibertec.proyecto.integrador.entity.OrderDetailEntity;
import com.cibertec.proyecto.integrador.repository.OrderDetailRepository;
import com.cibertec.proyecto.integrador.repository.OrderRepository;
import com.cibertec.proyecto.integrador.repository.RoomRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {
    private OrderRepository orderRepository;

    private OrderDetailRepository orderDetailRepository;
    private final JdbcTemplate jdbcTemplate;
    private final RoomRepository roomRepository;
    private int lastOrderDetailId = 0;

    @Autowired
    public OrderServiceImpl(JdbcTemplate jdbcTemplate, RoomRepository roomRepository, OrderRepository orderRepository,
                            OrderDetailRepository orderDetailRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.roomRepository = roomRepository;
        this.orderRepository = orderRepository;
        this.orderDetailRepository = orderDetailRepository;
    }

    @Override
    @Transactional
    public Order shoppingCart(Order order, List<OrderDetailEntity> orderDetails) {
        try {
            LocalDateTime currentDateTime = LocalDateTime.now();
            order.setTimestamp(currentDateTime);
            order.setStatus("SHOPPING CART");

            if (order.getUserid() == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El campo 'userId' es requerido.");
            }
            if (order.getTotal() == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El campo 'total' es requerido.");
            }

            jdbcTemplate.update("INSERT INTO orders (userId, total, timestamp, active, deleted, status, step) VALUES (?, ?, ?, ?, ?, ?, ?)",
                    order.getUserid(), order.getTotal(), currentDateTime, order.getActive(), order.getDeleted(), order.getStatus(), order.getStep());

            String selectLastOrderIdSQL = "SELECT MAX(id) FROM orders WHERE userId = ?";
            Integer orderId = jdbcTemplate.queryForObject(selectLastOrderIdSQL, Integer.class, order.getUserid());
            order.setId(orderId);

            for (OrderDetailEntity detail : orderDetails) {
                int nextOrderDetailId = getNextOrderDetailId();
                detail.setId(nextOrderDetailId);
                detail.setOrderid(order.getId());
                detail.setDeleted(false);

                if (detail.getStartedtime() == null) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El campo 'startedtime' es requerido.");
                }
                if (detail.getEndtime() == null) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El campo 'endtime' es requerido.");
                }

                jdbcTemplate.update("INSERT INTO order_detail (id, orderId, subtotal, price, days, roomId, bookedTime, startedTime, endTime, deleted) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                        detail.getId(), detail.getOrderid(), detail.getSubtotal(), detail.getPrice(), detail.getDays(), detail.getRoomid(), detail.getBookedtime(), detail.getStartedtime(), detail.getEndtime(), detail.getDeleted());
            }

            return order;
        } catch (Exception e) {
            System.err.println("Error " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Se produjo un error al procesar la solicitud.");
        }
    }
    @Override
    public Order getLastShoppingCart(Integer userId) {
        String selectLastOrderSQL =
                "SELECT id, userId, total, timestamp, active, deleted, status, step " +
                        "FROM orders " +
                        "WHERE userId = ? AND status = 'SHOPPING CART' " +
                        "ORDER BY id DESC LIMIT 1";

        Order order = jdbcTemplate.queryForObject(selectLastOrderSQL, new Object[]{userId}, (rs, rowNum) -> {
            Order orderC = new Order();
            orderC.setId(rs.getInt("id"));
            orderC.setUserid(rs.getInt("userId"));
            orderC.setTotal(rs.getBigDecimal("total"));
            orderC.setTimestamp(rs.getTimestamp("timestamp").toLocalDateTime());
            orderC.setActive(rs.getBoolean("active"));
            orderC.setDeleted(rs.getBoolean("deleted"));
            orderC.setStatus(rs.getString("status"));
            orderC.setStep(rs.getString("step"));
            return orderC;
        });

        if (order != null) {
            List<OrderDetailEntity> orderDetails = getOrderDetailsByOrderId(order.getId());
            order.setOrderDetails(orderDetails);
        }

        return order;
    }
    @Override
    @Transactional
    public Order addToShoppingCart(Order order, List<OrderDetailEntity> addedDetails) {
        try {
            Order existingOrder = getOrderById(order.getId());

            if (existingOrder == null || !existingOrder.getStatus().equals("SHOPPING CART") || !existingOrder.getActive() || existingOrder.getDeleted()) {
                throw new IllegalArgumentException("La orden no existe, no está en estado 'SHOPPING CART', o está inactiva o eliminada.");
            }

            int maxDetailId = getMaxOrderDetailId(existingOrder.getId());

            for (OrderDetailEntity newDetail : addedDetails) {
                maxDetailId++;
                newDetail.setId(maxDetailId);
                newDetail.setOrderid(existingOrder.getId());
                newDetail.setDeleted(false);

                if (newDetail.getStartedtime() == null || newDetail.getEndtime() == null) {
                    throw new IllegalArgumentException("Los campos 'startedtime' y 'endtime' son requeridos para los nuevos detalles.");
                }

                jdbcTemplate.update("INSERT INTO order_detail (id, orderId, subtotal, price, days, roomId, bookedTime, startedTime, endTime, deleted) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                        newDetail.getId(), newDetail.getOrderid(), newDetail.getSubtotal(), newDetail.getPrice(), newDetail.getDays(),
                        newDetail.getRoomid(), newDetail.getBookedtime(), newDetail.getStartedtime(), newDetail.getEndtime(), newDetail.getDeleted());
            }

            List<OrderDetailEntity> existingDetails = getOrderDetailsByOrderId(existingOrder.getId());
            existingOrder.setOrderDetails(existingDetails);

            BigDecimal newTotal = calculateTotal(existingDetails);
            existingOrder.setTotal(newTotal);

            jdbcTemplate.update("UPDATE orders SET total = ? WHERE id = ?", existingOrder.getTotal(), existingOrder.getId());

            return existingOrder;
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            return null;
        }
    }
    @Override
    @Transactional
    public Order removeFromShoppingCart(Order order, List<Integer> removedDetailIds) {
        try {
            Order existingOrder = getOrderById(order.getId());

            if (existingOrder == null || !existingOrder.getStatus().equals("SHOPPING CART") || !existingOrder.getActive() || existingOrder.getDeleted()) {
                throw new IllegalArgumentException("La orden no existe, no está en estado 'SHOPPING CART', o está inactiva o eliminada.");
            }

            for (Integer removedDetailId : removedDetailIds) {
                jdbcTemplate.update("DELETE FROM order_detail WHERE orderId = ? AND id = ?", existingOrder.getId(), removedDetailId);
            }

            List<OrderDetailEntity> existingDetails = getOrderDetailsByOrderId(existingOrder.getId());
            existingOrder.setOrderDetails(existingDetails);

            BigDecimal newTotal = calculateTotal(existingDetails);
            existingOrder.setTotal(newTotal);

            jdbcTemplate.update("UPDATE orders SET total = ? WHERE id = ?", existingOrder.getTotal(), existingOrder.getId());

            return existingOrder;
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            return null;
        }
    }

    @Override
    @Transactional
    public Order changeToOrder(Integer orderId) {
        try {
            if (orderId == null){
                throw new IllegalArgumentException("La id de la orden es nulo");
            }
            Order existingOrder = getOrderById(orderId);

            if (existingOrder == null || !existingOrder.getStatus().equals("SHOPPING CART") || !existingOrder.getActive() || existingOrder.getDeleted()) {
                throw new IllegalArgumentException("La orden no existe, no está en estado 'SHOPPING CART', o está inactiva o eliminada.");
            }

            existingOrder.setStatus("ORDER");

            jdbcTemplate.update("UPDATE orders SET status = ? WHERE id = ?", existingOrder.getStatus(), existingOrder.getId());

            return existingOrder;
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            return null;
        }
    }

    @Override
    @Transactional
    public Order addToOrder(Order order, List<OrderDetailEntity> addedDetails) {
        try {
            Order existingOrder = getOrderById(order.getId());

            if (existingOrder == null || !existingOrder.getStatus().equals("ORDER") || !existingOrder.getActive() || existingOrder.getDeleted()) {
                throw new IllegalArgumentException("La orden no existe, no está en estado 'ORDER', o está inactiva o eliminada.");
            }

            int maxDetailId = getMaxOrderDetailId(existingOrder.getId());

            for (OrderDetailEntity newDetail : addedDetails) {
                maxDetailId++;
                newDetail.setId(maxDetailId);
                newDetail.setOrderid(existingOrder.getId());
                newDetail.setDeleted(false);

                if (newDetail.getStartedtime() == null || newDetail.getEndtime() == null) {
                    throw new IllegalArgumentException("Los campos 'startedtime' y 'endtime' son requeridos para los nuevos detalles.");
                }

                jdbcTemplate.update("INSERT INTO order_detail (id, orderId, subtotal, price, days, roomId, bookedTime, startedTime, endTime, deleted) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                        newDetail.getId(), newDetail.getOrderid(), newDetail.getSubtotal(), newDetail.getPrice(), newDetail.getDays(),
                        newDetail.getRoomid(), newDetail.getBookedtime(), newDetail.getStartedtime(), newDetail.getEndtime(), newDetail.getDeleted());
            }

            List<OrderDetailEntity> existingDetails = getOrderDetailsByOrderId(existingOrder.getId());
            existingOrder.setOrderDetails(existingDetails);

            BigDecimal newTotal = calculateTotal(existingDetails);
            existingOrder.setTotal(newTotal);

            jdbcTemplate.update("UPDATE orders SET total = ? WHERE id = ?", existingOrder.getTotal(), existingOrder.getId());

            return existingOrder;
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            return null;
        }
    }
    @Override
    @Transactional
    public Order removeFromOrder(Order order, List<Integer> removedDetailIds) {
        try {
            Order existingOrder = getOrderById(order.getId());

            if (existingOrder == null || !existingOrder.getStatus().equals("ORDER") || !existingOrder.getActive() || existingOrder.getDeleted()) {
                throw new IllegalArgumentException("La orden no existe, no está en estado 'ORDER', o está inactiva o eliminada.");
            }

            // Quitar detalles
            for (Integer removedDetailId : removedDetailIds) {
                jdbcTemplate.update("UPDATE order_detail SET deleted = true WHERE orderId = ? AND id = ?",existingOrder.getId(), removedDetailId);
            }

            List<OrderDetailEntity> existingDetails = getDetailsNotDeletedByOrderId(existingOrder.getId());
            existingOrder.setOrderDetails(existingDetails);

            BigDecimal newTotal = calculateTotal(existingDetails);
            existingOrder.setTotal(newTotal);

            jdbcTemplate.update("UPDATE orders SET total = ? WHERE id = ?", existingOrder.getTotal(), existingOrder.getId());

            return existingOrder;
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            return null;
        }
    }
    @Override
    @Transactional
    public Order backToShoppingCart(Integer orderId) {
        try {
            if (orderId == null) {
                throw new IllegalArgumentException("La id de la orden es nulo");
            }

            Order existingOrder = getOrderById(orderId);

            if (existingOrder == null || !existingOrder.getStatus().equals("ORDER") || !existingOrder.getActive() || existingOrder.getDeleted()) {
                throw new IllegalArgumentException("La orden no existe, no está en estado 'ORDER', o está inactiva o eliminada.");
            }

            existingOrder.setStatus("SHOPPING CART");

            jdbcTemplate.update("UPDATE order_detail SET deleted = false WHERE orderId = ? AND deleted = true", existingOrder.getId());

            List<OrderDetailEntity> existingDetails = getOrderDetailsByOrderId(existingOrder.getId());
            existingOrder.setOrderDetails(existingDetails);

            BigDecimal newTotal = calculateTotal(existingDetails);
            existingOrder.setTotal(newTotal);

            jdbcTemplate.update("UPDATE orders SET status = ?, total = ? WHERE id = ?", existingOrder.getStatus(), existingOrder.getTotal(), existingOrder.getId());

            return existingOrder;
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            return null;
        }
    }

    private Order getOrderById(Integer orderId) {
        String selectOrderByIdSQL =
                "SELECT id, userId, total, timestamp, active, deleted, status, step " +
                        "FROM orders " +
                        "WHERE id = ?";

        try {
            Order order = jdbcTemplate.queryForObject(selectOrderByIdSQL, new Object[]{orderId}, (rs, rowNum) -> {
                Order orderC = new Order();
                orderC.setId(rs.getInt("id"));
                orderC.setUserid(rs.getInt("userId"));
                orderC.setTotal(rs.getBigDecimal("total"));
                orderC.setTimestamp(rs.getTimestamp("timestamp").toLocalDateTime());
                orderC.setActive(rs.getBoolean("active"));
                orderC.setDeleted(rs.getBoolean("deleted"));
                orderC.setStatus(rs.getString("status"));
                orderC.setStep(rs.getString("step"));
                return orderC;
            });

            if (order != null) {
                List<OrderDetailEntity> orderDetails = getDetailsNotDeletedByOrderId(orderId);
                order.setOrderDetails(orderDetails);
            }

            return order;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    private List<OrderDetailEntity> getOrderDetailsByOrderId(Integer orderId) {
        String selectOrderDetailsSQL =
                "SELECT id, subtotal, price, days, roomId, bookedTime, startedTime, endTime, deleted " +
                        "FROM order_detail " +
                        "WHERE orderId = ?";

        List<OrderDetailEntity> orderDetails = jdbcTemplate.query(selectOrderDetailsSQL, new Object[]{orderId}, (rs, rowNum) -> {
            OrderDetailEntity detail = new OrderDetailEntity();
            detail.setId(rs.getInt("id"));
            detail.setSubtotal(rs.getBigDecimal("subtotal"));
            detail.setPrice(rs.getBigDecimal("price"));
            detail.setDays(rs.getDouble("days"));
            detail.setRoomid(rs.getInt("roomId"));
            detail.setBookedtime(rs.getTimestamp("bookedTime"));
            detail.setStartedtime(rs.getTimestamp("startedTime") != null ? rs.getTimestamp("startedTime") : null);
            detail.setEndtime(rs.getTimestamp("endTime") != null ? rs.getTimestamp("endTime") : null);
            detail.setDeleted(rs.getBoolean("deleted"));
            detail.setOrderid(orderId);
            return detail;
        });

        return orderDetails;
    }
    private List<OrderDetailEntity> getDetailsNotDeletedByOrderId(Integer orderId) {
        String selectOrderDetailsSQL =
                "SELECT id, subtotal, price, days, roomId, bookedTime, startedTime, endTime, deleted " +
                        "FROM order_detail " +
                        "WHERE orderId = ? AND deleted = false";

        List<OrderDetailEntity> orderDetails = jdbcTemplate.query(selectOrderDetailsSQL, new Object[]{orderId}, (rs, rowNum) -> {
            OrderDetailEntity detail = new OrderDetailEntity();
            detail.setId(rs.getInt("id"));
            detail.setSubtotal(rs.getBigDecimal("subtotal"));
            detail.setPrice(rs.getBigDecimal("price"));
            detail.setDays(rs.getDouble("days"));
            detail.setRoomid(rs.getInt("roomId"));
            detail.setBookedtime(rs.getTimestamp("bookedTime"));
            detail.setStartedtime(rs.getTimestamp("startedTime") != null ? rs.getTimestamp("startedTime") : null);
            detail.setEndtime(rs.getTimestamp("endTime") != null ? rs.getTimestamp("endTime") : null);
            detail.setDeleted(rs.getBoolean("deleted"));
            detail.setOrderid(orderId);
            return detail;
        });

        return orderDetails;
    }

    private int getMaxOrderDetailId(Integer orderId) {
        String selectMaxIdSQL = "SELECT MAX(id) FROM order_detail WHERE orderId = ?";
        Integer maxId = jdbcTemplate.queryForObject(selectMaxIdSQL, new Object[]{orderId}, Integer.class);

        if (maxId != null) {
            return maxId + 1;
        } else {
            return 1;
        }
    }

    private BigDecimal calculateTotal(List<OrderDetailEntity> orderDetails) {
        BigDecimal total = BigDecimal.ZERO;
        for (OrderDetailEntity detail : orderDetails) {
            total = total.add(detail.getSubtotal());
        }
        return total;
    }
    private int getNextOrderDetailId() {
        lastOrderDetailId++;
        return lastOrderDetailId;
    }
}
