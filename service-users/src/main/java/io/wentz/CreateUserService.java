package io.wentz;

import io.wentz.models.Order;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.sql.SQLException;
import java.util.UUID;

class CreateUserServiceMain {
    public static void main(String[] args) {
        new ServiceRunner<>(CreateUserService::new).start(3);
    }
}

public class CreateUserService implements ConsumerService<Order> {

    private final LocalDatabase db;

    public CreateUserService() throws SQLException {
        this.db = new LocalDatabase("users_database");
        this.db.createIfNotExists("create table Users (" +
                "uuid varchar(200) primary key" +
                "email varchar(200))");
    }

    @Override
    public void parse(ConsumerRecord<String, Message<Order>> r) throws SQLException {
        System.out.println(r.value().toString());

        Order order = r.value().getPayload();
        if (isNewUser(order.getUserEmail())) {
            System.out.println("Inserting new user");
            insertUser(order.getUserEmail());
            return;
        }
        System.out.println("This user already exists");
    }

    @Override
    public String getTopic() {
        return "ECOMMERCE_NEW_ORDER";
    }

    @Override
    public String getConsumerGroup() {
        return CreateUserService.class.getSimpleName();
    }

    private void insertUser(String email) throws SQLException {
        var executed = db.update("insert into users (uuid, email) values (?, ?)",
                UUID.randomUUID().toString(),
                email);
        System.out.println(executed);
    }

    private boolean isNewUser(String email) throws SQLException {
        var result = db.query("select uuid from users where email = ? limit 1", email);
        return !result.next();
    }
}
