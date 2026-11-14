package kz.texteditor.export.factory.product;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class TextExporterTest {

    @TempDir
    Path folder;

    @Test
    void txtExporterWritesWithoutChanges() throws IOException {
        Path file = folder.resolve("result.txt");
        String text = "Привет! Сәлем! Hello!\n  next line";

        TextExporter exporter = new TxtExporter();
        exporter.write(text, file);

        assertEquals(
                text,
                Files.readString(file, StandardCharsets.UTF_8)
        );
    }

    @Test
    void htmlExporterWritesEscapedText() throws IOException {
        Path file = folder.resolve("result.html");
        String text = "<b>Сәлем</b> & \"Hello\" 'test'";

        TextExporter exporter = new HtmlExporter();
        exporter.write(text, file);

        String html = Files.readString(file, StandardCharsets.UTF_8);

        assertTrue(html.startsWith("<!DOCTYPE html>"));
        assertTrue(html.contains("<meta charset=\"UTF-8\">"));
        assertTrue(html.contains(
                "<pre>\n&lt;b&gt;Сәлем&lt;/b&gt; &amp; "
                        + "&quot;Hello&quot; &#39;test&#39;</pre>"
        ));
        assertFalse(html.contains("<b>Сәлем</b>"));
        assertTrue(html.endsWith("</html>"));
    }

    @Test
    void exportersHandleEmptyText() throws IOException {
        Path txt = folder.resolve("empty.txt");
        Path html = folder.resolve("empty.html");

        new TxtExporter().write("", txt);
        new HtmlExporter().write("", html);

        assertEquals("", Files.readString(txt));
        assertTrue(Files.readString(html).contains("<pre>\n</pre>"));
    }

    @Test
    void exportersReportFileErrors() {
        Path file = folder.resolve("missing/result");

        assertThrows(IOException.class, () ->
                new TxtExporter().write("text", file)
        );

        assertThrows(IOException.class, () ->
                new HtmlExporter().write("text", file)
        );
    }
}