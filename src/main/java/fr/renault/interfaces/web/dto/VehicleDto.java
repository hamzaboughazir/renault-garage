package fr.renault.interfaces.web.dto;

import jakarta.validation.constraints.*;
import java.util.List;
import java.util.UUID;

public record VehicleDto(
        UUID id,
        @NotBlank String brand,
        @NotBlank String model,
        @Min(1900) int yearOfManufacture,
        @NotBlank String fuelType,
        List<AccessoryDto> accessories
) {}
