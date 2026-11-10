package kz.texteditor.processing;

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
