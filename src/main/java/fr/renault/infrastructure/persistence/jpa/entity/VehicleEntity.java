package fr.renault.infrastructure.persistence.jpa.entity;

import fr.renault.domain.model.FuelType;
import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name = "vehicles")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class VehicleEntity {
    @Id
    private java.util.UUID id;
    private String brand;
    private String model;
    private int yearOfManufacture;
    @Enumerated(EnumType.STRING)
    private FuelType fuelType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "garage_id")
    private GarageEntity garage;

    @OneToMany(mappedBy = "vehicle", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private java.util.List<AccessoryEntity> accessories = new java.util.ArrayList<>();
}
