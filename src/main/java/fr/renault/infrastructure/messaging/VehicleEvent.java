package fr.renault.infrastructure.messaging;

import fr.renault.domain.model.FuelType;
import java.util.UUID;

public record VehicleEvent(UUID vehicleId, UUID garageId, String brand, String model, int year, FuelType fuelType) {}
