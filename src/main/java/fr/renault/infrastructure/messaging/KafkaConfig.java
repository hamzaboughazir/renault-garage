package fr.renault.infrastructure.messaging;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaConfig {
    @Bean
    public NewTopic vehicleCreatedTopic(@Value("${app.kafka.topics.vehicle-created:vehicle-created}") String topic) {
        return new NewTopic(topic, 1, (short)1);
    }
}
