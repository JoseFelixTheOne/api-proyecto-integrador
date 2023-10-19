package com.cibertec.proyecto.integrador.entity;

public class Usuario {
    private int id;
    private String username;
    private String email;
    private String password;
    private String document;
    private int role;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }


    // Constructor sin argumentos
    public Usuario() {
    }
    // Constructor
    public Usuario(int id, String username, String email, String document, int role) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.document = document;
        this.role = role;
    }

}