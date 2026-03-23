package com.pao.laboratory05.audit;

public record AuditEntry(String timestamp, String action, String target) { }
