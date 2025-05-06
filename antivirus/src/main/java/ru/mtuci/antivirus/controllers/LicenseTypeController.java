package ru.mtuci.antivirus.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.mtuci.antivirus.entities.LicenseType;
import ru.mtuci.antivirus.entities.requests.LicenseTypeRequest;
import ru.mtuci.antivirus.services.LicenseTypeService;

import java.util.List;
import java.util.Objects;

@PreAuthorize("hasRole('ROLE_ADMIN')")
@RestController
@RequestMapping("/license-types")
@RequiredArgsConstructor
public class LicenseTypeController {

    private final LicenseTypeService licenseTypeService;

    @PostMapping
    public ResponseEntity<?> createLicenseType(@Valid @RequestBody LicenseTypeRequest licenseTypeRequest, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            String errMsg = Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage();
            return ResponseEntity.status(200).body("Validation error: " + errMsg);
        }

        LicenseType licenseType = licenseTypeService.createLicenseType(licenseTypeRequest);
        return ResponseEntity.status(200).body(licenseType);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getLicenseTypeById(@PathVariable Long id){
        return ResponseEntity.status(200).body(licenseTypeService.getLicenseTypeById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateLicenseType(@PathVariable Long id, @Valid @RequestBody LicenseTypeRequest licenseTypeRequest, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            String errMsg = Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage();
            return ResponseEntity.status(200).body("Validation error: " + errMsg);
        }

        LicenseType licenseType = licenseTypeService.updateLicenseType(id, licenseTypeRequest);
        return ResponseEntity.status(200).body(licenseType);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteLicenseTypeById(@PathVariable Long id){
        licenseTypeService.deleteLicenseType(id);
        return ResponseEntity.status(200).body("License type with id: " + id + " deleted");
    }

    @GetMapping
    public ResponseEntity<List<LicenseType>> getAllLicenseTypes(){
        return ResponseEntity.status(200).body(licenseTypeService.getAllLicenseTypes());
    }
}