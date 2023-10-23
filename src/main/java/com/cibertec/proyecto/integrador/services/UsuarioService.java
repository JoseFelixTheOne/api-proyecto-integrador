package com.cibertec.proyecto.integrador.services;

import com.cibertec.proyecto.integrador.entity.Usuario;
import com.cibertec.proyecto.integrador.generic.Cifrado;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UsuarioService {


    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UsuarioService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate=jdbcTemplate;
    }

    public int insertarUsuario(Usuario usuario) {

        String emailMinusculas = usuario.getEmail().trim().toLowerCase();
        String usernameMinusculas = usuario.getUsername().trim().toLowerCase();

        String regex = "^[A-Za-z0-9_.]+@[A-Za-z0-9-]+(\\.[A-Za-z]{2,})+$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(emailMinusculas);

        if (!matcher.matches()) {
            return -3;
        }

        String consultaCorreoExistente = "SELECT COUNT(*) FROM users WHERE LOWER(email) = ?";
        int count = jdbcTemplate.queryForObject(consultaCorreoExistente, Integer.class, emailMinusculas);

        if (count > 0) {
            return -1;
        }

        String consultaUsernameExistente = "SELECT COUNT(*) FROM users WHERE LOWER(username) = ?";
        int countUsername = jdbcTemplate.queryForObject(consultaUsernameExistente, Integer.class, usernameMinusculas);

        if (countUsername > 0) {
            return -2;
        }

        String contracifrada= Cifrado.cifrarCadena(usuario.getPassword());
        String consultaSql = "INSERT INTO users (username, email, password, document, role) VALUES (?, ?, ?, ?, ?)";
        int filasAfectadas= jdbcTemplate.update(consultaSql, usuario.getUsername(), usuario.getEmail(), contracifrada, usuario.getDocument(), usuario.getRole());
        return filasAfectadas;
    }





}
