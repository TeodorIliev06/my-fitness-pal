package com.fmi.myfitnesspal.persistence.file;

import org.external.json.JsonConverter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
public final class JsonObjectPersistenceTest {

    private static final String SERIALIZED_JSON = "{\"brand\":\"Apple\"}";
    private static final String FOOD_NAME = "Apple";

    @Mock
    private JsonConverter jsonConverter;

    @Mock
    private FileGateway fileGateway;

    @Test
    void testLoadWithNonExistentFileReturnsEmptyOptional() {
        when(fileGateway.read()).thenReturn(Optional.empty());
        JsonObjectPersistence<String> persistence =
                new JsonObjectPersistence<>(jsonConverter, fileGateway, String.class);

        Optional<String> result = persistence.load();

        assertTrue(result.isEmpty(),
                "load should return an empty Optional when the gateway finds no file");
        verify(jsonConverter, never()).deserializeSingle(anyString(), any());
    }

    @Test
    void testLoadWithExistingFileReturnsDeserializedItem() {
        when(fileGateway.read()).thenReturn(Optional.of(SERIALIZED_JSON));
        when(jsonConverter.deserializeSingle(SERIALIZED_JSON, String.class)).thenReturn(FOOD_NAME);
        JsonObjectPersistence<String> persistence =
                new JsonObjectPersistence<>(jsonConverter, fileGateway, String.class);

        Optional<String> result = persistence.load();

        assertTrue(result.isPresent(),
                "load should return a non-empty Optional when the gateway returns file content");
        assertEquals(FOOD_NAME, result.get(),
                "load should return the object produced by the deserializer");
    }

    @Test
    void testLoadPassesGatewayContentToDeserializer() {
        when(fileGateway.read()).thenReturn(Optional.of(SERIALIZED_JSON));
        when(jsonConverter.deserializeSingle(eq(SERIALIZED_JSON), eq(String.class)))
                .thenReturn(FOOD_NAME);
        JsonObjectPersistence<String> persistence =
                new JsonObjectPersistence<>(jsonConverter, fileGateway, String.class);

        persistence.load();

        verify(jsonConverter).deserializeSingle(SERIALIZED_JSON, String.class);
    }

    @Test
    void testSavePassesItemToSerializer() {
        when(jsonConverter.serializeSingle(any())).thenReturn(SERIALIZED_JSON);
        JsonObjectPersistence<String> persistence =
                new JsonObjectPersistence<>(jsonConverter, fileGateway, String.class);

        persistence.save(FOOD_NAME);

        verify(jsonConverter).serializeSingle(FOOD_NAME);
    }

    @Test
    void testSaveWritesSerializerOutputToGateway() {
        when(jsonConverter.serializeSingle(any())).thenReturn(SERIALIZED_JSON);
        JsonObjectPersistence<String> persistence =
                new JsonObjectPersistence<>(jsonConverter, fileGateway, String.class);

        persistence.save(FOOD_NAME);

        verify(fileGateway).write(SERIALIZED_JSON);
    }
}
