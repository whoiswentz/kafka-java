package io.wentz;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.io.IOException;
import java.sql.SQLException;
import java.util.concurrent.ExecutionException;

public interface ConsumerService<T> {
    void parse(ConsumerRecord<String, Message<T>> r) throws IOException, ExecutionException, InterruptedException, SQLException;

    String getTopic();

    String getConsumerGroup();
}
