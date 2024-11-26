package ru.mtuci.antivirus.controllers;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.mtuci.antivirus.entities.DTO.ActivationRequest;
import ru.mtuci.antivirus.entities.*;
import ru.mtuci.antivirus.services.*;

@RestController
@RequestMapping(name="/license")
public class ActivationController {

    private final UserService userService;
    private final DeviceService deviceService;
    private final LicenseService licenseService;

    public ActivationController(UserService userService, DeviceService deviceService, LicenseService licenseService) {
        this.userService = userService;
        this.deviceService = deviceService;
        this.licenseService = licenseService;
    }


    @PostMapping("/test")
    public String test() {
        return "Tested successfully";
    }

    @PostMapping("/activate")
    public ResponseEntity<?> activateLicense(@Valid @RequestBody ActivationRequest request, BindingResult bindingResult) {
        System.out.println("ActivationController: activateLicense: Started activating license, data: " + request.getActivationCode() + ", " + request.getDeviceName() + ", " + request.getMacAddress());
        if (bindingResult.hasErrors()) {
            StringBuilder errorMessage = new StringBuilder();
            bindingResult.getFieldErrors().forEach(error ->
                    errorMessage.append(error.getField())
                            .append(": ")
                            .append(error.getDefaultMessage())
                            .append("; ")
            );
            return ResponseEntity.badRequest().body(errorMessage.toString());
        }
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                return ResponseEntity.status(403).body("User is not authenticated");
            }

            String username = authentication.getName();
            System.out.println("ActivationController: activateLicense: Request from user: " + username);

            String activationCode = request.getActivationCode(); // TODO rewrite logic here

            User deviceOwner = userService.getUserByLogin(username);

            // Register or update device
            Device device = deviceService.registerOrUpdateDevice(request, deviceOwner);

            // Activate license
            Ticket ticket = licenseService.activateLicense(activationCode, device, username);

            return ResponseEntity.ok(ticket);

        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception ex) {
            // global exception handler
            return ResponseEntity.status(500).body("Internal server error: " + ex.getMessage());
        }
    }
}
