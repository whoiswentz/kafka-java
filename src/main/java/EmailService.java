import kafka.KafkaService;
import org.apache.kafka.clients.consumer.ConsumerRecord;

public class EmailService {
    public static void main(String[] args) {
        final var groupId = EmailService.class.getName();
        final var topic = "ECOMMERCE_SEND_EMAIL";

        try (final var service = new KafkaService(groupId, topic, EmailService::parse)) {
            service.run();
        }
    }

    private static void parse(ConsumerRecord<String, String> r) {
        System.out.println("Processing Email");
        System.out.println("Email: {" +
                " topic: " + r.topic() +
                " partition: " + r.partition() +
                " offset: " + r.offset() +
                " timestamp: " + r.timestamp() +
                " }");
        System.out.println("Email send");
    }


}
