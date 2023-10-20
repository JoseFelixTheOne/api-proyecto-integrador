package com.cibertec.proyecto.integrador.config;
import javax.sql.DataSource;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.jdbc.core.JdbcTemplate;


import org.springframework.boot.web.servlet.FilterRegistrationBean;

@Configuration
public class AppConfig {

    private DataSource datasource;


    public JdbcTemplate jdbcTemplate() {

        return new JdbcTemplate(datasource);

    }

    public FilterRegistrationBean<JwtAuthenticationFilter> jwtFilter(JWTProvider jwtProvider) {
        FilterRegistrationBean<JwtAuthenticationFilter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        filterRegistrationBean.setFilter(new JwtAuthenticationFilter(jwtProvider));
        filterRegistrationBean.addUrlPatterns("/api/*");
        return filterRegistrationBean;
    }

}
