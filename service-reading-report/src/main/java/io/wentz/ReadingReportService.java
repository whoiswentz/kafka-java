package io.wentz;

import io.wentz.models.Order;
import io.wentz.models.User;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;

public class ReadingReportService {
    private static final String klass = ReadingReportService.class.getName();
    private static final KafkaIngester<User> ingester = new KafkaIngester<>(
            klass,
            "USER_GENERATE_READING_REPORT",
            ReadingReportService::parse,
            User.class,
            Map.of());

    private static final Path SOURCE = new File("src/main/resources/report.txt").toPath();

    public static void main(String[] args) {
        ingester.run();
    }

    private static void parse(ConsumerRecord<String, User> r) throws IOException {
        System.out.println(r.value());

        var user = r.value();
        var target = new File(user.getReportPath());
        IO.copy(SOURCE, target);
        IO.append(target, "Created for " + user.getUUID());

        System.out.println("File created: " + target.getAbsolutePath());
    }
}
