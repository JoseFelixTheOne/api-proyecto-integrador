package com.cibertec.proyecto.integrador.services;

import com.cibertec.proyecto.integrador.entity.Usuario;
import com.cibertec.proyecto.integrador.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<Usuario> listUsers() {
        return userRepository.findAll();
    }

    @Override
    public Usuario getUserToId(int id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public List<Usuario> getUserToUsername(String username) {
        return userRepository.getUserByUsername(username);
    }

    @Override
    public List<Usuario> getUserToEmail(String email) {
        return userRepository.getUserByEmail(email);
    }

    @Override
    public List<Usuario> getUserToDocument(String document) {
        return userRepository.getUserByDocument(document);
    }
}
