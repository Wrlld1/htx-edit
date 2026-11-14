package kz.texteditor.export;

import java.io.IOException;
import java.nio.file.Path;

public abstract class ExportJob {

    public final void export(String text, Path path) throws IOException {
        TextExporter exporter = createExporter();
        exporter.write(text, path);
    }

    protected abstract TextExporter createExporter();
}