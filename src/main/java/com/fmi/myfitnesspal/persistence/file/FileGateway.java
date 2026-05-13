package com.fmi.myfitnesspal.persistence.file;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

public final class FileGateway {

    private final Path filePath;

    public FileGateway(Path filePath) {
        this.filePath = filePath;
    }

    public Optional<String> read() {
        if (!Files.exists(filePath)) {
            return Optional.empty();
        }
        try {
            return Optional.of(Files.readString(filePath));
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to read from file: " + filePath, e);
        }
    }

    public void write(String content) {
        try {
            Files.writeString(filePath, content);
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to write to file: " + filePath, e);
        }
    }
}
