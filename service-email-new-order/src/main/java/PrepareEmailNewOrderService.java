import io.wentz.ConsumerService;
import io.wentz.Message;
import io.wentz.dispatcher.KafkaDispatcher;
import models.Order;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.util.concurrent.ExecutionException;

public class PrepareEmailNewOrderService implements ConsumerService<Order> {
    private static final KafkaDispatcher<String> dispatcher = new KafkaDispatcher<>();

    public void parse(ConsumerRecord<String, Message<Order>> r) throws ExecutionException, InterruptedException {
        var order = r.value().getPayload();
        var emailCode = "New order email";

        dispatcher.send("ECOMMERCE_SEND_EMAIL",
                order.getEmail(),
                r.value().getId().continueWith(PrepareEmailNewOrderService.class.getName()),
                emailCode);
    }

    @Override
    public String getTopic() {
        return "ECOMMERCE_NEW_ORDER";
    }

    @Override
    public String getConsumerGroup() {
        return PrepareEmailNewOrderService.class.getName();
    }
}
