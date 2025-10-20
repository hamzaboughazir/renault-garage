package fr.renault.infrastructure.persistence.jpa.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.*;

@Entity @Table(name = "garages")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class GarageEntity {
    @Id
    private java.util.UUID id;
    private String name;
    private String address;
    private String telephone;
    private String email;

    @ElementCollection
    @CollectionTable(name = "opening_hours", joinColumns = @JoinColumn(name = "garage_id"))
    private java.util.List<OpeningTimeEmbeddable> openingHours = new ArrayList<>();

    @OneToMany(mappedBy = "garage", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private java.util.List<VehicleEntity> vehicles = new ArrayList<>();
}
