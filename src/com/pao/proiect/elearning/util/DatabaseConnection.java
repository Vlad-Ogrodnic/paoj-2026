package com.pao.proiect.elearning.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class DatabaseConnection {
    private static DatabaseConnection instance;
    private final String url;
    private Connection connection;


    private DatabaseConnection() {
        Properties props = loadProperties();
        this.url = props.getProperty("db.url");
        if (url == null || url.isBlank()) {
            throw new IllegalStateException("Proprietatea db.url lipseste din db.properties");
        }
    }



    public static synchronized DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    public synchronized Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(url);
            try (Statement stmt = connection.createStatement()) {
                stmt.execute("PRAGMA foreign_keys = ON");
            }
        }
        return connection;
    }

    public void initializeSchema() {
        try (InputStream in = getClass().getResourceAsStream("/com/pao/proiect/elearning/schema.sql")) {
            if (in == null) {
                throw new IllegalStateException("schema.sql nu a fost gasit in classpath");
            }
            String sql = new String(in.readAllBytes(), StandardCharsets.UTF_8);
            try (Statement stmt = getConnection().createStatement()) {
                for (String command : sql.split(";")) {
                    String trimmed = command.trim();
                    if (!trimmed.isEmpty()) {
                        stmt.execute(trimmed);
                    }
                }
            }
        } catch (IOException | SQLException e) {
            throw new RuntimeException("Nu s-a putut initializa schema bazei de date", e);
        }
    }

    private Properties loadProperties() {
        Properties props = new Properties();
        try (InputStream in = getClass().getResourceAsStream("/com/pao/proiect/elearning/resources/db.properties")) {
            if (in == null) {
                throw new IllegalStateException("db.properties nu a fost gasit in classpath");
            }
            props.load(in);
        } catch (IOException e) {
            throw new RuntimeException("Nu s-a putut citi db.properties", e);
        }
        return props;
    }
}
