package ru.mtuci.antivirus.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.mtuci.antivirus.entities.*;
import ru.mtuci.antivirus.entities.DTO.LicenseRequest;
import ru.mtuci.antivirus.repositories.LicenseRepository;

@Service
public class LicenseService{

    private final LicenseRepository licenseRepository;
    private final ProductService productService;
    private final UserService userService;
    private final LicenseTypeService licenseTypeService;
    private final LicenseHistoryService licenseHistoryService;

    @Autowired
    public LicenseService(LicenseRepository licenseRepository, ProductService productService, UserService userService, LicenseTypeService licenseTypeService, LicenseHistoryService licenseHistoryService) {
        this.licenseRepository = licenseRepository;
        this.productService = productService;
        this.userService = userService;
        this.licenseTypeService = licenseTypeService;
        this.licenseHistoryService = licenseHistoryService;
    }

    public License createLicense(LicenseRequest licenseRequest){
        Product product = productService.getProductById(licenseRequest.getProductId());
        if(product == null){
            System.err.println("LicenseService: createLicense: product not found");
            return null;
        }

        User user = userService.findUserById(licenseRequest.getOwnerId());
        if(user == null){
            System.err.println("LicenseService: createLicense: user not found");
            return null;
        }

        LicenseType licenseType = licenseTypeService.getLicenseTypeById(licenseRequest.getLicenseTypeId());
        if(licenseType == null){
            System.err.println("LicenseService: createLicense: license type not found");
            return null;
        }

        String code = generateLicenseCode();

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

        LicenseHistory licenseHistory = new LicenseHistory(license, user, "CREATED", null, "License created");
        licenseHistoryService.saveLicenseHistory(licenseHistory);

        return license;
    }

    // Other methods

    private String generateLicenseCode(){
        return "1234567890";
        /// TODO: implement license code generation
    }
}
