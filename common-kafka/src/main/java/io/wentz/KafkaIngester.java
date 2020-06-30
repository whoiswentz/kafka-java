package io.wentz;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.io.Closeable;
import java.time.Duration;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;


public class KafkaIngester<T> implements Closeable {
    private final KafkaConsumer<String, Message<T>> consumer;
    private final ConsumerFunction<T> parse;
    private Map<String, String> properties;

    private KafkaIngester(String groupId, ConsumerFunction<T> parse, Map<String, String> properties) {
        this.properties = properties;
        this.consumer = new KafkaConsumer<>(getProperties(groupId, properties));
        this.parse = parse;
    }

    public KafkaIngester(String groupId, String topic, ConsumerFunction<T> parse, Map<String, String> properties) {
        this(groupId, parse, properties);
        this.consumer.subscribe(Collections.singletonList(topic));
    }

    public KafkaIngester(String groupId, Pattern pattern, ConsumerFunction<T> parse, Map<String, String> properties) {
        this(groupId, parse, properties);
        this.consumer.subscribe(pattern);
    }

    public void run() {
        while (true) {
            var records = consumer.poll(Duration.ofMillis(100));
            if (records.isEmpty()) {
                continue;
            }
            for (ConsumerRecord<String, Message<T>> record : records) {
                try {
                    this.parse.consume(record);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private Properties getProperties(String groupId, Map<String, String> overrideProperties) {
        Properties properties = new Properties();

        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, GsonDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        properties.setProperty(ConsumerConfig.CLIENT_ID_CONFIG, UUID.randomUUID().toString());
        properties.setProperty(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, "1");
        properties.putAll(overrideProperties);
        return properties;
    }

    @Override
    public void close() {
        this.consumer.close();
    }
}
