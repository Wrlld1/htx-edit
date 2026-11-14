package kz.texteditor.processing;

import java.util.Objects;

/**
 * Concrete Component that serves as the innermost layer of the Decorator chain,
 * delegating initial case conversion to the configured CaseStrategy.
 */
public class BaseTextProcessor implements TextProcessor {
    private final CaseStrategy strategy;

    public BaseTextProcessor(CaseStrategy strategy) {
        this.strategy = Objects.requireNonNull(strategy, "Case strategy cannot be null");
    }

    @Override
    public String process(String text) {
        Objects.requireNonNull(text, "Input text cannot be null");
        return strategy.apply(text);
    }
}
