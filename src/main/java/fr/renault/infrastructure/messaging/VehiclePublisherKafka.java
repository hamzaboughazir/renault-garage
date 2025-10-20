package fr.renault.infrastructure.messaging;

import fr.renault.application.VehiclePublisher;
import fr.renault.domain.model.Vehicle;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class VehiclePublisherKafka implements VehiclePublisher {

    private final KafkaTemplate<String, VehicleEvent> template;

    @Value("${app.kafka.topics.vehicle-created:vehicle-created}")
    private String topic;

    @Override
    public void publishVehicleCreated(UUID garageId, Vehicle v) {
        VehicleEvent evt = new VehicleEvent(v.getId(), garageId, v.getBrand(), v.getModel(), v.getYearOfManufacture(), v.getFuelType());
        template.send(topic, v.getId().toString(), evt);
    }
}
