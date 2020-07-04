package io.wentz;

import io.wentz.dispatcher.KafkaDispatcher;
import io.wentz.models.Order;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class NewOrderMain {
    private static final String NEW_ORDER_TOPIC = "ECOMMERCE_NEW_ORDER";

    public static void main(String[] args) {
        try (
                final var orderDispatcher = new KafkaDispatcher<Order>();
        ) {
            final var userEmail = Math.random() + "@email.com";
            for (var i = 0; i < 10; i++) {
                final var orderId = UUID.randomUUID().toString();
                final var amount = BigDecimal.valueOf(Math.random() * 5000 + 1);

                final var order = new Order(orderId, amount, userEmail);

                try {
                    var id = new CorrelationId(NewOrderMain.class.getName());
                    orderDispatcher.send(NEW_ORDER_TOPIC, userEmail, id, order);
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
