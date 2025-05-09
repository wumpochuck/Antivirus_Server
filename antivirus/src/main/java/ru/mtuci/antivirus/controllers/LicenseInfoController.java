package ru.mtuci.antivirus.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.mtuci.antivirus.entities.Device;
import ru.mtuci.antivirus.entities.License;
import ru.mtuci.antivirus.entities.Ticket;
import ru.mtuci.antivirus.entities.User;
import ru.mtuci.antivirus.entities.requests.LicenseInfoRequest;
import ru.mtuci.antivirus.services.DeviceService;
import ru.mtuci.antivirus.services.LicenseService;
import ru.mtuci.antivirus.services.UserService;

import java.util.Objects;

@RestController
@RequestMapping("/license")
@RequiredArgsConstructor
public class LicenseInfoController {

    private final DeviceService deviceService;
    private final LicenseService licenseService;
    private final UserService userService;

    @PostMapping("/info")
    public ResponseEntity<?> getLicenseInfo(@Valid @RequestBody LicenseInfoRequest licenseInfoRequest, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            String errMsg = Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage();
            return ResponseEntity.status(200).body("Validation error: " + errMsg);
        }

        try{
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User user = userService.findUserByLogin(authentication.getName());

            Device device = deviceService.getDeviceByMacAddress(licenseInfoRequest.getMacAddress());

            if(!Objects.equals(device.getUser().getId(), user.getId())){
                throw new IllegalArgumentException("Authentication error: invalid user");
            }

            if(device == null){
                return ResponseEntity.status(404).body("Error: device not found");
            }

            License activeLicense = licenseService.getActiveLicenseForDevice(device, user, licenseInfoRequest.getLicenseCode());

            licenseService.validateActivation(activeLicense,device,user.getLogin());

            Ticket ticket = licenseService.generateTicket(activeLicense, device);

            return ResponseEntity.status(200).body("License found, Ticket: " + ticket.toString());

        } catch (Exception e){
            return ResponseEntity.status(500).body("Internal Server Error: " + e.getMessage());
        }
    }

}
