import kafka.KafkaService;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.util.regex.Pattern;

public class LogService {
    public static void main(String[] args) {
        final var service = new KafkaService(
                LogService.class.getName(),
                Pattern.compile("ECOMMERCE.*"),
                LogService::parse);
        service.run();
    }

    private static void parse(ConsumerRecord<String, String> r) {
        System.out.println("LOG: {" +
                " topic: " + r.topic() +
                " partition: " + r.partition() +
                " offset: " + r.offset() +
                " timestamp: " + r.timestamp() +
                " }");
    }
}
