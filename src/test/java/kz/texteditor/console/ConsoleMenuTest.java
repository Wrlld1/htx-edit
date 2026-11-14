package kz.texteditor.console;

import kz.texteditor.export.ExportFormat;
import kz.texteditor.export.ExportJob;
import kz.texteditor.export.HtmlExportJob;
import kz.texteditor.export.TxtExportJob;
import kz.texteditor.model.EditorModel;
import kz.texteditor.processing.CaseMode;
import kz.texteditor.processing.ProcessingService;
import kz.texteditor.service.EditorFacade;
import kz.texteditor.service.FileService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.EnumMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/** End-to-end tests through the menu using scripted input. */
class ConsoleMenuTest {

    @TempDir
    Path dir;

    private EditorFacade facade;

    private String run(String... inputLines) {
        FileService files = new FileService();
        Map<ExportFormat, ExportJob> jobs = new EnumMap<>(ExportFormat.class);
        jobs.put(ExportFormat.TXT, new TxtExportJob(files));
        jobs.put(ExportFormat.HTML, new HtmlExportJob(files));

        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(buffer, true, StandardCharsets.UTF_8);
        EditorModel model = new EditorModel();
        StatisticsObserver statistics = new StatisticsObserver();
        model.addObserver(new ResultObserver(out));
        model.addObserver(statistics);
        facade = new EditorFacade(model, new ProcessingService(), files, jobs);

        BufferedReader in = new BufferedReader(new StringReader(String.join("\n", inputLines) + "\n"));
        new ConsoleMenu(facade, statistics, in, out).run();
        return buffer.toString(StandardCharsets.UTF_8);
    }

    @Test
    void fullPathInputProcessSaveTxt() throws IOException {
        Path target = dir.resolve("result.txt");
        String out = run("1", "Hello", "world", ":end", "4", "6", target.toString(), "0");

        assertTrue(out.contains("Статистика результата: символов: 11, слов: 2, строк: 2"));
        assertEquals("Hello\nworld", Files.readString(target, StandardCharsets.UTF_8));
        assertFalse(facade.getSnapshot().sourceText().contains(":end"));
    }

    @Test
    void saveBeforeProcessingShowsMessage() {
        String out = run("1", "abc", ":end", "6", "0");
        assertTrue(out.contains("Сначала обработайте текст"));
    }

    @Test
    void changingSettingMakesResultStaleAndBlocksSave() {
        String out = run("1", "abc", ":end", "4", "3", "2", "0", "7", "0");
        assertTrue(out.contains("Результат устарел: нужна обработка."));
        assertTrue(out.contains("Сначала обработайте текст"));
        assertTrue(out.contains("Исправить пунктуацию [ВКЛ]"));
    }

    @Test
    void caseModeCanBeSelected() {
        run("3", "4", "3", "0", "0");
        assertEquals(CaseMode.LOWER, facade.getSnapshot().options().caseMode());
    }

    @Test
    void invalidMenuChoiceReturnsToMenu() {
        String out = run("99", "abc", "0");
        assertEquals(2, out.split("Неверный пункт меню", -1).length - 1);
    }

    @Test
    void openingMissingFileKeepsTextAndShowsError() {
        String out = run("1", "keep", ":end", "2", dir.resolve("none.txt").toString(), "0");
        assertTrue(out.contains("Не удалось открыть файл"));
        assertEquals("keep", facade.getSnapshot().sourceText());
    }

    @Test
    void overwriteRequiresConfirmation() throws IOException {
        Path target = dir.resolve("result.txt");
        Files.writeString(target, "old", StandardCharsets.UTF_8);

        run("1", "new", ":end", "4", "6", target.toString(), "n", "0");
        assertEquals("old", Files.readString(target, StandardCharsets.UTF_8));

        run("1", "new", ":end", "4", "6", target.toString(), "y", "0");
        assertEquals("new", Files.readString(target, StandardCharsets.UTF_8));
    }

    @Test
    void showSourceAndResultMarksStaleResult() {
        String out = run("1", "abc", ":end", "4", "1", "xyz", ":end", "5", "0");
        assertTrue(out.contains("--- Результат (устарел: нужна обработка) ---"));
    }

    @Test
    void showBeforeProcessingSaysNoResult() {
        String out = run("5", "0");
        assertTrue(out.contains("Результата ещё нет"));
    }

    @Test
    void endOfInputExitsGracefully() {
        assertDoesNotThrow(() -> run("1", "abc"));
    }
}
