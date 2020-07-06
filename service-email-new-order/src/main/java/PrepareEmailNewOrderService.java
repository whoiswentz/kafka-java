import io.wentz.ConsumerService;
import io.wentz.Message;
import io.wentz.dispatcher.KafkaDispatcher;
import models.Order;
import org.apache.kafka.clients.consumer.ConsumerRecord;

public class PrepareEmailNewOrderService implements ConsumerService<Order> {
    private static final KafkaDispatcher<String> dispatcher = new KafkaDispatcher<>();

    public PrepareEmailNewOrderService() {
        System.out.println("Constructed");
    }

    public void parse(ConsumerRecord<String, Message<Order>> r) {
        System.out.println("Processing new order email");

        var order = r.value().getPayload();
        var emailCode = "New order email";

        dispatcher.sendAsync("ECOMMERCE_SEND_EMAIL",
                order.getEmail(),
                r.value().getId().continueWith(PrepareEmailNewOrderService.class.getSimpleName()),
                emailCode);
    }

    @Override
    public String getTopic() {
        return "ECOMMERCE_NEW_ORDER";
    }

    @Override
    public String getConsumerGroup() {
        return PrepareEmailNewOrderService.class.getSimpleName();
    }
}
