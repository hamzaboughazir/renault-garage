package fr.renault.infrastructure.persistence.jpa.entity;

import fr.renault.domain.model.AccessoryType;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity @Table(name = "accessories")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AccessoryEntity {
    @Id
    private java.util.UUID id;
    private String name;
    private String description;
    private BigDecimal price;
    @Enumerated(EnumType.STRING)
    private AccessoryType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id")
    private VehicleEntity vehicle;
}
