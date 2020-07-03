package io.wentz;

import io.wentz.dispatcher.KafkaDispatcher;
import io.wentz.ingester.KafkaIngester;
import io.wentz.models.User;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class BatchSendMessageService {
    private static final KafkaDispatcher<User> userDispatcher = new KafkaDispatcher<>();
    private static final String klass = BatchSendMessageService.class.getName();
    private static Connection connection;

    public static void main(String[] args) throws SQLException, ExecutionException, InterruptedException {
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
        try (var ingester = new KafkaIngester<>(klass,
                "ECOMMERCE_SEND_MESSAGE_TO_ALL_USERS",
                BatchSendMessageService::parse,
                Map.of())
        ) {
            ingester.run();
        }
    }

    private static void parse(ConsumerRecord<String, Message<String>> r) throws SQLException, ExecutionException, InterruptedException {
        String className = BatchSendMessageService.class.getSimpleName();

        for (User user : getAllUsers()) {
            var message = r.value();
            userDispatcher.sendAsync(
                    message.getPayload(),
                    user.getUUID(),
                    message.getId().continueWith(className),
                    user);
            System.out.println("I Think that I send");
        }
    }

    private static List<User> getAllUsers() throws SQLException {
        ResultSet result = connection
                .prepareStatement("SELECT uuid FROM users")
                .executeQuery();

        List<User> users = new ArrayList<>();
        while (result.next()) {
            users.add(new User(result.getString(1)));
        }

        return users;
    }
}
