package kz.texteditor.model;

import kz.texteditor.processing.ProcessingOptions;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Subject in the Observer pattern. Stores the editor state and notifies subscribers
 * with a single snapshot after each actual change.
 */
public class EditorModel {

    private final List<EditorObserver> observers = new CopyOnWriteArrayList<>();

    private String sourceText = "";
    private String resultText = "";
    private ProcessingOptions options = ProcessingOptions.defaults();
    private boolean resultCurrent;
    private long resultVersion;

    public void addObserver(EditorObserver observer) {
        Objects.requireNonNull(observer, "observer");
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }

    public void removeObserver(EditorObserver observer) {
        observers.remove(observer);
    }

    /** Replaces the source text. If the text has not changed, nothing happens. */
    public void setSource(String text) {
        Objects.requireNonNull(text, "text");
        if (sourceText.equals(text)) {
            return;
        }
        sourceText = text;
        markStaleAndNotify();
    }

    /** Replaces the settings. If the settings have not changed, nothing happens. */
    public void setOptions(ProcessingOptions newOptions) {
        Objects.requireNonNull(newOptions, "newOptions");
        if (options.equals(newOptions)) {
            return;
        }
        options = newOptions;
        markStaleAndNotify();
    }

    /** Saves the processing result and marks it as current. */
    public void setResult(String text) {
        Objects.requireNonNull(text, "text");
        resultText = text;
        resultCurrent = true;
        resultVersion++;
        notifyObservers();
    }

    public EditorSnapshot snapshot() {
        return new EditorSnapshot(sourceText, resultText, options, resultCurrent, resultVersion);
    }

    private void markStaleAndNotify() {
        resultCurrent = false;
        notifyObservers();
    }

    private void notifyObservers() {
        EditorSnapshot snapshot = snapshot();
        for (EditorObserver observer : observers) {
            observer.onChanged(snapshot);
        }
    }
}
