import kafka.KafkaDispatcher;

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
            for (var i = 0; i < 10; i++) {
                final var userId = UUID.randomUUID().toString();
                final var orderId = UUID.randomUUID().toString();
                final var amount = BigDecimal.valueOf(Math.random() * 5000 + 1);

                final var order = new Order(userId, orderId, amount);
                final var email = new Email(
                        "New Order",
                        "Thank, you for order, we are processing your order"
                );

                try {
                    orderDispatcher.send(NEW_ORDER_TOPIC, userId, order);
                    emailDispatcher.send(EMAIL_ORDER_TOPIC, userId, email);
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
