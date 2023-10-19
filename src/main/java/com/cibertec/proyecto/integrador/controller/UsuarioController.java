package com.cibertec.proyecto.integrador.controller;

import com.cibertec.proyecto.integrador.entity.Usuario;
import com.cibertec.proyecto.integrador.services.LoginService;
import com.cibertec.proyecto.integrador.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class UsuarioController {

    private final UsuarioService servicio;
    @Autowired
    public UsuarioController(UsuarioService servicio ) {
        this.servicio=servicio;
    }

    @PostMapping("/usuario")
    @PreAuthorize("hasRole('ADMIN')") // Requiere el rol 'ADMIN' para acceder a este endpoint
    public ResponseEntity<String> crearUsuario(@RequestBody Usuario usuario) {
        int filasAfectadas = servicio.insertarUsuario(usuario);
        if (filasAfectadas == 1) {
            return ResponseEntity.status(HttpStatus.CREATED).body("Usuario creado exitosamente");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al crear el usuario");
        }
    }

}
