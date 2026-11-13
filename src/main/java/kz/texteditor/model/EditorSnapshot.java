package kz.texteditor.model;

import kz.texteditor.processing.ProcessingOptions;

import java.util.Objects;

/**
 * Immutable snapshot of the editor state. This is what is passed to subscribers.
 *
 * @param sourceText    source text
 * @param resultText    latest result (may be stale; "" if no processing has occurred yet)
 * @param options       current settings
 * @param resultCurrent true if the result matches the current source and settings
 * @param resultVersion result number: increases after each processing step, 0 means no result yet
 */
public record EditorSnapshot(String sourceText,
                             String resultText,
                             ProcessingOptions options,
                             boolean resultCurrent,
                             long resultVersion) {

    public EditorSnapshot {
        Objects.requireNonNull(sourceText, "sourceText");
        Objects.requireNonNull(resultText, "resultText");
        Objects.requireNonNull(options, "options");
    }

    /** Whether at least one processing step has occurred. */
    public boolean hasResult() {
        return resultVersion > 0;
    }
}
