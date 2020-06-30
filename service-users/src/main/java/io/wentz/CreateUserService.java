package io.wentz;

import io.wentz.models.Order;
import io.wentz.models.User;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;
import java.util.UUID;

public class CreateUserService {
    private static final String klass = CreateUserService.class.getName();
    private static final String newOrderTopic = "ECOMMERCE_NEW_ORDER";
    private static Connection connection;

    public static void main(String[] args) throws SQLException {
        String url = "jdbc:sqlite:target/users_database.db";
        connection = DriverManager.getConnection(url);

        try {
            connection.createStatement()
                    .execute("create table users (" +
                            "uuid varchar(200) primary key," +
                            "email varchar(200))"
                    );
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        try (var ingester = new KafkaIngester<>(klass, newOrderTopic, CreateUserService::parse, Order.class, Map.of())) {
            ingester.run();
        }
    }

    private static void parse(ConsumerRecord<String, Message<Order>> r) throws SQLException {
        System.out.println(r.value().toString());

        Order order = r.value().getPayload();
        if (isNewUser(order.getUserEmail())) {
            insertUser(order.getUserEmail());
        }
    }

    private static void insertUser(String email) throws SQLException {
        var insert = connection.prepareStatement("insert into users (uuid, email) values (?, ?)");
        insert.setString(1, UUID.randomUUID().toString());
        insert.setString(2, email);
        insert.execute();
    }

    private static boolean isNewUser(String email) throws SQLException {
        var exists = connection.prepareStatement("select uuid from users where email = ? limit 1");
        exists.setString(1, email);
        var result = exists.executeQuery();
        return !result.next();
    }
}
