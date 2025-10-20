package fr.renault.application;

import fr.renault.domain.model.Vehicle;

import java.util.UUID;

public interface VehiclePublisher {
    void publishVehicleCreated(UUID garageId, Vehicle vehicle);
}
