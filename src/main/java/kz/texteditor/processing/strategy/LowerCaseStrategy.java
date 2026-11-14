package kz.texteditor.processing.strategy;

import java.util.Locale;
import java.util.Objects;

/**
 * Concrete Strategy that converts letters to lowercase using a locale-independent
 * root mapping to support Russian, Kazakh, and English characters consistently.
 */
public class LowerCaseStrategy implements CaseStrategy {
    @Override
    public String apply(String text) {
        Objects.requireNonNull(text, "Input text cannot be null");
        return text.toLowerCase(Locale.ROOT);
    }
}
