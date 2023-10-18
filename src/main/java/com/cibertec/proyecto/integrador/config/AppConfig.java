package com.cibertec.proyecto.integrador.config;
import javax.sql.DataSource;

import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class AppConfig {

    private DataSource datasource;


    public JdbcTemplate jdbcTemplate() {

        return new JdbcTemplate(datasource);

    }
}
