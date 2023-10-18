package com.cibertec.proyecto.integrador.generic;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Cifrado {

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
