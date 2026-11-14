package kz.texteditor.processing;

/**
 * Concrete Decorator that executes its delegate first, then removes horizontal
 * whitespace before punctuation marks and normalizes spacing after punctuation
 * groups while preserving closing quotes and line breaks.
 */
public class PunctuationDecorator extends TextProcessorDecorator {
    private static final String STRIP_SPACES_REGEX =
            "[ \\t]+(?=[.,!?:;])";
    private static final String NORMALIZE_AFTER_REGEX =
            "(?m)([.,!?:;]+)[ \\t]*(?![ \\t.,!?:;\\r\\n»\"]|$)";

    public PunctuationDecorator(TextProcessor delegate) {
        super(delegate);
    }

    @Override
    public String process(String text) {
        String processedText = delegate.process(text);
        String newDecorated = processedText.replaceAll(STRIP_SPACES_REGEX, "");
        return newDecorated.replaceAll(NORMALIZE_AFTER_REGEX, "$1 ");
    }
}
