package ru.mtuci.antivirus.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.mtuci.antivirus.entities.requests.LicenseInfoRequest;
import ru.mtuci.antivirus.entities.Device;
import ru.mtuci.antivirus.entities.License;
import ru.mtuci.antivirus.entities.Ticket;
import ru.mtuci.antivirus.entities.User;
import ru.mtuci.antivirus.services.DeviceService;
import ru.mtuci.antivirus.services.LicenseService;
import ru.mtuci.antivirus.services.UserService;

import java.util.Objects;

//TODO: 1. Убрать лишние проверки (например стр. 42-43) ✅
//TODO: 2. Поменять логику поиска текущей лицензии из списка (передать код вместе с мак адресом 39, 60) ✅

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
/*
    @PostMapping("/info")
    public ResponseEntity<?> getLicenseInfo(@Valid @RequestBody LicenseInfoRequest licenseInfoRequest, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            String msg = Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage();
            return ResponseEntity.status(400).body("Validation error: " + msg);
        }

        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();     // TODO: 1 убрано здесь
            User user = userService.findUserByLogin(authentication.getName());

            // Looking for the device
            Device device = deviceService.getDeviceByInfo(licenseInfoRequest.getMacAddress(), user);

            if(device == null){
                throw new IllegalArgumentException("Device not found");
            }

            if(!Objects.equals(device.getUser().getId(), user.getId())){
                throw new IllegalArgumentException("Device not found");
            }

            // Getting license using mac address and code
            License activeLicense = licenseService.getActiveLicenseForDevice(device, user, licenseInfoRequest.getLicenseCode()); // TODO: 2 изменена логика внутри метода

            // Generating ticket
            Ticket ticket = licenseService.generateTicket(activeLicense, device);

            return ResponseEntity.status(200).body("Licenses found. " + ticket.toString());

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body("Validation error: " + e.getMessage());
        }
        catch (Exception e){
            return ResponseEntity.status(500).body("Internal server error: " + e.getMessage());
        }
    }
*/
    @GetMapping("/info")
    public ResponseEntity<?> getLicenseInfo(@Valid @RequestParam String macAddress){
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();     // TODO: 1 убрано здесь
            User user = userService.findUserByLogin(authentication.getName());

            // Looking for the device
            Device device = deviceService.getDeviceByInfo(macAddress, user);

            if(device == null){
                throw new IllegalArgumentException("Device not found");
            }

            if(!Objects.equals(device.getUser().getId(), user.getId())){
                throw new IllegalArgumentException("Device not found");
            }

            // Getting license using mac address and code
            License activeLicense = licenseService.getActiveLicenseForDevice(device, user); // TODO: 2 изменена логика внутри метода

            // Generating ticket
            Ticket ticket = licenseService.generateTicket(activeLicense, device);

            return ResponseEntity.status(200).body("Licenses found. " + ticket.toString());

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body("Validation error: " + e.getMessage());
        }
        catch (Exception e){
            return ResponseEntity.status(500).body("Internal server error: " + e.getMessage());
        }
    }
}
