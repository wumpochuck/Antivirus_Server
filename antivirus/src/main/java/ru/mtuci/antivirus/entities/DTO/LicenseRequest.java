package ru.mtuci.antivirus.entities.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LicenseRequest {
    private Long productId;
    private Long ownerId;
    private Long licenseTypeId;
    private String description;
    private Integer deviceCount;
    private Integer duration;

    // Getters and Setters
}
