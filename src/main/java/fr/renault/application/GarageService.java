package fr.renault.application;

import fr.renault.domain.model.*;
import fr.renault.domain.ports.GarageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class GarageService {
    private static final int MAX_VEHICLES_PER_GARAGE = 50;
    private final GarageRepository garageRepository;
    private final VehiclePublisher vehiclePublisher;

    public List<Garage> listAll() {
        return garageRepository.findAll();
    }

    public Optional<Garage> get(UUID id) {
        return garageRepository.findById(id);
    }

    public Garage create(Garage garage) {
        if (garage.getId() == null) garage.setId(UUID.randomUUID());
        return garageRepository.save(garage);
    }

    public Garage update(UUID id, Garage update) {
        Garage existing = garageRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Garage not found: " + id));
        existing.setName(update.getName());
        existing.setAddress(update.getAddress());
        existing.setTelephone(update.getTelephone());
        existing.setEmail(update.getEmail());
        existing.setOpeningHours(update.getOpeningHours());
        return garageRepository.save(existing);
    }

    public void delete(UUID id) {
        garageRepository.deleteById(id);
    }

    @Transactional
    public Vehicle addVehicle(UUID garageId, Vehicle vehicle) {
        Garage garage = garageRepository.findById(garageId)
                .orElseThrow(() -> new NoSuchElementException("Garage not found: " + garageId));
        if (garage.getVehicles().size() >= MAX_VEHICLES_PER_GARAGE) {
            throw new IllegalStateException("Quota de 50 véhicules atteint pour ce garage");
        }
        if (vehicle.getId() == null) vehicle.setId(UUID.randomUUID());
        garage.getVehicles().add(vehicle);
        garageRepository.save(garage);
        vehiclePublisher.publishVehicleCreated(garageId, vehicle);
        return vehicle;
    }

    public void removeVehicle(UUID garageId, UUID vehicleId) {
        Garage garage = garageRepository.findById(garageId)
                .orElseThrow(() -> new NoSuchElementException("Garage not found: " + garageId));
        boolean removed = garage.getVehicles().removeIf(v -> v.getId().equals(vehicleId));
        if (!removed) throw new NoSuchElementException("Vehicle not found: " + vehicleId);
        garageRepository.save(garage);
    }

    public List<Vehicle> listVehicles(UUID garageId) {
        Garage garage = garageRepository.findById(garageId)
                .orElseThrow(() -> new NoSuchElementException("Garage not found: " + garageId));
        return garage.getVehicles();
    }

    public List<Vehicle> listVehiclesByModel(String model) {
        List<Vehicle> all = new ArrayList<>();
        for (Garage g : garageRepository.findAll()) {
            for (Vehicle v : g.getVehicles()) {
                if (v.getModel() != null && v.getModel().equalsIgnoreCase(model)) {
                    all.add(v);
                }
            }
        }
        return all;
    }

    public List<Garage> searchByFuelType(FuelType fuelType) {
        return garageRepository.searchByFuelType(fuelType);
    }

    public List<Garage> searchByAccessoryName(String accessoryName) {
        return garageRepository.searchByAccessoryName(accessoryName);
    }
}
