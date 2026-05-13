package com.fmi.myfitnesspal.persistence.file;

import com.fmi.myfitnesspal.persistence.PersistenceStoreFactory;
import org.external.json.JsonConverter;

import java.nio.file.Path;

public final class JsonPersistenceStoreFactory implements PersistenceStoreFactory {

    private final JsonConverter jsonConverter;

    public JsonPersistenceStoreFactory(JsonConverter jsonConverter) {
        this.jsonConverter = jsonConverter;
    }

    @Override
    public <DTO> PersistenceStore<DTO> createListStore(Path filePath, Class<DTO> dtoClass) {
        FileGateway fileGateway = new FileGateway(filePath);
        return new JsonPersistence<>(jsonConverter, fileGateway, dtoClass);
    }

    @Override
    public <DTO> ObjectPersistenceStore<DTO> createObjectStore(Path filePath, Class<DTO> dtoClass) {
        FileGateway fileGateway = new FileGateway(filePath);
        return new JsonObjectPersistence<>(jsonConverter, fileGateway, dtoClass);
    }
}
