package kz.texteditor.processing.decorator;

import java.util.Objects;

/**
 * Base Decorator that holds a reference to a wrapped TextProcessor delegate,
 * allowing formatting rules to be stacked dynamically.
 */
public abstract class TextProcessorDecorator implements TextProcessor {
    private final TextProcessor delegate;

    protected TextProcessorDecorator(TextProcessor delegate) {
        this.delegate = Objects.requireNonNull(delegate, "Delegate processor cannot be null");
    }

    @Override
    public String process(String text) {
        return delegate.process(text);
    }
}
