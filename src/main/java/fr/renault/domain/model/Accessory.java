package fr.renault.domain.model;

import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Accessory {
    private UUID id;
    private String name;
    private String description;
    private BigDecimal price;
    private AccessoryType type;
}
