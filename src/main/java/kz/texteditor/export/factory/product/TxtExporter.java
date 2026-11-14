package kz.texteditor.export.factory.product;

import kz.texteditor.service.FileService;

import java.io.IOException;
import java.nio.file.Path;

public class TxtExporter implements TextExporter {

    private final FileService fileService = new FileService();

    @Override
    public void write(String text, Path path) throws IOException {
        fileService.write(text, path);
    }
}