package kz.texteditor.processing;

import java.util.Locale;
import java.util.Objects;

/**
 * Concrete Strategy that converts letters to uppercase using a locale-independent
 * root mapping to support Russian, Kazakh, and English characters consistently.
 */
public class UpperCaseStrategy implements CaseStrategy {
    @Override
    public String apply(String text) {
        Objects.requireNonNull(text, "Input text cannot be null");
        return text.toUpperCase(Locale.ROOT);
    }
}
