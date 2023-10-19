package com.cibertec.proyecto.integrador.repository;

import com.cibertec.proyecto.integrador.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<Usuario, Integer> {
    public List<Usuario> getUserByUsername(String username);
    public List<Usuario> getUserByDocument(String document);
    public List<Usuario> getUserByEmail(String email);
}
