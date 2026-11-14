package kz.texteditor.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class FileServiceTest {

    @TempDir
    Path folder;

    private final FileService service = new FileService();

    @Test
    void writesAndReadsUtf8() throws IOException {
        Path file = folder.resolve("text.txt");
        String text = "Привет! Сәлем! Hello!";

        service.write(text, file);

        assertEquals(text, service.read(file));
        assertArrayEquals(
                text.getBytes(StandardCharsets.UTF_8),
                Files.readAllBytes(file)
        );
    }

    @Test
    void normalizesLineEndings() throws IOException {
        Path file = folder.resolve("lines.txt");
        service.write("first\r\nsecond\rthird", file);

        assertEquals("first\nsecond\nthird", service.read(file));
    }

    @Test
    void supportsEmptyText() throws IOException {
        Path file = folder.resolve("empty.txt");
        service.write("", file);

        assertEquals("", service.read(file));
    }

    @Test
    void missingFileCausesError() {
        Path file = folder.resolve("missing.txt");

        assertThrows(IOException.class, () -> service.read(file));
    }
}