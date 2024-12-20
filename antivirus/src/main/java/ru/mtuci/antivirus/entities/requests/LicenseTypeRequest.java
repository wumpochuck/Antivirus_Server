package ru.mtuci.antivirus.entities.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LicenseTypeRequest {

    @NotBlank(message = "Name cannot be empty")
    private String name;

    @NotNull
    private int defaultDuration;

    @NotBlank(message = "Description cannot be empty")
    private String description;
}

