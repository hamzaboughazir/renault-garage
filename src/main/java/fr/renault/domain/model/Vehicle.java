package fr.renault.domain.model;

import lombok.*;

import java.util.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Vehicle {
    private UUID id;
    private String brand;
    private String model;
    private int yearOfManufacture;
    private FuelType fuelType;
    @Builder.Default
    private List<Accessory> accessories = new ArrayList<>();
}
