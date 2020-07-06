package io.wentz;

import io.wentz.ingester.KafkaIngester;

import java.util.Map;
import java.util.concurrent.Callable;

public class ServiceProvider<T> implements Callable<Void> {
    private final ServiceFactory<T> factory;

    public ServiceProvider(ServiceFactory<T> service) {
        this.factory = service;
    }

    public Void call() throws Exception {
        final var service = factory.create();
        final var groupId = service.getConsumerGroup();
        final var topic = service.getTopic();

        try (final var consumer = new KafkaIngester<>(groupId, topic, service::parse, Map.of())) {
            consumer.run();
        }
        return null;
    }
}
