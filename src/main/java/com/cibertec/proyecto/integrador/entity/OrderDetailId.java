package com.cibertec.proyecto.integrador.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailId implements Serializable {
    private Integer orderid;
    private Integer id;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrderDetailId that)) return false;
        return Objects.equals(orderid, that.orderid) && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderid, id);
    }
}
