package ru.mtuci.antivirus.services;

import org.springframework.stereotype.Service;
import ru.mtuci.antivirus.entities.DeviceLicense;
import ru.mtuci.antivirus.repositories.DeviceLicenseRepository;

@Service
public class DeviceLicenseService {

    private final DeviceLicenseRepository deviceLicenseRepository;

    public DeviceLicenseService(DeviceLicenseRepository deviceLicenseRepository) {
        this.deviceLicenseRepository = deviceLicenseRepository;
    }

    public void save(DeviceLicense deviceLicense) {
        deviceLicenseRepository.save(deviceLicense);
    }
}
