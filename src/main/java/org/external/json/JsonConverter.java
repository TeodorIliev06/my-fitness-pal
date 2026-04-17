package org.external.json;

import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.type.CollectionType;

import java.util.List;

public final class JsonConverter {

    private final JsonMapper jsonMapper;

    public JsonConverter(JsonMapper jsonMapper) {
        this.jsonMapper = jsonMapper;
    }

    public <T> String serialize(List<T> items) {
        return jsonMapper.writeValueAsString(items);
    }

    public <T> String serializeSingle(T item) {
        return jsonMapper.writeValueAsString(item);
    }

    public <T> List<T> deserialize(String json, Class<T> recordType) {
        CollectionType listType = jsonMapper
                .getTypeFactory()
                .constructCollectionType(List.class, recordType);
        return jsonMapper.readValue(json, listType);
    }

    public <T> T deserializeSingle(String json, Class<T> recordType) {
        return jsonMapper.readValue(json, recordType);
    }
}
