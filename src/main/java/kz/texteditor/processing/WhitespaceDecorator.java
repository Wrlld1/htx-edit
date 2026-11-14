package kz.texteditor.processing;

import java.util.regex.Pattern;

/**
 * Concrete Decorator that executes its delegate first, then collapses sequences
 * of spaces and tabs into a single space and trims line edges while preserving
 * line breaks and empty lines.
 */
public class WhitespaceDecorator extends TextProcessorDecorator {
    private static final Pattern HORIZONTAL_SPACES_REGEX =
            Pattern.compile("[ \\t]+");

    public WhitespaceDecorator(TextProcessor delegate) {
        super(delegate);
    }

    @Override
    public String process(String text) {
        String processedText = delegate.process(text);
        String[] splitText = processedText.split("\n", -1);
        for (int i = 0; i < splitText.length; i++) {
            String noWhitespaceText = splitText[i].trim();
            noWhitespaceText = HORIZONTAL_SPACES_REGEX.matcher(noWhitespaceText).replaceAll(" ");
            splitText[i] = noWhitespaceText;
        }
        return String.join("\n", splitText);
    }
}
