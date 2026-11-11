package kz.texteditor.processing;

/**
 * Concrete Component that serves as the innermost layer of the Decorator chain,
 * delegating initial case conversion to the configured CaseStrategy.
 */
public class BaseTextProcessor implements TextProcessor {
    private final CaseStrategy strategy;

    public BaseTextProcessor(CaseStrategy strategy) {
        this.strategy = strategy;
    }

    @Override
    public String process(String text) {
        return strategy.apply(text);
    }
}
