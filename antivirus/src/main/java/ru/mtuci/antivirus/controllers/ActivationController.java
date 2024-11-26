package ru.mtuci.antivirus.controllers;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.mtuci.antivirus.entities.DTO.ActivationRequest;
import ru.mtuci.antivirus.entities.*;
import ru.mtuci.antivirus.services.*;

@RestController
@RequestMapping("/license")
public class ActivationController {

    private final UserService userService;
    private final DeviceService deviceService;
    private final LicenseService licenseService;

    public ActivationController(UserService userService, DeviceService deviceService, LicenseService licenseService) {
        this.userService = userService;
        this.deviceService = deviceService;
        this.licenseService = licenseService;
    }


    @GetMapping("/test")
    public String test() {
        return "ActivationController: Tested successfully";
    }

    @PostMapping("/activate")
//    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    public ResponseEntity<?> activateLicense(@Valid @RequestBody ActivationRequest activationRequest/*, BindingResult bindingResult*/) {
        System.out.println("ActivationController: activateLicense: Started activating license, data: " + activationRequest.getActivationCode() + ", " + activationRequest.getDeviceName() + ", " + activationRequest.getMacAddress());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null || !authentication.isAuthenticated()){
            return ResponseEntity.status(403).body("User is not authenticated");
        }

        try {

            // Get authenticated user
            System.out.println("ActivationController: activateLicense: Request from user: " + authentication.getName());
            User user = userService.getUserByLogin(authentication.getName());

            // Register or update device
            Device device = deviceService.registerOrUpdateDevice(activationRequest, user);

            // Activate license
            String activationCode = activationRequest.getActivationCode();
            String login = user.getLogin();
            Ticket ticket = licenseService.activateLicense(activationCode, device, login);

            return ResponseEntity.ok("License activated successfully. Ticket: " + ticket.getBody());

        } catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e){
            return ResponseEntity.status(500).body("Internal server error: " + e.getMessage());
        }

//        if (bindingResult.hasErrors()) {
//            StringBuilder errorMessage = new StringBuilder();
//            bindingResult.getFieldErrors().forEach(error ->
//                    errorMessage.append(error.getField())
//                            .append(": ")
//                            .append(error.getDefaultMessage())
//                            .append("; ")
//            );
//            return ResponseEntity.badRequest().body(errorMessage.toString());
//        }
//        try {
//            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//            if (authentication == null || !authentication.isAuthenticated()) {
//                return ResponseEntity.status(403).body("User is not authenticated");
//            }
//
//            String username = authentication.getName();
//            System.out.println("ActivationController: activateLicense: Request from user: " + username);
//
//            String activationCode = request.getActivationCode(); // TODO rewrite logic here
//
//            User deviceOwner = userService.getUserByLogin(username);
//
//            // Register or update device
//            Device device = deviceService.registerOrUpdateDevice(request, deviceOwner);
//
//            // Activate license
//            Ticket ticket = licenseService.activateLicense(activationCode, device, username);
//
//            return ResponseEntity.ok(ticket);
//
//        } catch (IllegalArgumentException ex) {
//            return ResponseEntity.badRequest().body(ex.getMessage());
//        } catch (Exception ex) {
//            // global exception handler
//            return ResponseEntity.status(500).body("Internal server error: " + ex.getMessage());
//        }
    }
}
