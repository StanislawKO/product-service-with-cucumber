package com.stas_kozh.domain.configuration;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

@Configuration
@Slf4j
public class DataSourceConfig {

    @Value("${spring.datasource.url}")
    private String dbUrl;

    @Value("${spring.datasource.username}")
    private String usernameSecret;

    @Value("${spring.datasource.password}")
    private String passwordSecret;

    @Value("${spring.datasource.driver-class-name}")
    private String driverClassName;

    @Bean
    public DataSource dataSource() {
        HikariConfig config = new HikariConfig();
        config.setDriverClassName(driverClassName);
        config.setJdbcUrl(dbUrl);
        String username = getSecretFromFile(usernameSecret).orElse(usernameSecret);
        String password = getSecretFromFile(passwordSecret).orElse(passwordSecret);
        config.setUsername(username);
        config.setPassword(password);
        return new HikariDataSource(config);
    }

    private Optional<String> getSecretFromFile(String secretFile) {
        String secret = null;
        try {
            secret = Files.readString(Path.of(secretFile));
        } catch (IOException e) {
            log.warn("Can't get secret from file: [{}]", e.getMessage());
        }
        return Optional.ofNullable(secret);
    }
}
