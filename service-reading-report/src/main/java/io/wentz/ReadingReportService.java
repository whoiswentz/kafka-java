package io.wentz;

import io.wentz.models.User;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class ReadingReportService implements ConsumerService<User> {
    private static final Path SOURCE = new File("src/main/resources/report.txt").toPath();

    @Override
    public String getTopic() {
        return "ECOMMERCE_USER_GENERATE_READING_REPORT";
    }

    @Override
    public String getConsumerGroup() {
        return ReadingReportService.class.getSimpleName();
    }

    @Override
    public void parse(ConsumerRecord<String, Message<User>> r) throws IOException {
        System.out.println(r.value());

        var user = r.value().getPayload();
        var target = new File(user.getReportPath());
        IO.copy(SOURCE, target);
        IO.append(target, "Created for " + user.getUUID());

        System.out.println("File created: " + target.getAbsolutePath());
    }
}
