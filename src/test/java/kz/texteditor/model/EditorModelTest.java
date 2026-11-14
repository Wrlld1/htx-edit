package kz.texteditor.model;

import kz.texteditor.processing.CaseMode;
import kz.texteditor.processing.ProcessingOptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EditorModelTest {

    private EditorModel model;
    private List<EditorSnapshot> first;
    private List<EditorSnapshot> second;

    @BeforeEach
    void setUp() {
        model = new EditorModel();
        first = new ArrayList<>();
        second = new ArrayList<>();
        model.addObserver(first::add);
        model.addObserver(second::add);
    }

    @Test
    void initialStateHasNoResult() {
        EditorSnapshot s = model.snapshot();
        assertEquals("", s.sourceText());
        assertFalse(s.resultCurrent());
        assertFalse(s.hasResult());
        assertEquals(ProcessingOptions.defaults(), s.options());
    }

    @Test
    void setResultMakesResultCurrentAndNotifiesBothObserversWithSameSnapshot() {
        model.setSource("abc");
        model.setResult("ABC");

        assertEquals(2, first.size());
        assertEquals(2, second.size());
        EditorSnapshot last = first.get(1);
        assertSame(last, second.get(1));
        assertTrue(last.resultCurrent());
        assertEquals("ABC", last.resultText());
        assertEquals(1, last.resultVersion());
    }

    @Test
    void changingSourceMakesResultStaleButKeepsOldResult() {
        model.setSource("abc");
        model.setResult("abc");
        model.setSource("new");

        EditorSnapshot s = model.snapshot();
        assertFalse(s.resultCurrent());
        assertEquals("abc", s.resultText());
        assertTrue(s.hasResult());
    }

    @Test
    void changingOptionsMakesResultStale() {
        model.setResult("x");
        model.setOptions(ProcessingOptions.builder().caseMode(CaseMode.LOWER).build());
        assertFalse(model.snapshot().resultCurrent());
    }

    @Test
    void unchangedSourceOrOptionsDoNotNotifyOrInvalidate() {
        model.setSource("abc");
        model.setResult("abc");
        int notifications = first.size();

        model.setSource("abc");
        model.setOptions(ProcessingOptions.defaults());

        assertEquals(notifications, first.size());
        assertTrue(model.snapshot().resultCurrent());
    }

    @Test
    void sameObserverIsRegisteredOnlyOnce() {
        List<EditorSnapshot> received = new ArrayList<>();
        EditorObserver observer = received::add;
        model.addObserver(observer);
        model.addObserver(observer);
        model.setSource("a");
        assertEquals(1, received.size());
    }

    @Test
    void removedObserverIsNotNotified() {
        List<EditorSnapshot> received = new ArrayList<>();
        EditorObserver observer = received::add;
        model.addObserver(observer);
        model.removeObserver(observer);
        model.setSource("a");
        assertTrue(received.isEmpty());
    }

    @Test
    void emptySourceCanBeProcessedToEmptyResult() {
        model.setResult("");
        assertTrue(model.snapshot().resultCurrent());
        assertTrue(model.snapshot().hasResult());
    }
}
