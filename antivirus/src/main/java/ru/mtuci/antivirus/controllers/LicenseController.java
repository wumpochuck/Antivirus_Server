package ru.mtuci.antivirus.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.mtuci.antivirus.entities.*;
import ru.mtuci.antivirus.entities.DTO.LicenseRequest;
import ru.mtuci.antivirus.services.LicenseService;

@RestController
@RequestMapping("/license")
public class LicenseController {

    private final LicenseService licenseService;

    @Autowired
    public LicenseController(LicenseService licenseService) {
        this.licenseService = licenseService;
    }

    @PostMapping("/test")
    public ResponseEntity<?> test() {
        return ResponseEntity.ok("Test");
    }

//    @PostMapping("/create")
//    public ResponseEntity<?> createLicense(@Valid @RequestBody LicenseRequest licenseRequest, HttpServletRequest request) {
//        if (!request.isUserInRole("ADMIN")) {
//            return ResponseEntity.status(403).body("Access Denied: you don't have permission to create license");
//        }
//
//        License license = licenseService.createLicense(licenseRequest);
//
//        if (license == null) {
//            return ResponseEntity.badRequest().body("Failed to create license");
//        }
//
//        return ResponseEntity.ok("License created successfully, license: " + license);
//    }
}