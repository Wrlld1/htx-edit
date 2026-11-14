package kz.texteditor.console;

import kz.texteditor.console.observer.ResultObserver;
import kz.texteditor.console.observer.StatisticsObserver;
import kz.texteditor.console.observer.TextStatistics;
import kz.texteditor.model.observer.EditorModel;
import kz.texteditor.processing.builder.CaseMode;
import kz.texteditor.processing.builder.ProcessingOptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

class ObserversTest {

    private ByteArrayOutputStream buffer;
    private EditorModel model;
    private StatisticsObserver statistics;

    @BeforeEach
    void setUp() {
        buffer = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(buffer, true, StandardCharsets.UTF_8);
        model = new EditorModel();
        statistics = new StatisticsObserver();
        model.addObserver(new ResultObserver(out));
        model.addObserver(statistics);
    }

    private String output() {
        return buffer.toString(StandardCharsets.UTF_8);
    }

    @Test
    void bothObserversReactToProcessing() {
        model.setSource("a b");
        model.setResult("A B");

        assertTrue(output().contains("A B"));
        assertEquals(new TextStatistics(3, 2, 1), statistics.sourceStatistics());
        assertEquals(new TextStatistics(3, 2, 1), statistics.resultStatistics().orElseThrow());
    }

    @Test
    void resultIsNotPrintedOnStaleOrUnchangedChanges() {
        model.setSource("x");
        assertFalse(output().contains("Результат"));
    }

    @Test
    void staleTransitionPrintsNeedsProcessingOnce() {
        model.setResult("x");
        model.setSource("y");
        model.setOptions(ProcessingOptions.builder().caseMode(CaseMode.UPPER).build());

        String out = output();
        assertEquals(1, out.split("нужна обработка", -1).length - 1);
    }

    @Test
    void reprocessingPrintsNewResultAgain() {
        model.setResult("x");
        model.setResult("x");
        assertEquals(2, output().split("--- Результат ---", -1).length - 1);
    }

    @Test
    void noResultStatisticsBeforeProcessing() {
        model.setSource("abc");
        assertTrue(statistics.resultStatistics().isEmpty());
    }
}
