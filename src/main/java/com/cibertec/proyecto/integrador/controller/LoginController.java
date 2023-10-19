package com.cibertec.proyecto.integrador.controller;
import com.cibertec.proyecto.integrador.config.JWTProvider;
import com.cibertec.proyecto.integrador.entity.ErrorResponse;
import com.cibertec.proyecto.integrador.entity.JwtResponse;
import com.cibertec.proyecto.integrador.entity.Usuario;
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
@RequestMapping("/api")
public class LoginController {

    private final LoginService servicio;
    private final JWTProvider jwtProvider;


    @Autowired
    public LoginController(LoginService servicio , JWTProvider jwtProvider ) {
        this.servicio=servicio;
        this.jwtProvider=jwtProvider;
    }

    @GetMapping("/login/{usuario}/{contra}")
    public int obtenerContador(@PathVariable String usuario, @PathVariable  String contra)
    {
        int exito= servicio.login(usuario,contra);
        return exito;
    }

    @GetMapping("/verificar/{token}")
    public ResponseEntity<String> tuMetodo(@PathVariable String token) {
        if (jwtProvider.validateToken(token.replace("Bearer ", ""))) {
            return ResponseEntity.ok("Token válido");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido");
        }
    }

    @GetMapping("/loginuser/{usuario}/{contra}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> login(@PathVariable String usuario, @PathVariable String contra, @RequestHeader("Authorization") String token) {
        if (jwtProvider.validateToken(token.replace("Bearer ", ""))) {
            Usuario usuarioEncontrado = servicio.loginUsuario(usuario, contra);
            if (usuarioEncontrado != null) {
                return ResponseEntity.ok(usuarioEncontrado);
            } else {
                ErrorResponse errorResponse = new ErrorResponse("Credenciales incorrectas");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
            }
        } else {
            ErrorResponse errorResponse = new ErrorResponse("Token no válido");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }
    }

    @GetMapping("/loginusertoken/{usuario}/{contra}")
    public ResponseEntity<?> loginusertoken(@PathVariable String usuario, @PathVariable String contra) {
        System.out.println("Entroooooo");
        Usuario usuarioEncontrado = servicio.loginUsuario(usuario, contra);
        if (usuarioEncontrado != null) {
            List<SimpleGrantedAuthority> authorities = Arrays.asList(new SimpleGrantedAuthority("ADMIN"));
            String token = jwtProvider
                    .generateToken(usuarioEncontrado.getUsername(), authorities);
            return ResponseEntity.ok(new JwtResponse(token));
        } else {
            ErrorResponse errorResponse = new ErrorResponse("Credenciales incorrectas");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }
    }


}
