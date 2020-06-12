package io.wentz;

import io.wentz.models.Email;
import io.wentz.models.Order;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class NewOrderMain {
    private static final String NEW_ORDER_TOPIC = "ECOMMERCE_NEW_ORDER";
    private static final String EMAIL_ORDER_TOPIC = "ECOMMERCE_SEND_EMAIL";

    public static void main(String[] args) {
        try (
                final var orderDispatcher = new KafkaDispatcher<Order>();
                final var emailDispatcher = new KafkaDispatcher<Email>()
        ) {
            final var userEmail = Math.random() + "@email.com";
            for (var i = 0; i < 10; i++) {
                final var orderId = UUID.randomUUID().toString();
                final var amount = BigDecimal.valueOf(Math.random() * 5000 + 1);

                final var order = new Order(orderId, amount, userEmail);
                final var email = new Email("New Order", "Thank");

                try {
                    orderDispatcher.send(NEW_ORDER_TOPIC, userEmail, order);
                    emailDispatcher.send(EMAIL_ORDER_TOPIC, userEmail, email);
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
