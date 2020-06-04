import kafka.KafkaDispatcher;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class NewOrderMain {
    private static final String NEW_ORDER_TOPIC = "ECOMMERCE_NEW_ORDER";
    private static final String EMAIL_ORDER_TOPIC = "ECOMMERCE_SEND_EMAIL";

    public static void main(String[] args) {
        try(final var dispatcher = new KafkaDispatcher()) {
            for (var i = 0; i < 1000000; i++) {
                final var id = UUID.randomUUID().toString();

                var value = id + ",321321321,12334556";
                var email = "Thank, you for order, we are processing your order";

                try {
                    dispatcher.send(NEW_ORDER_TOPIC, id, value);
                    dispatcher.send(EMAIL_ORDER_TOPIC, id, email);
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
