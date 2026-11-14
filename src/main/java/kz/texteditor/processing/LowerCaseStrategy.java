package kz.texteditor.processing;

import java.util.Locale;

/**
 * Concrete Strategy that converts letters to lowercase using a locale-independent
 * root mapping to support Russian, Kazakh, and English characters consistently.
 */
public class LowerCaseStrategy implements CaseStrategy {
    @Override
    public String apply(String text) {
        return text.toLowerCase(Locale.ROOT);
    }
}
