import io.wentz.Message;
import io.wentz.dispatcher.KafkaDispatcher;
import io.wentz.ingester.KafkaIngester;
import models.Order;
import org.apache.kafka.clients.consumer.ConsumerRecord;


import java.util.Map;
import java.util.concurrent.ExecutionException;

public class PrepateEmailNewOrderService {
    private static final String klass = PrepateEmailNewOrderService.class.getName();

    private static final KafkaDispatcher<String> dispatcher = new KafkaDispatcher<>();

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        final var ingester = new KafkaIngester<>(
                klass,
                "ECOMMERCE_NEW_ORDER",
                PrepateEmailNewOrderService::parse,
                Map.of());
        ingester.run();
    }

    private static void parse(ConsumerRecord<String, Message<Order>> r) throws ExecutionException, InterruptedException {
        var order = r.value().getPayload();
        var emailCode = "New order email";

        dispatcher.send("ECOMMERCE_SEND_EMAIL",
                order.getEmail(),
                r.value().getId().continueWith(PrepateEmailNewOrderService.class.getName()),
                emailCode);
    }
}
