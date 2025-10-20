package fr.renault.infrastructure.messaging;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class VehicleCreatedConsumer {

    @KafkaListener(topics = "#{@vehicleCreatedTopic.name()}", groupId = "renault_garage_", containerFactory = "kafkaListenerContainerFactory")
    public void onVehicleCreated(VehicleEvent evt) {
        log.info("Vehicle created consumed: {}", evt);
    }
}
