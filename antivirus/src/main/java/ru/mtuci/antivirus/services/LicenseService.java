package ru.mtuci.antivirus.services;

import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.mtuci.antivirus.entities.*;
import ru.mtuci.antivirus.entities.DTO.LicenseRequest;
import ru.mtuci.antivirus.repositories.LicenseRepository;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Date;

@Service
public class LicenseService{

    private final LicenseRepository licenseRepository;
    private final ProductService productService;
    private final UserService userService;
    private final LicenseTypeService licenseTypeService;
    private final LicenseHistoryService licenseHistoryService;
    private final DeviceLicenseService deviceLicenseService;

    @Autowired
    public LicenseService(LicenseRepository licenseRepository, ProductService productService, UserService userService, LicenseTypeService licenseTypeService, LicenseHistoryService licenseHistoryService, DeviceLicenseService deviceLicenseService) {
        this.licenseRepository = licenseRepository;
        this.productService = productService;
        this.userService = userService;
        this.licenseTypeService = licenseTypeService;
        this.licenseHistoryService = licenseHistoryService;
        this.deviceLicenseService = deviceLicenseService;
    }

    /// License creation
    public License createLicense(LicenseRequest licenseRequest) {

        // Trying to get product, user and license type by id
        Product product = productService.getProductById(licenseRequest.getProductId());
        if(product == null){
            throw new IllegalArgumentException("Product not found");
        }

        User user = userService.getUserById(licenseRequest.getOwnerId());
        if(user == null){
            throw new IllegalArgumentException("User not found");
        }

        LicenseType licenseType = licenseTypeService.getLicenseTypeById(licenseRequest.getLicenseTypeId());
        if(licenseType == null){
            throw new IllegalArgumentException("License type not found");
        }

        // Generate license code
        String code = generateLicenseCode(licenseRequest);

        // Create license and saving
        License license = new License();
        license.setCode(code);
        license.setUser(user);
        license.setProduct(product);
        license.setType(licenseType);
        license.setFirstActivationDate(null);
        license.setEndingDate(null);
        license.setIsBlocked(false);
        license.setDevicesCount(licenseRequest.getDeviceCount());
        license.setOwner(user);
        license.setDuration(licenseRequest.getDuration());
        license.setDescription(licenseRequest.getDescription());
        license.setProduct(product);
        licenseRepository.save(license);

        // Save license history
        LicenseHistory licenseHistory = new LicenseHistory(license, user, "CREATED", new Date(), "License created");
        licenseHistoryService.saveLicenseHistory(licenseHistory);

        return license;
    }

    /// License activation
    public Ticket activateLicense(String activationCode, Device device, String login) {

        // Trying to get license by activation code
        License license = licenseRepository.getLicensesByCode(activationCode);
        if(license == null){
            throw new IllegalArgumentException("License not found");
        }

        // Validate license
        validateActivation(license, device, login);

        // Create device license
        createDeviceLicense(license, device);

        // Update license
        updateLicense(license);

        // Save license history
        LicenseHistory licenseHistory = new LicenseHistory(license, license.getOwner(), "ACTIVATED", new Date(), "License activated");
        licenseHistoryService.saveLicenseHistory(licenseHistory);

        // Generate ticket
        Ticket ticket = new Ticket();
        ticket.setCurrentDate(new Date());
        ticket.setLifetime(license.getDuration());
        ticket.setActivationDate(new Date());
        ticket.setExpirationDate(license.getEndingDate());
        ticket.setUserId(license.getOwner().getId());
        ticket.setDeviceId(device.getId());
        ticket.setIsBlocked(false);
        ticket.setSignature(generateSignature(ticket));
        return ticket;
    }

    // Other methods

    private void validateActivation(License license, Device device, String login) {

        // Is license blocked
        if (license.getIsBlocked()) {
            throw new IllegalArgumentException("Could not activate license: license is blocked");
        }

        // Is license expired
        if(license.getEndingDate() != null) {
            if (license.getEndingDate().before(new Date())) {
                throw new IllegalArgumentException("Could not activate license: license is expired");
            }
        }
        // Is license already activated
        if (license.getFirstActivationDate() != null) {
            throw new IllegalArgumentException("Could not activate license: license is already activated");
        }

        // Is device count exceeded
        if (license.getDevicesCount() <= deviceLicenseService.getDeviceLicensesByLicense(license).size()) {
            throw new IllegalArgumentException("Could not activate license: device count exceeded");
        }

        // TODO Add another validation rules...
    }

    private void createDeviceLicense(License license, Device device) {
        DeviceLicense deviceLicense = new DeviceLicense();
        deviceLicense.setDevice(device);
        deviceLicense.setLicense(license);
        deviceLicense.setActivationDate(new Date());

        deviceLicenseService.save(deviceLicense);
    }

    private void updateLicense(License license) {
        license.setFirstActivationDate(new Date());
        license.setEndingDate(new Date(System.currentTimeMillis() + license.getDuration()));
        licenseRepository.save(license);
    }

    private String generateSignature(Ticket ticket) {
        return Keys.hmacShaKeyFor(ticket.toString().getBytes()).toString(); // TODO: implement signature generation...
    }

    private String generateLicenseCode(LicenseRequest licenseRequest){
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            String data = licenseRequest.getProductId() + licenseRequest.getOwnerId() + licenseRequest.getLicenseTypeId() + licenseRequest.getDeviceCount() + licenseRequest.getDuration() + licenseRequest.getDescription() + LocalDateTime.now();
            byte[] hash = digest.digest(data.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error generating license code", e);
        }
        // TODO: implement license code generation...
    }
}
