package fr.renault.domain.model;

import lombok.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class OpeningTime {
    @NotNull
    private LocalTime startTime;
    @NotNull
    private LocalTime endTime;
}
