package com.fmi.myfitnesspal.persistence.file;

import java.util.List;

public interface PersistenceStore<DTO> {
    List<DTO> load();
    void save(List<DTO> dtos);
}
