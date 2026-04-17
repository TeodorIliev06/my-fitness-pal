package com.fmi.myfitnesspal.persistence.file;

import org.external.json.JsonConverter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

    @TempDir
    Path tempDirectory;

    @Mock
    private JsonConverter jsonConverter;

    @Test
    void testLoadWithNonExistingFileReadsFileAndDelegatesToDeserializer() {
        Path nonExistentFile = tempDirectory.resolve("foods.json");
        JsonPersistence<String> persistence =
                new JsonPersistence<>(jsonConverter, nonExistentFile, String.class);

        List<String> result = persistence.load();

        assertTrue(result.isEmpty(),
                "load should return an empty list when the backing file does not yet exist");
        verify(jsonConverter, never()).deserialize(anyString(), any());
    }

    @Test
    void testLoadWhenFileExistsReadsFileAndDelegatesToDeserializer() throws IOException {
        Path foodFile = tempDirectory.resolve("foods.json");
        Files.writeString(foodFile, SERIALIZED_JSON);

        List<String> expectedEntities = List.of("Apple");
        when(jsonConverter.deserialize(SERIALIZED_JSON, String.class))
                .thenReturn(expectedEntities);
        JsonPersistence<String> persistence =
                new JsonPersistence<>(jsonConverter, foodFile, String.class);

        List<String> result = persistence.load();

        assertEquals(expectedEntities, result,
                "load should return the list produced by the deserializer");
    }

    @Test
    void testLoadWhenFileExistsPassesFileToDeserializer() throws IOException {
        Path foodFile = tempDirectory.resolve("foods.json");
        Files.writeString(foodFile, SERIALIZED_JSON);

        when(jsonConverter.deserialize(eq(SERIALIZED_JSON), eq(String.class)))
                .thenReturn(List.of());
        JsonPersistence<String> persistence =
                new JsonPersistence<>(jsonConverter, foodFile, String.class);

        persistence.load();

        verify(jsonConverter).deserialize(SERIALIZED_JSON, String.class);
    }

    @Test
    void testLoadWhenReadingFileFails() throws IOException {
        Path directoryAsFile = tempDirectory.resolve("foods.json");

        Files.createDirectory(directoryAsFile);
        JsonPersistence<String> persistence =
                new JsonPersistence<>(jsonConverter, directoryAsFile, String.class);

        assertThrows(UncheckedIOException.class, persistence::load,
                "load should wrap any IOException from the file system in an UncheckedIOException");
    }

    @Test
    void testSaveSerializesListAndWritesResult() throws IOException {
        Path foodFile = tempDirectory.resolve("foods.json");

        when(jsonConverter.serialize(any())).thenReturn(SERIALIZED_JSON);
        JsonPersistence<String> persistence =
                new JsonPersistence<>(jsonConverter, foodFile, String.class);
        List<String> dtos = List.of("Apple");

        persistence.save(dtos);

        String writtenContent = Files.readString(foodFile);
        assertEquals(SERIALIZED_JSON, writtenContent,
                "save should write the serialized JSON string to the backing file");
    }

    @Test
    void testSavePassesDtoListToSerializer() {
        Path foodFile = tempDirectory.resolve("foods.json");

        when(jsonConverter.serialize(any())).thenReturn(SERIALIZED_JSON);
        JsonPersistence<String> persistence =
                new JsonPersistence<>(jsonConverter, foodFile, String.class);
        List<String> dtos = List.of("Apple", "Banana");

        persistence.save(dtos);

        verify(jsonConverter).serialize(dtos);
    }

    @Test
    void testSaveCreatesFileWithNoFileProvided() {
        Path newFile = tempDirectory.resolve("new-foods.json");

        when(jsonConverter.serialize(any())).thenReturn("[]");
        JsonPersistence<String> persistence =
                new JsonPersistence<>(jsonConverter, newFile, String.class);

        persistence.save(List.of());

        assertTrue(Files.exists(newFile),
                "save should create the backing file if it does not yet exist");
    }

    @Test
    void testSaveOverwritesExistingFile() throws IOException {
        Path foodFile = tempDirectory.resolve("foods.json");
        Files.writeString(foodFile, "[\"stale data\"]");

        when(jsonConverter.serialize(any())).thenReturn(SERIALIZED_JSON);
        JsonPersistence<String> persistence =
                new JsonPersistence<>(jsonConverter, foodFile, String.class);

        persistence.save(List.of("Apple"));

        assertEquals(SERIALIZED_JSON, Files.readString(foodFile),
                "save should overwrite an existing file completely, not append to it");
    }

    @Test
    void testSaveWhenWritingFileFails() throws IOException {
        Path directoryAsFile = tempDirectory.resolve("foods.json");
        Files.createDirectory(directoryAsFile);

        when(jsonConverter.serialize(any())).thenReturn(SERIALIZED_JSON);
        JsonPersistence<String> persistence =
                new JsonPersistence<>(jsonConverter, directoryAsFile, String.class);

        assertThrows(UncheckedIOException.class, () -> persistence.save(List.of()),
                "save should wrap any IOException from the file system in an UncheckedIOException");
    }
}
