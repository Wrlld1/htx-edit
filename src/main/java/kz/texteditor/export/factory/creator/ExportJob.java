package kz.texteditor.export.factory.creator;

import kz.texteditor.export.factory.product.TextExporter;

import java.io.IOException;
import java.nio.file.Path;

public abstract class ExportJob {

    public final void export(String text, Path path) throws IOException {
        TextExporter exporter = createExporter();
        exporter.write(text, path);
    }

    protected abstract TextExporter createExporter();
}