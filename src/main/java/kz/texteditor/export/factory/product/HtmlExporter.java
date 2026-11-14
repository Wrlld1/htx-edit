package kz.texteditor.export.factory.product;

import kz.texteditor.service.FileService;

import java.io.IOException;
import java.nio.file.Path;

public class HtmlExporter implements TextExporter {

    private final FileService fileService = new FileService();

    @Override
    public void write(String text, Path path) throws IOException {
        String escaped = text
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");

        String html = "<!DOCTYPE html>\n"
                + "<html>\n"
                + "<head>\n"
                + "<meta charset=\"UTF-8\">\n"
                + "<title>Text Editor</title>\n"
                + "</head>\n"
                + "<body>\n"
                + "<pre>\n" + escaped + "</pre>\n"
                + "</body>\n"
                + "</html>";

        fileService.write(html, path);
    }
}