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

//    @NotBlank(message = "Поле productId не должно быть пустым.")
    private Long productId;

//    @NotBlank(message = "Поле ownerId не должно быть пустым.")
    private Long ownerId;

//    @NotBlank(message = "Поле licenseTypeId не должно быть пустым.")
    private Long licenseTypeId;

    @NotBlank(message = "Поле description не должно быть пустым.")
    private String description;

//    @NotBlank(message = "Поле deviceCount не должно быть пустым.")
    private Integer deviceCount;

//    @NotBlank(message = "Поле duration не должно быть пустым.")
    private Integer duration;

    // Getters and Setters
}
