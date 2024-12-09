package ru.mtuci.antivirus.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.mtuci.antivirus.entities.*;
import ru.mtuci.antivirus.entities.requests.LicenseRequest;
import ru.mtuci.antivirus.repositories.DeviceRepository;
import ru.mtuci.antivirus.repositories.LicenseRepository;

import java.nio.charset.StandardCharsets;
import java.security.*;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Date;

//TODO: 1. Добавить ЭЦП к тикету на основе полей ✅
//TODO: 2. Пересмотреть логику validateActivation ✅ && updateLicense ✅
//TODO: 3.  validateActivation проверять дату первой активации по другому, чтобы работало на неск. ус-в

@Service
public class LicenseService{

    private final LicenseRepository licenseRepository;
    private final ProductService productService;
    private final UserService userService;
    private final LicenseTypeService licenseTypeService;
    private final LicenseHistoryService licenseHistoryService;
    private final DeviceLicenseService deviceLicenseService;
    private final DeviceRepository deviceRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public LicenseService(LicenseRepository licenseRepository, ProductService productService, UserService userService, LicenseTypeService licenseTypeService, LicenseHistoryService licenseHistoryService, DeviceLicenseService deviceLicenseService, DeviceRepository deviceRepository, PasswordEncoder passwordEncoder) {
        this.licenseRepository = licenseRepository;
        this.productService = productService;
        this.userService = userService;
        this.licenseTypeService = licenseTypeService;
        this.licenseHistoryService = licenseHistoryService;
        this.deviceLicenseService = deviceLicenseService;
        this.deviceRepository = deviceRepository;
        this.passwordEncoder = passwordEncoder;
    }


    /// License creation
    public License createLicense(LicenseRequest licenseRequest) {

        // Trying to get product, user and license type by id
        Product product = productService.getProductById(licenseRequest.getProductId());
        if(product == null){
            throw new IllegalArgumentException("Product not found");
        }

        User user = userService.getUserById(licenseRequest.getUserId());
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
        license.setUser(null); //
        license.setProduct(product);
        license.setType(licenseType);
        license.setFirstActivationDate(null);
        license.setEndingDate(null);
        license.setIsBlocked(false);
        license.setDevicesCount(licenseRequest.getDeviceCount());
        license.setOwner(user); // Владелец лицензии
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

        User user = userService.findUserByLogin(login);
        if(user == null){
            throw new IllegalArgumentException("User not found");
        }


        if(license.getUser() != null){
            if(license.getUser().getId().equals(user.getId())){
                throw new IllegalArgumentException("Wrong user");
            }
        }

        // Validate license
        validateActivation(license, device, login);

        // Update license
        if(license.getFirstActivationDate() == null){
            updateLicenseForActivation(license, user); // TODO: 2 добавлена замена id владельца лицензии
        }

        // Create device license
        createDeviceLicense(license, device);


        // Save license history
        LicenseHistory licenseHistory = new LicenseHistory(license, license.getOwner(), "ACTIVATED", new Date(), "License activated");
        licenseHistoryService.saveLicenseHistory(licenseHistory);

        // Generate ticket
        return generateTicket(license, device);
    }

    /// License finding for the device
    public License getActiveLicenseForDevice(Device device, User user, String code) {
        License license = licenseRepository.getLicensesByCode(code);

        if(license == null){
            throw new IllegalArgumentException("License not found");
        }

        DeviceLicense deviceLicense = deviceLicenseService.getDeviceLicenseByDeviceIdAndLicenseId(device.getId(), license.getId());

        if(deviceLicense == null){
            throw new IllegalArgumentException("License for this device not found");
        }

        if (license.getIsBlocked()){
            throw new IllegalArgumentException("License is blocked");
        }

        return license;
    }

    /// License updating

    public Ticket updateExistentLicense(String licenseCode, String login, String macAddress){

        // TODO refactor throws to failure tickets (if rly needed wtf)

        // Find license
        License license = licenseRepository.getLicensesByCode(licenseCode);
        if(license == null){
            throw new IllegalArgumentException("License not found");
        }

        // Validate license
        if(license.getIsBlocked()){
            throw new IllegalArgumentException("License is blocked");
        }

        if(license.getFirstActivationDate() == null){
            throw new IllegalArgumentException("License is not activated");
        }

        // Update license date
        license.setEndingDate(new Date(license.getEndingDate().getTime() + license.getDuration()));
        licenseRepository.save(license);

        // Save license history
        LicenseHistory licenseHistory = new LicenseHistory(license, license.getOwner(), "UPDATED", new Date(), "License updated");
        licenseHistoryService.saveLicenseHistory(licenseHistory);

        // Generate ticket
        return generateTicket(license, deviceRepository.findDeviceByMacAddress(macAddress));
    }


    // Other methods

    public Ticket generateTicket(License license, Device device){
        Ticket ticket = new Ticket();

        ticket.setCurrentDate(new Date());
        ticket.setLifetime(license.getDuration()); // Ticket life time, should be decreased to const int
        ticket.setActivationDate(new Date(license.getFirstActivationDate().getTime()));
        ticket.setExpirationDate(new Date(license.getEndingDate().getTime()));
        ticket.setUserId(license.getOwner().getId());
        ticket.setDeviceId(device.getId());
        ticket.setIsBlocked(false);
        ticket.setSignature(generateSignature(ticket));

        return ticket;
    }

    private void validateActivation(License license, Device device, String login) {

        // Is license blocked
        if (license.getIsBlocked()) {
            throw new IllegalArgumentException("License is blocked");
        }

        // Is license expired
        if(license.getEndingDate() != null) {
            if (license.getEndingDate().before(new Date())) {
                throw new IllegalArgumentException("License is expired");
            }
        }

        // Is device count exceeded
        if (license.getDevicesCount() <= deviceLicenseService.getDeviceLicensesByLicense(license).size()) {
            throw new IllegalArgumentException("Device count exceeded");
        }
    }

    private void createDeviceLicense(License license, Device device) {
        DeviceLicense deviceLicense = new DeviceLicense();
        deviceLicense.setDevice(device);
        deviceLicense.setLicense(license);
        deviceLicense.setActivationDate(new Date());

        deviceLicenseService.save(deviceLicense);
    }

    private void updateLicenseForActivation(License license, User user) {
        license.setFirstActivationDate(new Date());
        license.setEndingDate(new Date(System.currentTimeMillis() + license.getDuration()));
        license.setUser(user);
        licenseRepository.save(license);
    }

    public String generateSignature(Ticket ticket){
        String signature = passwordEncoder.encode(ticket.getBodyForSigning()); // TODO: 1 добавлена адекватная подпись через PasswordEncoder

        // String body_ticket = ticket.getBodyForSigning();
        // System.out.println("Signature matches: " + passwordEncoder.matches(body_ticket, signature));

        return signature;
    }

    private String generateLicenseCode(LicenseRequest licenseRequest){
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            String data = licenseRequest.getProductId() + licenseRequest.getUserId() + licenseRequest.getLicenseTypeId() + licenseRequest.getDeviceCount() + licenseRequest.getDuration() + licenseRequest.getDescription() + LocalDateTime.now();
            byte[] hash = digest.digest(data.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error generating license code", e);
        }

        // TODO: implement / refactor license code generation...
    }

    public License getLicenseById(Long id) {
        return licenseRepository.getLicenseById(id);
    }
}
