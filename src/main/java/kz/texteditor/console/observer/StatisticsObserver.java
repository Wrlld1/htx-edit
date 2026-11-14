package kz.texteditor.console.observer;

import kz.texteditor.model.observer.EditorObserver;
import kz.texteditor.model.observer.EditorSnapshot;

import java.util.Optional;

/** Recalculates source and result statistics; used for item 5 and output after processing. */
public class StatisticsObserver implements EditorObserver {

    private TextStatistics sourceStatistics = TextStatistics.of("");
    private TextStatistics resultStatistics;

    @Override
    public void onChanged(EditorSnapshot snapshot) {
        sourceStatistics = TextStatistics.of(snapshot.sourceText());
        resultStatistics = snapshot.hasResult() ? TextStatistics.of(snapshot.resultText()) : null;
    }

    public TextStatistics sourceStatistics() {
        return sourceStatistics;
    }

    /** Empty if processing has not occurred yet; returns the old result's statistics if it is stale. */
    public Optional<TextStatistics> resultStatistics() {
        return Optional.ofNullable(resultStatistics);
    }
}
