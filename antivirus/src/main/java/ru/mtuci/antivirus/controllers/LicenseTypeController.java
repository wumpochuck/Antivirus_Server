package ru.mtuci.antivirus.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.mtuci.antivirus.entities.requests.LicenseTypeRequest;
import ru.mtuci.antivirus.entities.LicenseType;
import ru.mtuci.antivirus.services.LicenseTypeService;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/license-types")
@PreAuthorize("hasRole('ADMIN')")
public class LicenseTypeController {

    private final LicenseTypeService licenseTypeService;

    @Autowired
    public LicenseTypeController(LicenseTypeService licenseTypeService) {
        this.licenseTypeService = licenseTypeService;
    }

    @PostMapping
    public ResponseEntity<?> createLicenseType(@Valid @RequestBody LicenseTypeRequest licenseTypeRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String msg = Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage();
            return ResponseEntity.status(400).body("Validation error: " + msg);
        }

        LicenseType licenseType = licenseTypeService.createLicenseType(licenseTypeRequest);
        return ResponseEntity.status(200).body(licenseType);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getLicenseType(@PathVariable Long id) {
        LicenseType licenseType = licenseTypeService.getLicenseTypeById(id);
        return ResponseEntity.status(200).body(licenseType.toString());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateLicenseType(@PathVariable Long id, @Valid @RequestBody LicenseTypeRequest licenseTypeRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String msg = Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage();
            return ResponseEntity.status(400).body("Validation error: " + msg);
        }

        LicenseType licenseType = licenseTypeService.updateLicenseType(id, licenseTypeRequest);
        return ResponseEntity.status(200).body(licenseType);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteLicenseType(@PathVariable Long id) {
        licenseTypeService.deleteLicenseType(id);
        return ResponseEntity.status(200).body("License type with id " + id + " was deleted");
    }

    @GetMapping
    public ResponseEntity<List<LicenseType>> getAllLicenseTypes() {
        List<LicenseType> licenseTypes = licenseTypeService.getAllLicenseTypes();
        return ResponseEntity.status(200).body(licenseTypes);
    }
}