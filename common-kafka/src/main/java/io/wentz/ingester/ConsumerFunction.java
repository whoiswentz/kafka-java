package io.wentz.ingester;

import io.wentz.Message;
import org.apache.kafka.clients.consumer.ConsumerRecord;

public interface ConsumerFunction<T> {
    void consume(ConsumerRecord<String, Message<T>> r) throws Exception;
}
