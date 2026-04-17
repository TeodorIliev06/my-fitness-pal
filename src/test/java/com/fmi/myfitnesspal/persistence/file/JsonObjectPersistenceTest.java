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
import java.util.Optional;

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
public final class JsonObjectPersistenceTest {

    private static final String SERIALIZED_JSON = "{\"brand\":\"Apple\"}";
    private static final String FOOD_NAME = "Apple";

    @TempDir
    Path tempDirectory;

    @Mock
    private JsonConverter jsonConverter;

    @Test
    void testLoadWithNonExistentFileReturnsEmptyOptional() {
        Path nonExistentFile = tempDirectory.resolve("diary.json");
        JsonObjectPersistence<String> persistence =
                new JsonObjectPersistence<>(jsonConverter, nonExistentFile, String.class);

        Optional<String> result = persistence.load();

        assertTrue(result.isEmpty(),
                "load should return an empty Optional when the backing file does not yet exist");
        verify(jsonConverter, never()).deserialize(anyString(), any());
    }

    @Test
    void testLoadWhenFileExistsReadsFileAndDelegatesToDeserializer() throws IOException {
        Path diaryFile = tempDirectory.resolve("diary.json");
        Files.writeString(diaryFile, SERIALIZED_JSON);

        String expectedItem = FOOD_NAME;
        when(jsonConverter.deserializeSingle(SERIALIZED_JSON, String.class)).thenReturn(expectedItem);
        JsonObjectPersistence<String> persistence =
                new JsonObjectPersistence<>(jsonConverter, diaryFile, String.class);

        Optional<String> result = persistence.load();

        assertTrue(result.isPresent(),
                "load should return a non-empty Optional when the backing file exists");
        assertEquals(expectedItem, result.get(),
                "load should return the object produced by the deserializer");
    }

    @Test
    void testLoadWhenFileExistsPassesFileToDeserializer() throws IOException {
        Path diaryFile = tempDirectory.resolve("diary.json");
        Files.writeString(diaryFile, SERIALIZED_JSON);

        when(jsonConverter.deserializeSingle(eq(SERIALIZED_JSON), eq(String.class))).thenReturn(FOOD_NAME);
        JsonObjectPersistence<String> persistence =
                new JsonObjectPersistence<>(jsonConverter, diaryFile, String.class);

        persistence.load();

        verify(jsonConverter).deserializeSingle(SERIALIZED_JSON, String.class);
    }

    @Test
    void testLoadWhenReadingFileFails() throws IOException {
        Path directoryAsFile = tempDirectory.resolve("diary.json");
        Files.createDirectory(directoryAsFile);

        JsonObjectPersistence<String> persistence =
                new JsonObjectPersistence<>(jsonConverter, directoryAsFile, String.class);

        assertThrows(UncheckedIOException.class, persistence::load,
                "load should wrap any IOException from the file system in an UncheckedIOException");
    }

    @Test
    void testSaveSerializesItemAndWritesResult() throws IOException {
        Path diaryFile = tempDirectory.resolve("diary.json");

        when(jsonConverter.serializeSingle(any())).thenReturn(SERIALIZED_JSON);
        JsonObjectPersistence<String> persistence =
                new JsonObjectPersistence<>(jsonConverter, diaryFile, String.class);

        persistence.save(FOOD_NAME);

        assertEquals(SERIALIZED_JSON, Files.readString(diaryFile),
                "save should write the serialized JSON string to the backing file");
    }

    @Test
    void testSavePassesItemToSerializer() {
        Path diaryFile = tempDirectory.resolve("diary.json");

        when(jsonConverter.serializeSingle(any())).thenReturn(SERIALIZED_JSON);
        JsonObjectPersistence<String> persistence =
                new JsonObjectPersistence<>(jsonConverter, diaryFile, String.class);
        String itemToSave = FOOD_NAME;

        persistence.save(itemToSave);

        verify(jsonConverter).serializeSingle(itemToSave);
    }

    @Test
    void testSaveCreatesFileWithNoFileProvided() {
        Path newFile = tempDirectory.resolve("new-diary.json");

        when(jsonConverter.serializeSingle(any())).thenReturn("{}");
        JsonObjectPersistence<String> persistence =
                new JsonObjectPersistence<>(jsonConverter, newFile, String.class);

        persistence.save(FOOD_NAME);

        assertTrue(Files.exists(newFile),
                "save should create the backing file if it does not yet exist");
    }

    @Test
    void testSaveOverwritesExistingFile() throws IOException {
        Path diaryFile = tempDirectory.resolve("diary.json");
        Files.writeString(diaryFile, "{\"stale\":true}");

        when(jsonConverter.serializeSingle(any())).thenReturn(SERIALIZED_JSON);
        JsonObjectPersistence<String> persistence =
                new JsonObjectPersistence<>(jsonConverter, diaryFile, String.class);

        persistence.save(FOOD_NAME);

        assertEquals(SERIALIZED_JSON, Files.readString(diaryFile),
                "save should overwrite an existing file completely, not append to it");
    }

    @Test
    void testSaveWhenWritingFileFails() throws IOException {
        Path directoryAsFile = tempDirectory.resolve("diary.json");
        Files.createDirectory(directoryAsFile);

        when(jsonConverter.serializeSingle(any())).thenReturn(SERIALIZED_JSON);
        JsonObjectPersistence<String> persistence =
                new JsonObjectPersistence<>(jsonConverter, directoryAsFile, String.class);

        assertThrows(UncheckedIOException.class, () -> persistence.save(FOOD_NAME),
                "save should wrap any IOException from the file system in an UncheckedIOException");
    }
}
