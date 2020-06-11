package io.wentz;

import io.wentz.models.Order;
import kafka.KafkaIngester;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.util.Map;

public class FraudDetectorService {
    public static void main(String[] args) {
        final var service = new KafkaIngester<>(
                FraudDetectorService.class.getName(),
                "ECOMMERCE_NEW_ORDER",
                FraudDetectorService::parse,
                Order.class,
                Map.of());
        service.run();
    }

    private static void parse(ConsumerRecord<String, Order> r) {
        System.out.println("Processing new order");
        System.out.println("io.wentz.models.Order: {" +
                " topic: " + r.topic() +
                " partition: " + r.partition() +
                " offset: " + r.offset() +
                " timestamp: " + r.timestamp() +
                " value: " + r.value() +
                " }");
        System.out.println("io.wentz.models.Order processed");
    }
}
