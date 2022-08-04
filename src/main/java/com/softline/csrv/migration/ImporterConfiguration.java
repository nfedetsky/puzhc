package com.softline.csrv.migration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.Serializable;


@Component("importerConfiguration")
public class ImporterConfiguration implements Serializable {

    @Value("${main.datasource.driverClassName}")
    private String driverClassName;
    @Value("${main.datasource.jdbcUrl}")
    private String jdbcUrl;
    @Value("${main.datasource.username}")
    private String username;
    @Value("${main.datasource.password}")
    private String password;

    public ImporterConfiguration(){}

    public ImporterConfiguration(String driverClassName, String jdbcUrl, String username,
                                 String password) {
        this.driverClassName = driverClassName;
        this.jdbcUrl = jdbcUrl;
        this.username = username;
        this.password = password;
    }

    public String getDriverClassName() {
        return driverClassName;
    }

    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }

    public String getJdbcUrl() {
        return jdbcUrl;
    }

    public void setJdbcUrl(String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "ImporterConfiguration{" +
                "driverClassName='" + driverClassName + '\'' +
                ", jdbcUrl='" + jdbcUrl + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
