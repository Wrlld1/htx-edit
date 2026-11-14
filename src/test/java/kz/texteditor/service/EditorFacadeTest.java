package kz.texteditor.service;

import kz.texteditor.export.factory.ExportFormat;
import kz.texteditor.export.factory.creator.ExportJob;
import kz.texteditor.export.factory.creator.HtmlExportJob;
import kz.texteditor.export.factory.creator.TxtExportJob;
import kz.texteditor.model.observer.EditorModel;
import kz.texteditor.processing.builder.ProcessingOptions;
import kz.texteditor.processing.ProcessingService;
import kz.texteditor.service.facade.EditorException;
import kz.texteditor.service.facade.EditorFacade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.EnumMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class EditorFacadeTest {

    @TempDir
    Path dir;

    private EditorFacade facade;

    @BeforeEach
    void setUp() {
        FileService files = new FileService();
        Map<ExportFormat, ExportJob> jobs = new EnumMap<>(ExportFormat.class);
        jobs.put(ExportFormat.TXT, new TxtExportJob());
        jobs.put(ExportFormat.HTML, new HtmlExportJob());
        // All rules are disabled by default, so the result does not depend on the ProcessingService implementation.
        facade = new EditorFacade(new EditorModel(), new ProcessingService(), files, jobs);
    }

    @Test
    void saveWithoutProcessingIsBlocked() {
        facade.updateSource("abc");
        EditorException e = assertThrows(EditorException.class,
                () -> facade.saveResult(ExportFormat.TXT, dir.resolve("r.txt").toString()));
        assertEquals("Сначала обработайте текст", e.getMessage());
    }

    @Test
    void processThenSaveTxtWritesUtf8() throws IOException {
        facade.updateSource("Привет, мир!");
        facade.processText();
        Path target = dir.resolve("r.txt");
        facade.saveResult(ExportFormat.TXT, target.toString());
        assertEquals("Привет, мир!", Files.readString(target, StandardCharsets.UTF_8));
    }

    @Test
    void saveHtmlEscapesUserText() throws IOException {
        facade.updateSource("<b>привет</b>");
        facade.processText();
        Path target = dir.resolve("r.html");
        facade.saveResult(ExportFormat.HTML, target.toString());
        String html = Files.readString(target, StandardCharsets.UTF_8);
        assertTrue(html.contains("&lt;b&gt;привет&lt;/b&gt;"));
        assertFalse(html.contains("<b>привет</b>"));
    }

    @Test
    void changingSourceOrOptionsBlocksSaving() {
        facade.updateSource("abc");
        facade.processText();
        assertTrue(facade.canSave());

        facade.updateOptions(ProcessingOptions.builder().fixPunctuation(true).build());
        assertFalse(facade.canSave());
        assertThrows(EditorException.class,
                () -> facade.saveResult(ExportFormat.TXT, dir.resolve("x.txt").toString()));

        facade.processText();
        assertTrue(facade.canSave());
        facade.updateSource("other");
        assertFalse(facade.canSave());
    }

    @Test
    void openNormalizesLineEndings() throws IOException {
        Path file = dir.resolve("in.txt");
        Files.writeString(file, "a\r\nb\rc\nd", StandardCharsets.UTF_8);
        facade.open(file.toString());
        assertEquals("a\nb\nc\nd", facade.getSnapshot().sourceText());
    }

    @Test
    void openMissingFileKeepsCurrentText() {
        facade.updateSource("keep me");
        assertThrows(EditorException.class, () -> facade.open(dir.resolve("missing.txt").toString()));
        assertEquals("keep me", facade.getSnapshot().sourceText());
    }

    @Test
    void blankPathIsRejected() {
        assertThrows(EditorException.class, () -> facade.open("  "));
    }

    @Test
    void writeFailureGivesFriendlyErrorAndKeepsState() {
        facade.updateSource("abc");
        facade.processText();
        String badPath = dir.resolve("no-such-dir").resolve("r.txt").toString();
        EditorException e = assertThrows(EditorException.class, () -> facade.saveResult(ExportFormat.TXT, badPath));
        assertTrue(e.getMessage().startsWith("Не удалось сохранить файл"));
        assertTrue(facade.canSave());
    }

    @Test
    void emptySourceProcessesToEmptyResult() {
        facade.processText();
        assertTrue(facade.canSave());
        assertEquals("", facade.getSnapshot().resultText());
    }
}
