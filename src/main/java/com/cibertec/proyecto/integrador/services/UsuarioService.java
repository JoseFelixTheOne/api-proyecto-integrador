package com.cibertec.proyecto.integrador.services;

import com.cibertec.proyecto.integrador.entity.Usuario;
import com.cibertec.proyecto.integrador.generic.Cifrado;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {


    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UsuarioService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate=jdbcTemplate;
    }

    public int insertarUsuario(Usuario usuario) {
        String contracifrada= Cifrado.cifrarCadena(usuario.getPassword());
        String consultaSql = "INSERT INTO users (username, email, password, document, role) VALUES (?, ?, ?, ?, ?)";
        int filasAfectadas= jdbcTemplate.update(consultaSql, usuario.getUsername(), usuario.getEmail(), contracifrada, usuario.getDocument(), usuario.getRole());
        return filasAfectadas;
    }



}
