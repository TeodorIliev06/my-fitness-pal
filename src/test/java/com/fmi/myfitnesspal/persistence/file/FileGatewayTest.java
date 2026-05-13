package com.fmi.myfitnesspal.persistence.file;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public final class FileGatewayTest {

    private final String FILE_CONTENT = "[\"Apple\"]";

    @TempDir
    Path tempDirectory;

    @Test
    void testReadReturnsEmptyOptionalWhenFileDoesNotExist() {
        FileGateway gateway = new FileGateway(tempDirectory.resolve("missing.json"));

        Optional<String> result = gateway.read();

        assertTrue(result.isEmpty(),
                "read should return an empty Optional when the file does not exist");
    }

    @Test
    void testReadReturnsFileContentWhenFileExists() throws IOException {
        Path file = tempDirectory.resolve("data.json");
        Files.writeString(file, FILE_CONTENT);
        FileGateway gateway = new FileGateway(file);

        Optional<String> result = gateway.read();

        assertEquals(FILE_CONTENT, result.orElseThrow(),
                "read should return the exact content written to the file");
    }

    @Test
    void testReadThrowsOnNonReadablePath() throws IOException {
        Path directoryAsFile = tempDirectory.resolve("data.json");
        Files.createDirectory(directoryAsFile);
        FileGateway gateway = new FileGateway(directoryAsFile);

        assertThrows(UncheckedIOException.class, gateway::read,
                "read should wrap any IOException in an UncheckedIOException");
    }

    @Test
    void testWriteCreatesFileWhenItDoesNotExist() {
        Path newFile = tempDirectory.resolve("new-data.json");
        FileGateway gateway = new FileGateway(newFile);

        gateway.write(FILE_CONTENT);

        assertTrue(Files.exists(newFile),
                "write should create the file if it does not yet exist");
    }

    @Test
    void testWriteOverwritesExistingFile() throws IOException {
        Path file = tempDirectory.resolve("data.json");
        Files.writeString(file, "[\"stale\"]");
        FileGateway gateway = new FileGateway(file);

        gateway.write(FILE_CONTENT);

        assertEquals(FILE_CONTENT, Files.readString(file),
                "write should overwrite the existing content completely");
    }

    @Test
    void testWriteThrowsOnNonWritablePath() throws IOException {
        Path directoryAsFile = tempDirectory.resolve("data.json");
        Files.createDirectory(directoryAsFile);
        FileGateway gateway = new FileGateway(directoryAsFile);

        assertThrows(UncheckedIOException.class, () -> gateway.write(FILE_CONTENT),
                "write should wrap any IOException in an UncheckedIOException");
    }
}
