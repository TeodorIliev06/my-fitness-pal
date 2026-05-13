package com.fmi.myfitnesspal.persistence.file;

import org.external.json.JsonConverter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public final class JsonPersistenceTest {

    private static final String SERIALIZED_JSON = "[{\"brand\":\"Apple\"}]";

    @Mock
    private FileGateway fileGateway;

    @Mock
    private JsonConverter jsonConverter;

    @Test
    void testLoadWithNonExistingFileReturnsEmptyList() {
        when(fileGateway.read()).thenReturn(Optional.empty());
        JsonPersistence<String> persistence =
                new JsonPersistence<>(jsonConverter, fileGateway, String.class);

        List<String> result = persistence.load();

        assertTrue(result.isEmpty(),
                "load should return an empty list when the gateway finds no file");
        verify(jsonConverter, never()).deserialize(anyString(), any());
    }

    @Test
    void testLoadWhenFileExistsReturnsDeserializedList() {
        List<String> expectedEntities = List.of("Apple");

        when(fileGateway.read()).thenReturn(Optional.of(SERIALIZED_JSON));
        when(jsonConverter.deserialize(SERIALIZED_JSON, String.class))
                .thenReturn(expectedEntities);
        JsonPersistence<String> persistence =
                new JsonPersistence<>(jsonConverter, fileGateway, String.class);

        List<String> result = persistence.load();

        assertEquals(expectedEntities, result,
                "load should return the list produced by the deserializer");
    }

    @Test
    void testLoadPassesGatewayContentToDeserializer() {
        when(fileGateway.read()).thenReturn(Optional.of(SERIALIZED_JSON));
        when(jsonConverter.deserialize(eq(SERIALIZED_JSON), eq(String.class)))
                .thenReturn(List.of());
        JsonPersistence<String> persistence =
                new JsonPersistence<>(jsonConverter, fileGateway, String.class);

        persistence.load();

        verify(jsonConverter).deserialize(SERIALIZED_JSON, String.class);
    }

    @Test
    void testSavePassesDtoListToSerializer() {
        when(jsonConverter.serialize(any())).thenReturn(SERIALIZED_JSON);
        JsonPersistence<String> persistence =
                new JsonPersistence<>(jsonConverter, fileGateway, String.class);
        List<String> dtos = List.of("Apple", "Banana");

        persistence.save(dtos);

        verify(jsonConverter).serialize(dtos);
    }

    @Test
    void testSaveWritesSerializerOutputToGateway() {
        when(jsonConverter.serialize(any())).thenReturn(SERIALIZED_JSON);
        JsonPersistence<String> persistence =
                new JsonPersistence<>(jsonConverter, fileGateway, String.class);

        persistence.save(List.of("Apple"));

        verify(fileGateway).write(SERIALIZED_JSON);
    }
}
