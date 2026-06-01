package com.pao.proiect.elearning.service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

public class AuditService {
    private static AuditService instance;
    private static final String AUDIT_FILE = "audit.csv";

    private AuditService() {
    }

    public static AuditService getInstance() {
        if (instance == null) {
            instance = new AuditService();
        }
        return instance;
    }

    public synchronized void logActiune(String numeActiune) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(AUDIT_FILE, true))) {
            writer.write(numeActiune + "," + LocalDateTime.now());
            writer.newLine();
        } catch (IOException e) {
            throw new RuntimeException("Eroare la scrierea in audit.csv", e);
        }
    }
}
