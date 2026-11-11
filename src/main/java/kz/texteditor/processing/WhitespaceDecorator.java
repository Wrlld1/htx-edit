package kz.texteditor.processing;

public class WhitespaceDecorator extends TextProcessorDecorator {
    public WhitespaceDecorator(TextProcessor delegate) {
        super(delegate);
    }

    @Override
    public String process(String text) {
        String processedText = delegate.process(text);
        String[] splitText = processedText.split("\n", -1);
        for (int i = 0; i < splitText.length; i++) {
            String noWhitespaceText = splitText[i].trim().replaceAll("[ \t]+", " ");
            splitText[i] = noWhitespaceText;
        }
        return String.join("\n", splitText);
    }
}
