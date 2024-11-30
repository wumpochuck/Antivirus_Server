package ru.mtuci.antivirus.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ru.mtuci.antivirus.entities.Device;
import ru.mtuci.antivirus.entities.License;
import ru.mtuci.antivirus.entities.Ticket;
import ru.mtuci.antivirus.entities.User;
import ru.mtuci.antivirus.services.DeviceService;
import ru.mtuci.antivirus.services.LicenseService;
import ru.mtuci.antivirus.services.UserService;

import java.util.List;
import java.util.Optional;

//TODO: 1. Убрать лишние проверки (например стр. 42-43)
//TODO: 2. Поменять логику поиска текущей лицензии из списка (передать код вместе с мак адресом 39, 60)

@RestController
@RequestMapping("/license")
public class LicenseInfoController {

    private final DeviceService deviceService;
    private final LicenseService licenseService;
    private final UserService userService;

    @Autowired
    public LicenseInfoController(DeviceService deviceService, LicenseService licenseService, UserService userService) {
        this.deviceService = deviceService;
        this.licenseService = licenseService;
        this.userService = userService;
    }

    @GetMapping("/info")
    public ResponseEntity<?> getLicenseInfo(@Valid @RequestParam("macAddress") String macAddress){
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                return ResponseEntity.status(403).body("User is not authenticated");
            }

            String login = authentication.getName();
            User user = userService.findUserByLogin(login);

            System.out.println("LicenseInfoController: getLicenseInfo: Request from user: " + login);
            System.out.println("LicenseInfoController: getLicenseInfo: Requested MAC address: " + macAddress);

            // Looking for the device
            Device device = deviceService.getDeviceByInfo(macAddress, user);
            if(device == null){
                return ResponseEntity.badRequest().body("Device not found");
            }


            // Getting active licenses for the device
            List<License> activeLicenses = licenseService.getActiveLicenseForDevice(device, user);
            if(activeLicenses == null || activeLicenses.isEmpty()){
                return ResponseEntity.ok("No active licenses found for the device");
            }

            // Generating ticket
            Ticket ticket = licenseService.generateTicket(activeLicenses.get(0), device);

            return ResponseEntity.ok("Licenses found. Ticket:\n" + ticket.getBody());

        } catch (Exception e){
            return ResponseEntity.status(500).body("Internal server error: " + e.getMessage());
        }
    }

}
