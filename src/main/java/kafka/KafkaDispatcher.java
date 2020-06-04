package kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;

import java.io.Closeable;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class KafkaDispatcher implements Closeable {
    private final KafkaProducer<String, String> producer;

    public KafkaDispatcher() {
        this.producer = new KafkaProducer<>(properties());
    }

    private static Properties properties() {
        final Properties properties = new Properties();

        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        return properties;
    }

    public void send(String topic, String id, String value) throws ExecutionException, InterruptedException {
        final var orderRecord = new ProducerRecord<>(topic, id, value);
        final var emailRecord = new ProducerRecord<>(topic, id, value);

        producer.send(orderRecord, KafkaDispatcher::onCompletion).get();
        producer.send(emailRecord, KafkaDispatcher::onCompletion).get();
    }

    private static void onCompletion(RecordMetadata data, Exception e) {
        if (e != null) {
            e.printStackTrace();
            return;
        }

        System.out.println("Message: {" +
                " topic: " + data.topic() +
                " partition: " + data.partition() +
                " offset: " + data.offset() +
                " timestamp: " + data.timestamp() +
                " }"
        );
    }

    @Override
    public void close() {
        producer.close();
    }
}
