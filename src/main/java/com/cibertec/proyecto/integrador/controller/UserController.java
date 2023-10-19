package com.cibertec.proyecto.integrador.controller;

import com.cibertec.proyecto.integrador.entity.Usuario;
import com.cibertec.proyecto.integrador.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

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
}
