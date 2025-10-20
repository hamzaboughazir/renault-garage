package fr.renault.domain.model;

import lombok.*;

import java.time.DayOfWeek;
import java.util.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Garage {
    private UUID id;
    private String name;
    private String address;
    private String telephone;
    private String email;
    @Builder.Default
    private Map<DayOfWeek, List<OpeningTime>> openingHours = new HashMap<>();
    @Builder.Default
    private List<Vehicle> vehicles = new ArrayList<>();
}
