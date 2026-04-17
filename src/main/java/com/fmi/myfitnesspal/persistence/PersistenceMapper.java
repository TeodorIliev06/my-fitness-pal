package com.fmi.myfitnesspal.persistence;

import java.util.List;

public interface PersistenceMapper<ENTITY, DTO> {

    DTO toDto(ENTITY entity);

    ENTITY toEntity(DTO dto);

    default List<DTO> toDtos(List<ENTITY> entities) {
        return entities.stream().map(this::toDto).toList();
    }

    default List<ENTITY> toEntities(List<DTO> dtos) {
        return dtos.stream().map(this::toEntity).toList();
    }
}
