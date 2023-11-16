package com.cibertec.proyecto.integrador.controller;

import com.cibertec.proyecto.integrador.config.JWTProvider;
import com.cibertec.proyecto.integrador.entity.Usuario;
import com.cibertec.proyecto.integrador.services.UserService;
import com.cibertec.proyecto.integrador.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final JWTProvider jwtProvider;

    private final UsuarioService servicio;
    private UserService userService;
    @Autowired
    public UserController(UsuarioService servicio, JWTProvider jwtProvider, UserService userService ) {
        this.servicio=servicio;
        this.jwtProvider=jwtProvider;
        this.userService = userService;
    }

    @GetMapping("")
    public List<Usuario> listUsers(){
        return userService.listUsers();
    }

    @GetMapping("/{id}")
    public Usuario getUserToId(@PathVariable int id){
        return userService.getUserToId(id);
    }

    @GetMapping("/emailuser/{email}")
    public List<Usuario> getUserToEmail(@PathVariable String email){
        return userService.getUserToEmail(email);
    }

    @GetMapping("/username/{username}")
    public List<Usuario> getUserToUsername(@PathVariable String username){
        return userService.getUserToUsername(username);
    }

    @GetMapping("/nrodoc/{nrodoc}")
    public List<Usuario> getUserToDocument(@PathVariable String document){
        return userService.getUserToDocument(document);
    }

    // parte de Elifio
    @PostMapping("/usuario")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> crearUsuario(@RequestBody Usuario usuario , @RequestHeader("Authorization") String token) {
        if (jwtProvider.validateToken(token.replace("Bearer ", ""))) {
            int filasAfectadas = servicio.insertarUsuario(usuario);
            if (filasAfectadas == 1) {
                return ResponseEntity.status(HttpStatus.CREATED).body("Usuario creado exitosamente");
            }else if(filasAfectadas==-1) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ya existe el correo en la base de datos");
            }else if(filasAfectadas==-2) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ya existe el nombre del usuario en la base de datos");
            }
            else if(filasAfectadas==-3) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("El formato del correo no es válido");
            }
            else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al crear el usuario");
            }
        } else {
            // El token no es válido, maneja el error adecuadamente
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido");
        }
    }


    @PostMapping
    public ResponseEntity<String> crearUsuarioSinToken(@RequestBody Usuario usuario) {
        int filasAfectadas = servicio.insertarUsuario(usuario);
        if (filasAfectadas == 1) {
            return ResponseEntity.status(HttpStatus.CREATED).body("Usuario creado exitosamente");
        } else if(filasAfectadas==-1) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ya existe el correo en la base de datos");
        }else if(filasAfectadas==-2) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ya existe el nombre del usuario en la base de datos");
        }
        else if(filasAfectadas==-3) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("El formato del correo no es válido");
        }
        else{
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al crear el usuario");
        }

    }
}
