package com.cibertec.proyecto.integrador.services;

import com.cibertec.proyecto.integrador.entity.Usuario;

import java.util.List;

public interface UserService {
    public List<Usuario> listUsers();
    public Usuario getUserToId(int id);
    public List<Usuario> getUserToUsername(String username);
    public List<Usuario> getUserToEmail(String email);
    public List<Usuario> getUserToDocument(String document);
}
