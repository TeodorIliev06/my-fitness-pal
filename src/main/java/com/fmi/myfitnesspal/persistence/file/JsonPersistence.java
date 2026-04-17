package com.fmi.myfitnesspal.persistence.file;

import org.external.json.JsonConverter;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public final class JsonPersistence<DTO> implements PersistenceStore<DTO> {

    private final JsonConverter jsonConverter;
    private final Path filePath;
    private final Class<DTO> dtoType;

    public JsonPersistence(JsonConverter jsonConverter, Path filePath, Class<DTO> dtoType) {
        this.jsonConverter = jsonConverter;
        this.filePath = filePath;
        this.dtoType = dtoType;
    }

    @Override
    public List<DTO> load() {
        if (!Files.exists(filePath)) {
            return List.of();
        }
        try {
            String json = Files.readString(filePath);
            return jsonConverter.deserialize(json, dtoType);
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to load from file: " + filePath, e);
        }
    }

    @Override
    public void save(List<DTO> dtos) {
        try {
            String json = jsonConverter.serialize(dtos);
            Files.writeString(filePath, json);
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to save to file: " + filePath, e);
        }
    }
}
