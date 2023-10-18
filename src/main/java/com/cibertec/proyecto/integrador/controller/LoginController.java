package com.cibertec.proyecto.integrador.controller;
import com.cibertec.proyecto.integrador.entity.ErrorResponse;
import com.cibertec.proyecto.integrador.entity.Usuario;
import com.cibertec.proyecto.integrador.services.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api")
public class LoginController {

    private final LoginService servicio;


    @Autowired
    public LoginController(LoginService servicio ) {
        this.servicio=servicio;
    }

    @GetMapping("/login/{usuario}/{contra}")
    public int obtenerContador(@PathVariable String usuario, @PathVariable  String contra)
    {
        int exito= servicio.login(usuario,contra);
        return exito;
    }

    @GetMapping("/loginuser/{usuario}/{contra}")
    public ResponseEntity<?> login(@PathVariable String usuario, @PathVariable String contra) {
        Usuario usuarioEncontrado = servicio.loginUsuario(usuario, contra);
        if (usuarioEncontrado != null) {
            return ResponseEntity.ok(usuarioEncontrado);
        } else {
            ErrorResponse errorResponse = new ErrorResponse("Credenciales incorrectas");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }
    }


}
