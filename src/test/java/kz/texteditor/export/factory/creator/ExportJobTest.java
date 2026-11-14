package kz.texteditor.export.factory.creator;

import kz.texteditor.export.factory.product.HtmlExporter;
import kz.texteditor.export.factory.product.TxtExporter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class ExportJobTest {

    @TempDir
    Path folder;

    @Test
    void creatorsReturnCorrectExporters() {
        assertInstanceOf(
                TxtExporter.class,
                new TxtExportJob().createExporter()
        );

        assertInstanceOf(
                HtmlExporter.class,
                new HtmlExportJob().createExporter()
        );
    }

    @Test
    void exportsTxtThroughJob() throws IOException {
        Path file = folder.resolve("result.txt");
        String text = "Привет! Сәлем! Hello!\n  next line";

        ExportJob job = new TxtExportJob();
        job.export(text, file);

        assertEquals(
                text,
                Files.readString(file, StandardCharsets.UTF_8)
        );
    }

    @Test
    void exportsHtmlThroughJob() throws IOException {
        Path file = folder.resolve("result.html");
        String text = "<b>Сәлем</b> & \"Hello\" 'test'";

        ExportJob job = new HtmlExportJob();
        job.export(text, file);

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
}