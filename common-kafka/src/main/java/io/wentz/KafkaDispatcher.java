package io.wentz;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;

import java.io.Closeable;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class KafkaDispatcher<T> implements Closeable {
    private final KafkaProducer<String, Message<T>> producer;

    public KafkaDispatcher() {
        this.producer = new KafkaProducer<>(properties());
    }

    private static Properties properties() {
        final Properties properties = new Properties();

        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        properties.setProperty(ProducerConfig.ACKS_CONFIG, "all");
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, GsonSerializer.class.getName());

        return properties;
    }

    private static void onCompletion(RecordMetadata data, Exception e) {
        if (e != null) {
            e.printStackTrace();
        }
        System.out.println("Send " + data.topic() + ":::partition " + data.partition() + "/offset " + data.offset() + "/timestamp " + data.timestamp());
    }

    public void send(String topic, String key, CorrelationId id, T payload) throws ExecutionException, InterruptedException {
        final var message = new Message<>(id, payload);
        final var record = new ProducerRecord<>(topic, key, message);
        producer.send(record, KafkaDispatcher::onCompletion).get();
    }

    @Override
    public void close() {
        producer.close();
    }
}
