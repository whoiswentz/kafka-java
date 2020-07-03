package io.wentz.dispatcher;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.wentz.Message;
import io.wentz.MessageTypeAdapter;
import org.apache.kafka.common.serialization.Serializer;

public class GsonSerializer<T> implements Serializer<T> {
    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Message.class, new MessageTypeAdapter())
            .create();

    @Override
    public byte[] serialize(String s, T object) {
        return gson.toJson(object).getBytes();
    }
}
