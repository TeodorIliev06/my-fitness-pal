package com.fmi.myfitnesspal.persistence;

import com.fmi.myfitnesspal.persistence.file.ObjectPersistenceStore;
import com.fmi.myfitnesspal.persistence.file.PersistenceStore;

import java.nio.file.Path;

public interface PersistenceStoreFactory {
    <DTO> PersistenceStore<DTO> createListStore(Path filePath, Class<DTO> dtoClass);

    <DTO> ObjectPersistenceStore<DTO> createObjectStore(Path filePath, Class<DTO> dtoClass);
}
