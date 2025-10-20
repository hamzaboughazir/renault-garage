package fr.renault.infrastructure.persistence;

import fr.renault.domain.model.FuelType;
import fr.renault.domain.model.Garage;
import fr.renault.domain.ports.GarageRepository;
import fr.renault.infrastructure.persistence.jpa.entity.GarageEntity;
import fr.renault.infrastructure.persistence.jpa.repo.GarageJpaRepository;
import fr.renault.infrastructure.persistence.mapper.GarageMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class GarageRepositoryAdapter implements GarageRepository {

    private final GarageJpaRepository jpaRepository;

    @Override
    public Garage save(Garage garage) {
        GarageEntity saved = jpaRepository.save(GarageMapper.toEntity(garage));
        return GarageMapper.toDomain(saved);
    }

    @Override
    public Optional<Garage> findById(UUID id) {
        return jpaRepository.findById(id).map(GarageMapper::toDomain);
    }

    @Override
    public void deleteById(UUID id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public List<Garage> findAll() {
        return jpaRepository.findAll().stream().map(GarageMapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<Garage> searchByFuelType(FuelType fuelType) {
        return findAll().stream()
                .filter(g -> g.getVehicles().stream().anyMatch(v -> v.getFuelType()==fuelType))
                .collect(Collectors.toList());
    }

    @Override
    public List<Garage> searchByAccessoryName(String accessoryName) {
        String an = accessoryName.toLowerCase();
        return findAll().stream()
                .filter(g -> g.getVehicles().stream()
                        .anyMatch(v -> v.getAccessories().stream()
                                .anyMatch(a -> a.getName()!=null && a.getName().toLowerCase().contains(an))))
                .collect(Collectors.toList());
    }
}
