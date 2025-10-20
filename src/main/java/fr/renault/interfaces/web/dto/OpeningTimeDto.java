package fr.renault.interfaces.web.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalTime;

public record OpeningTimeDto(@NotNull LocalTime startTime, @NotNull LocalTime endTime) {}
