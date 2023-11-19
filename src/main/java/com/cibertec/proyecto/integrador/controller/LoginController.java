package com.cibertec.proyecto.integrador.controller;
import com.cibertec.proyecto.integrador.config.JWTProvider;
import com.cibertec.proyecto.integrador.entity.*;
import com.cibertec.proyecto.integrador.services.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;


@RestController
@RequestMapping("/login")
public class LoginController {

    private final LoginService servicio;
    private final JWTProvider jwtProvider;


    @Autowired
    public LoginController(LoginService servicio , JWTProvider jwtProvider ) {
        this.servicio=servicio;
        this.jwtProvider=jwtProvider;
    }

    @PostMapping
    public ResponseEntity<?> loginusertoken(@RequestBody Login login) {
        Usuario usuarioEncontrado = servicio.loginUsuario(login.getEmail(), login.getPassword());
        if (usuarioEncontrado != null) {
            List<SimpleGrantedAuthority> authorities = Arrays.asList(new SimpleGrantedAuthority("ADMIN"));
            String token = jwtProvider.generateToken(usuarioEncontrado.getUsername(), authorities);
            User ousu = servicio.obtenerUsuarioPorEmail(login.getEmail());
            ousu.setToken(token);
            return ResponseEntity.ok(ousu);
        } else {
            ErrorResponse errorResponse = new ErrorResponse("Credenciales incorrectas");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }
    }

}
