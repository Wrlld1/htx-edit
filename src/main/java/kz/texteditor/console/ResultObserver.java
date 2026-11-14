package kz.texteditor.console;

import kz.texteditor.model.EditorObserver;
import kz.texteditor.model.EditorSnapshot;

import java.io.PrintStream;

/**
 * Prints the result only when a new current result becomes available.
 * When the current result becomes stale, reports that processing is needed.
 */
public class ResultObserver implements EditorObserver {

    private final PrintStream out;
    private long lastShownVersion;
    private boolean lastCurrent;

    public ResultObserver(PrintStream out) {
        this.out = out;
    }

    @Override
    public void onChanged(EditorSnapshot snapshot) {
        if (snapshot.resultCurrent() && snapshot.resultVersion() != lastShownVersion) {
            out.println("--- Результат ---");
            out.println(snapshot.resultText());
            out.println("-----------------");
        } else if (!snapshot.resultCurrent() && lastCurrent) {
            out.println("Результат устарел: нужна обработка.");
        }
        lastShownVersion = snapshot.resultVersion();
        lastCurrent = snapshot.resultCurrent();
    }
}
