package com.fmi.myfitnesspal.persistence.file;

import org.external.json.JsonConverter;

import java.util.Optional;

public final class JsonObjectPersistence<T> implements ObjectPersistenceStore<T> {

    private final JsonConverter jsonConverter;
    private final FileGateway fileGateway;
    private final Class<T> dtoType;

    public JsonObjectPersistence(JsonConverter jsonConverter, FileGateway fileGateway, Class<T> dtoType) {
        this.jsonConverter = jsonConverter;
        this.fileGateway = fileGateway;
        this.dtoType = dtoType;
    }

    @Override
    public Optional<T> load() {
        return fileGateway.read()
                .map(json -> jsonConverter.deserializeSingle(json, dtoType));
    }

    @Override
    public void save(T item) {
        fileGateway.write(jsonConverter.serializeSingle(item));
    }
}
