package io.wentz;

import io.wentz.dispatcher.KafkaDispatcher;
import io.wentz.ingester.KafkaIngester;
import io.wentz.models.Order;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.util.Map;
import java.util.concurrent.ExecutionException;

public class FraudDetectorService {
    private static final String klass = FraudDetectorService.class.getName();
    private static final String newOrderTopic = "ECOMMERCE_NEW_ORDER";
    private static final String rejectedOrderTopic = "ECOMMERCE_ORDER_REJECTED";
    private static final String approvedOrderTopic = "ECOMMERCE_ORDER_APPROVED";
    private static final KafkaDispatcher<Order> dispatcher = new KafkaDispatcher<>();
    private static final KafkaIngester<Order> ingester = new KafkaIngester<>(
            klass,
            newOrderTopic,
            FraudDetectorService::parse,
            Map.of());

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ingester.run();
    }

    private static void parse(ConsumerRecord<String, Message<Order>> r) throws ExecutionException, InterruptedException {
        String className = FraudDetectorService.class.getSimpleName();

        var message = r.value();
        var order = message.getPayload();
        if (order.isFraud()) {
            dispatcher.send(
                    rejectedOrderTopic,
                    order.getEmail(),
                    message.getId().continueWith(className),
                    order);
            return;
        }
        dispatcher.send(
                approvedOrderTopic,
                order.getEmail(),
                message.getId().continueWith(className),
                order);
    }
}
