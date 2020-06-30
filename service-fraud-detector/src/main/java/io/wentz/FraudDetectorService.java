package io.wentz;

import io.wentz.models.Order;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.util.Map;
import java.util.concurrent.ExecutionException;

public class FraudDetectorService {
    private static final String klass = FraudDetectorService.class.getName();
    private static final String newOrderTopic = "ECOMMERCE_NEW_ORDER";
    private static final String rejectedOrderTopic = "ECOMMERCE_ORDER_REJECTED";
    private static final String approvedOrderTopic = "ECOMMERCE_ORDER_APPROVED";
    private static final KafkaIngester<Order> ingester = new KafkaIngester<>(
            klass,
            newOrderTopic,
            FraudDetectorService::parse,
            Map.of());
    private static final KafkaDispatcher<Order> dispatcher = new KafkaDispatcher<>();

    public static void main(String[] args) {
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
