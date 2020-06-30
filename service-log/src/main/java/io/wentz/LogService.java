package io.wentz;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.Map;
import java.util.regex.Pattern;

public class LogService {
    public static void main(String[] args) {
        final var service = new KafkaIngester(
                LogService.class.getName(),
                Pattern.compile("ECOMMERCE.*"),
                LogService::parse,
                Map.of(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName()));
        service.run();
    }

    private static void parse(ConsumerRecord<String, Message<String>> r) {
        System.out.println("LOG: {" +
                " topic: " + r.topic() +
                " partition: " + r.partition() +
                " offset: " + r.offset() +
                " timestamp: " + r.timestamp() +
                " value: " + r.value()+
                " }");
    }
}
