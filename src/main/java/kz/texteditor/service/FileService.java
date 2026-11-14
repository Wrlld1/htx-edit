package kz.texteditor.service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileService {

    public String read(Path path) throws IOException {
        String text = Files.readString(path, StandardCharsets.UTF_8);

        return text.replace("\r\n", "\n").replace("\r", "\n");
    }

    public void write(String text, Path path) throws IOException {
        Files.writeString(path, text, StandardCharsets.UTF_8);
    }
}