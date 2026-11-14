package kz.texteditor.processing;

/**
 * Concrete Decorator that executes its delegate first, then removes horizontal
 * whitespace immediately inside paired guillemets («...») and straight double
 * quotes ("...") on the same line without altering unpaired quotes.
 */
public class QuotesDecorator extends TextProcessorDecorator {
    private static final String GUILLEMETS_REGEX =
            "«[ \\t]*([^«»\\r\\n]*?)[ \\t]*»";
    private static final String DOUBLE_QUOTES_REGEX =
            "\"[ \\t]*([^\"\\r\\n]*?)[ \\t]*\"";

    public QuotesDecorator(TextProcessor delegate) {
        super(delegate);
    }

    @Override
    public String process(String text) {
        String processedText = delegate.process(text);
        String finalText = processedText.replaceAll(GUILLEMETS_REGEX, "«$1»");
        return finalText.replaceAll(DOUBLE_QUOTES_REGEX, "\"$1\"");
    }
}
