package fr.renault.interfaces.web.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.util.UUID;

public record AccessoryDto(
        UUID id,
        @NotBlank String name,
        String description,
        @NotNull @PositiveOrZero BigDecimal price,
        @NotBlank String type
) {}
