package io.wentz;

import io.wentz.models.Email;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.util.Map;

public class EmailService {
    public static void main(String[] args) {
        final var groupId = EmailService.class.getName();
        final var topic = "ECOMMERCE_SEND_EMAIL";

        try (final var service = new KafkaIngester<>(groupId, topic, EmailService::parse, Email.class, Map.of())) {
            service.run();
        }
    }

    private static void parse(ConsumerRecord<String, String> r) {
        System.out.println("Processing io.wentz.models.Email");
        System.out.println("io.wentz.models.Email: {" +
                " topic: " + r.topic() +
                " partition: " + r.partition() +
                " offset: " + r.offset() +
                " timestamp: " + r.timestamp() +
                " }");
        System.out.println("io.wentz.models.Email send");
    }


}
