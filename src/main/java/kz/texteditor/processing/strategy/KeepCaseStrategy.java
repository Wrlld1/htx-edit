package kz.texteditor.processing.strategy;

import java.util.Objects;

/**
 * Concrete Strategy that leaves the original letter case unchanged.
 */
public class KeepCaseStrategy implements CaseStrategy {
    @Override
    public String apply(String text) {
        return Objects.requireNonNull(text, "Input text cannot be null");
    }
}
