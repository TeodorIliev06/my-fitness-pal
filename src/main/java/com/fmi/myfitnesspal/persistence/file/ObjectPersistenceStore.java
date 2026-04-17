package com.fmi.myfitnesspal.persistence.file;

import java.util.Optional;

public interface ObjectPersistenceStore<T> {
    Optional<T> load();
    void save(T item);
}
