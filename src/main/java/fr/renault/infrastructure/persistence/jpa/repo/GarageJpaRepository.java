package fr.renault.infrastructure.persistence.jpa.repo;

import fr.renault.infrastructure.persistence.jpa.entity.GarageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface GarageJpaRepository extends JpaRepository<GarageEntity, UUID> {
}
