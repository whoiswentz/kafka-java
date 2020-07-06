package io.wentz;

import io.wentz.dispatcher.KafkaDispatcher;
import io.wentz.models.Order;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.sql.SQLException;

public class FraudDetectorService implements ConsumerService<Order> {
    private static final String rejectedOrderTopic = "ECOMMERCE_ORDER_REJECTED";
    private static final String approvedOrderTopic = "ECOMMERCE_ORDER_APPROVED";
    private static final KafkaDispatcher<Order> dispatcher = new KafkaDispatcher<>();
    private final LocalDatabase db;

    public FraudDetectorService() throws SQLException {
        this.db = new LocalDatabase("frauds_database");
        boolean created = this.db.createIfNotExists("create table Orders (" +
                "uuid varchar(200) primary key," +
                "is_fraud boolean)");
        System.out.println("DB created? " + created);
        System.out.println("Constructed");
    }

    public void parse(ConsumerRecord<String, Message<Order>> r) throws SQLException {
        System.out.println("Verify");
        var message = r.value();
        var order = message.getPayload();

        String className = FraudDetectorService.class.getSimpleName();

        if (wasProcessed(order)) {
            System.out.println("Order already processed");
            return;
        }

        if (order.isFraud()) {
            var inserted = db.update("insert into Orders (uuid, is_fraud) values (?, true)", order.getOrderId());
            System.out.println("Order deceted as fraud, and inserted into db: " + inserted);
            dispatcher.sendAsync(rejectedOrderTopic, order.getEmail(), message.getId().continueWith(className), order);
            return;
        }
        db.update("insert into Orders (uuid, is_fraud) values (?, true)", order.getOrderId());
        dispatcher.sendAsync(approvedOrderTopic, order.getEmail(), message.getId().continueWith(className), order);
    }

    private boolean wasProcessed(Order order) throws SQLException {
        var result = db.query("select uuid from Orders where uuid = ? limit 1", order.getOrderId());
        return result.next();
    }

    @Override
    public String getTopic() {
        return "ECOMMERCE_NEW_ORDER";
    }

    @Override
    public String getConsumerGroup() {
        return FraudDetectorService.class.getSimpleName();
    }
}
