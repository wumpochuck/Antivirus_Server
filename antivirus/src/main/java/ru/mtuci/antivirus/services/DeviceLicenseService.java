package ru.mtuci.antivirus.services;

import org.springframework.stereotype.Service;
import ru.mtuci.antivirus.entities.Device;
import ru.mtuci.antivirus.entities.DeviceLicense;
import ru.mtuci.antivirus.entities.License;
import ru.mtuci.antivirus.repositories.DeviceLicenseRepository;

import java.util.List;

@Service
public class DeviceLicenseService {

    private final DeviceLicenseRepository deviceLicenseRepository;

    public DeviceLicenseService(DeviceLicenseRepository deviceLicenseRepository) {
        this.deviceLicenseRepository = deviceLicenseRepository;
    }

    public List<DeviceLicense> getDeviceLicensesByLicense(License license) {
        return deviceLicenseRepository.getDeviceLicensesByLicense(license);
    }

    public void save(DeviceLicense deviceLicense) {
        deviceLicenseRepository.save(deviceLicense);
    }

    public DeviceLicense getDeviceLicenseByDeviceIdAndLicenseId(Long deviceId, Long licenseId) {
        return deviceLicenseRepository.getDeviceLicenseByDeviceIdAndLicenseId(deviceId, licenseId);
    }

    public DeviceLicense getDeviceLicenseByDevice(Device device) {
        return deviceLicenseRepository.getDeviceLicenseByDevice(device);
    }

    public DeviceLicense getDeviceLicenseByDeviceAndIsNotBlocked(Device device) {
        List<DeviceLicense> deviceLicenses = deviceLicenseRepository.getDeviceLicensesByDevice(device);
        for(DeviceLicense deviceLicense : deviceLicenses) {
            if (!deviceLicense.getLicense().getIsBlocked()) {
                return deviceLicense;
            }
        }
        return null;
    }
}
