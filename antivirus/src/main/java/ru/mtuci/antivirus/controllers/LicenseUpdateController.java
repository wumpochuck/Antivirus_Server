package ru.mtuci.antivirus.controllers;


import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.mtuci.antivirus.entities.requests.LicenseUpdateRequest;
import ru.mtuci.antivirus.entities.Ticket;
import ru.mtuci.antivirus.services.LicenseService;

import java.util.Objects;

//TODO: 1. Убрать лишние проверки ✅

@RestController
@RequestMapping("/license")
public class LicenseUpdateController {

    private final LicenseService licenseService;

    @Autowired
    public LicenseUpdateController(LicenseService licenseService) {
        this.licenseService = licenseService;
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateLicense(@Valid @RequestBody LicenseUpdateRequest updateRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String msg = Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage();
            return ResponseEntity.status(400).body("Validation error: " + msg);
        }

        try { // TODO: 1 убрана лишняя проверка аутентификации

            Ticket ticket = licenseService.updateExistentLicense(updateRequest.getLicenseCode(), updateRequest.getLogin(), updateRequest.getMacAddress());

            /// Я не помню зачем это, но лучше не убирать а то вдруг понадобится | 03.12.24
            // if (ticket.getIsBlocked()) {
            //     return ResponseEntity.status(400).body("License update unavailable: " + ticket.getSignature());
            // }

            return ResponseEntity.status(200).body("License update successful. " + ticket.toString());
        } catch (IllegalArgumentException e){
            return ResponseEntity.status(400).body("Validation error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Internal server error: " + e.getMessage());
        }
    }
}
