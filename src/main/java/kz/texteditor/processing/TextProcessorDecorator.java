package kz.texteditor.processing;

/**
 * Base Decorator that holds a reference to a wrapped TextProcessor delegate,
 * allowing formatting rules to be stacked dynamically.
 */
public abstract class TextProcessorDecorator implements TextProcessor {
    protected final TextProcessor delegate;

    protected TextProcessorDecorator(TextProcessor delegate) {
        this.delegate = delegate;
    }
}
