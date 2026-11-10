package kz.texteditor.processing;

import java.util.Locale;

public class UpperCaseStrategy implements CaseStrategy {
    @Override
    public String apply(String text) {
        return text.toUpperCase(Locale.ROOT);
    }
}
