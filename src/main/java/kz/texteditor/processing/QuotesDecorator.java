package kz.texteditor.processing;

import java.util.regex.Pattern;

/**
 * Concrete Decorator that executes its delegate first, then removes horizontal
 * whitespace immediately inside paired guillemets («...») and straight double
 * quotes ("...") on the same line without altering unpaired quotes.
 */
public class QuotesDecorator extends TextProcessorDecorator {
    private static final Pattern GUILLEMETS_REGEX =
            Pattern.compile("«[ \\t]*([^«»\\r\\n]*?)[ \\t]*»");
    private static final Pattern DOUBLE_QUOTES_REGEX =
            Pattern.compile("\"[ \\t]*([^\"\\r\\n]*?)[ \\t]*\"");

    public QuotesDecorator(TextProcessor delegate) {
        super(delegate);
    }

    @Override
    public String process(String text) {
        String processedText = delegate.process(text);
        String finalText = GUILLEMETS_REGEX.matcher(processedText).replaceAll("«$1»");
        return DOUBLE_QUOTES_REGEX.matcher(finalText).replaceAll("\"$1\"");
    }
}
