import kafka.KafkaService;
import org.apache.kafka.clients.consumer.ConsumerRecord;

public class FraudDetectorService {
    public static void main(String[] args) {
        final var service = new KafkaService(
                FraudDetectorService.class.getName(),
                "ECOMMERCE_NEW_ORDER",
                FraudDetectorService::parse);
        service.run();
    }

    private static void parse(ConsumerRecord<String, String> r) {
        System.out.println("Processing new order");
        System.out.println("Order: {" +
                " topic: " + r.topic() +
                " partition: " + r.partition() +
                " offset: " + r.offset() +
                " timestamp: " + r.timestamp() +
                " }");
        System.out.println("Order processed");
    }
}
