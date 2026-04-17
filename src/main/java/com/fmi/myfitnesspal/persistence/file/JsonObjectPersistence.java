package com.fmi.myfitnesspal.persistence.file;

import org.external.json.JsonConverter;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

public final class JsonObjectPersistence<T> implements ObjectPersistenceStore<T> {

    private final JsonConverter jsonConverter;
    private final Path filePath;
    private final Class<T> recordType;

    public JsonObjectPersistence(JsonConverter jsonConverter, Path filePath, Class<T> recordType) {
        this.jsonConverter = jsonConverter;
        this.filePath = filePath;
        this.recordType = recordType;
    }

    @Override
    public Optional<T> load() {
        if (!Files.exists(filePath)) {
            return Optional.empty();
        }
        try {
            String json = Files.readString(filePath);
            return Optional.of(jsonConverter.deserializeSingle(json, recordType));
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to load object from file: " + filePath, e);
        }
    }

    @Override
    public void save(T item) {
        try {
            String json = jsonConverter.serializeSingle(item);
            Files.writeString(filePath, json);
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to save object to file: " + filePath, e);
        }
    }
}
