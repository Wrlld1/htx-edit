package kz.texteditor.processing.decorator;

import java.util.regex.Pattern;

/**
 * Concrete Decorator that executes its delegate first, then collapses sequences
 * of spaces and tabs into a single space and trims line edges while preserving
 * line breaks and empty lines.
 */
public class WhitespaceDecorator extends TextProcessorDecorator {
    private static final Pattern HORIZONTAL_SPACES_REGEX =
            Pattern.compile("[ \\t]+");
    private static final Pattern EDGE_SPACES_REGEX =
            Pattern.compile("^[ \\t]+|[ \\t]+$");

    public WhitespaceDecorator(TextProcessor delegate) {
        super(delegate);
    }

    @Override
    public String process(String text) {
        String processedText = super.process(text);
        String[] splitText = processedText.split("\n", -1);
        for (int i = 0; i < splitText.length; i++) {
            String trimmedLine = EDGE_SPACES_REGEX.matcher(splitText[i]).replaceAll("");
            splitText[i] = HORIZONTAL_SPACES_REGEX.matcher(trimmedLine).replaceAll(" ");
        }
        return String.join("\n", splitText);
    }
}
