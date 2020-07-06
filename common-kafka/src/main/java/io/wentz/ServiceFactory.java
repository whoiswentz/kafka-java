package io.wentz;

public interface ServiceFactory<T> {
    ConsumerService<T> create() throws Exception;
}
