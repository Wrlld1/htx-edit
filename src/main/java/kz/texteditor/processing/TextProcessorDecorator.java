package kz.texteditor.processing;

public abstract class TextProcessorDecorator implements TextProcessor {
    protected final TextProcessor delegate;

    protected TextProcessorDecorator(TextProcessor delegate) {
        this.delegate = delegate;
    }
}
