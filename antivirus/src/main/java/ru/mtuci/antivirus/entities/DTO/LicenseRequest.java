package ru.mtuci.antivirus.entities.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LicenseRequest {

    @NotBlank(message = "productId cannot be empty.")
    private Long productId;

    @NotBlank(message = "ownerId cannot be empty.")
    private Long ownerId;

    @NotBlank(message = "licenseTypeId cannot be empty.")
    private Long licenseTypeId;

    @NotBlank(message = "description cannot be empty.")
    private String description;

    @NotBlank(message = "deviceCount cannot be empty.")
    private Integer deviceCount;

    @NotBlank(message = "duration cannot be empty.")
    private Integer duration;

}
