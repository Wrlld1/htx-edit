package kz.texteditor.processing;

import java.util.regex.Pattern;

/**
 * Concrete Decorator that executes its delegate first, then removes horizontal
 * whitespace before punctuation marks and normalizes spacing after punctuation
 * groups while preserving closing quotes and line breaks.
 */
public class PunctuationDecorator extends TextProcessorDecorator {
    private static final Pattern STRIP_SPACES_REGEX =
            Pattern.compile("[ \\t]+(?=[.,!?:;])");
    private static final Pattern NORMALIZE_AFTER_REGEX =
            Pattern.compile("(?m)([.,!?:;]+)[ \\t]*(?![ \\t.,!?:;\\r\\n»]|\"(?:[^\"\\r\\n]*\"[^\"\\r\\n]*\")*[^\"\\r\\n]*$|$)");

    public PunctuationDecorator(TextProcessor delegate) {
        super(delegate);
    }

    @Override
    public String process(String text) {
        String processedText = super.process(text);
        String newDecorated = STRIP_SPACES_REGEX.matcher(processedText).replaceAll("");
        return NORMALIZE_AFTER_REGEX.matcher(newDecorated).replaceAll("$1 ");
    }
}
