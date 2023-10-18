package com.cibertec.proyecto.integrador.services;

import com.cibertec.proyecto.integrador.entity.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
@Service
public class LoginService {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public LoginService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate=jdbcTemplate;
    }

    public int login(String usuario,String contra) {
        String cifrado= cifrarCadena(contra);
        String consultaSql = "SELECT COUNT(*) FROM users where username='"+usuario+"' and password='"+cifrado+"'";
        int contador = jdbcTemplate.queryForObject(consultaSql, Integer.class);
        return contador;
    }

    public Usuario loginUsuario(String nombreUsuario, String contra) {
        String cifrado = cifrarCadena(contra);
        String consultaSql = "SELECT id, username, email, document, role FROM users WHERE username=? AND password=?";
        try {
            Usuario usuarioEncontrado = jdbcTemplate.queryForObject(
                    consultaSql,
                    new Object[]{nombreUsuario, cifrado},
                    (rs, rowNum) -> new Usuario(
                            rs.getInt("id"),
                            rs.getString("username"),
                            rs.getString("email"),
                            rs.getString("document"),
                            rs.getInt("role")
                    )
            );
            return usuarioEncontrado;
        } catch (Exception e) {

            return null;
        }
    }



    /*

        insert into roles(name)  values('Admin');

        insert into users(id,username,email,password,document,role)
        values(1,'eroldan','elifioroldan@hotmail.com',
        'EF797C8118F02DFB649607DD5D3F8C7623048C9C063D532CC95C5ED7A898A64F','72722415',1)

    * */
    public static String cifrarCadena(String cadena) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] bytecadena = cadena.getBytes(StandardCharsets.UTF_8);
            byte[] bytecifrado = digest.digest(bytecadena);
            StringBuilder hexString = new StringBuilder();
            for (byte b : bytecifrado) {
                String hex = String.format("%02X", b);
                hexString.append(hex);
            }

            return hexString.toString().toLowerCase();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

}
