package ru.mtuci.antivirus.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.mtuci.antivirus.entities.DTO.LicenseUpdateRequest;
import ru.mtuci.antivirus.entities.Ticket;
import ru.mtuci.antivirus.services.AuthenticationService;
import ru.mtuci.antivirus.services.LicenseService;

@RestController
@RequestMapping("/license")
public class LicenseUpdateController {

    private final AuthenticationService authenticationService;
    private final LicenseService licenseService;

    @Autowired
    public LicenseUpdateController(AuthenticationService authenticationService, LicenseService licenseService) {
        this.authenticationService = authenticationService;
        this.licenseService = licenseService;
    }


    @PostMapping("/update")
    public ResponseEntity<?> updateLicense(@RequestBody LicenseUpdateRequest updateRequest){
        try {
            if (!authenticationService.authenticate(updateRequest.getLogin(), updateRequest.getPassword())) {
                return ResponseEntity.status(403).body("Invalid login or password");
            }

            Ticket ticket = licenseService.updateLicense(updateRequest.getLicenseKey(), updateRequest.getLogin());
            if (ticket.getIsBlocked()) {
                return ResponseEntity.status(400).body("License update unavailable: " + ticket.getSignature());
            }

            return ResponseEntity.ok("License update successful, Ticket:\n" + ticket.getBody());
        } catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}