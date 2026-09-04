package com.vehiqon.security.controller;

import org.junit.jupiter.api.Disabled;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Disabled("Temporarily disabled while fixing security test configuration")
//@RestController
//@RequestMapping("/api/admin")
public class TestAdminController {

    @GetMapping
    @PreAuthorize("hasRole('ADMIN")
    public ResponseEntity<String> getAdminDashboard() {
        return ResponseEntity.ok("Admin data");
    }
}
