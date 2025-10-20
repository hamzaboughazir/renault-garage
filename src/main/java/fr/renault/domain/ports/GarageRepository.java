package fr.renault.domain.ports;

import fr.renault.domain.model.Garage;
import fr.renault.domain.model.FuelType;

import java.util.*;

public interface GarageRepository {
    Garage save(Garage garage);
    Optional<Garage> findById(UUID id);
    void deleteById(UUID id);
    List<Garage> findAll();
    List<Garage> searchByFuelType(FuelType fuelType);
    List<Garage> searchByAccessoryName(String accessoryName);
}
