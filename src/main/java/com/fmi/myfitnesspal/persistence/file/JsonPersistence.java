package com.fmi.myfitnesspal.persistence.file;

import org.external.json.JsonConverter;

import java.util.List;

public final class JsonPersistence<DTO> implements PersistenceStore<DTO> {

    private final JsonConverter jsonConverter;
    private final FileGateway fileGateway;
    private final Class<DTO> dtoType;

    public JsonPersistence(JsonConverter jsonConverter, FileGateway fileGateway, Class<DTO> dtoType) {
        this.jsonConverter = jsonConverter;
        this.fileGateway = fileGateway;
        this.dtoType = dtoType;
    }

    @Override
    public List<DTO> load() {
        return fileGateway.read()
                .map(json -> jsonConverter.deserialize(json, dtoType))
                .orElse(List.of());
    }

    @Override
    public void save(List<DTO> dtos) {
        fileGateway.write(jsonConverter.serialize(dtos));
    }
}
