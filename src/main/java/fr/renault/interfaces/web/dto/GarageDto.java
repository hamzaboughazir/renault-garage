package fr.renault.interfaces.web.dto;

import jakarta.validation.constraints.*;
import java.time.DayOfWeek;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public record GarageDto(
        UUID id,
        @NotBlank String name,
        @NotBlank String address,
        @NotBlank String telephone,
        @Email String email,
        Map<DayOfWeek, List<OpeningTimeDto>> openingHours,
        List<VehicleDto> vehicles
) {}
