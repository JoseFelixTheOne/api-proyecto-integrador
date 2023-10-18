package com.cibertec.proyecto.integrador.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public LoginService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate=jdbcTemplate;
    }

    public int login() {
        String consultaSql = "SELECT COUNT(*) FROM tu_tabla";
        int contador = jdbcTemplate.queryForObject(consultaSql, Integer.class);
        return contador;
    }
}
