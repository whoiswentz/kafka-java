package io.wentz;

import io.wentz.models.Email;
import org.apache.kafka.clients.consumer.ConsumerRecord;

public class EmailService implements ConsumerService<Email> {
    @Override
    public String getConsumerGroup() {
        return EmailService.class.getSimpleName();
    }

    @Override
    public String getTopic() {
        return "ECOMMERCE_SEND_EMAIL";
    }

    @Override
    public void parse(ConsumerRecord<String, Message<Email>> r) {
        System.out.println("Email: {" +
                " topic: " + r.topic() +
                " partition: " + r.partition() +
                " offset: " + r.offset() +
                " timestamp: " + r.timestamp() +
                " message: " + r.value().getPayload() +
                " }");
    }
}
