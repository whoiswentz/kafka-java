package io.wentz;

import com.google.gson.*;

import java.lang.reflect.Type;

public class MessageTypeAdapter implements JsonSerializer<Message>, JsonDeserializer<Message> {
    @Override
    public JsonElement serialize(Message src, Type typeOfSrc, JsonSerializationContext context) {
        var jsonObject = new JsonObject();
        jsonObject.addProperty("type", src.getPayload().getClass().getName());
        jsonObject.add("correlationId", context.serialize(src.getId()));
        jsonObject.add("payload", context.serialize(src.getPayload()));
        return jsonObject;
    }

    @Override
    public Message deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        var object = json.getAsJsonObject();
        var payloadType = object.get("type").getAsString();
        var correlationId = (CorrelationId)context.deserialize(object.get("correlationId"), CorrelationId.class);
        try {
            var payload = context.deserialize(object.get("payload"), Class.forName(payloadType));
            return new Message(correlationId, payload);
        } catch (ClassNotFoundException e) {
            throw new JsonParseException(e);
        }
    }
}
