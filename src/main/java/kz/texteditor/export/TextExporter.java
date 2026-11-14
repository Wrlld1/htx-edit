package kz.texteditor.export;

import java.io.IOException;
import java.nio.file.Path;

public interface TextExporter {
    void write(String text, Path path) throws IOException;
}