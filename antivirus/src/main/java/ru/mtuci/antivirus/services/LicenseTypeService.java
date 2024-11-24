package ru.mtuci.antivirus.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.mtuci.antivirus.entities.LicenseType;
import ru.mtuci.antivirus.repositories.LicenseTypeRepository;

@Service
public class LicenseTypeService {

    private final LicenseTypeRepository licenseTypeRepository;

    @Autowired
    public LicenseTypeService(LicenseTypeRepository licenseTypeRepository) {
        this.licenseTypeRepository = licenseTypeRepository;
    }

    public LicenseType getLicenseTypeById(Long licenseTypeId) {
        return licenseTypeRepository.findById(licenseTypeId).orElse(null);
    }
}
