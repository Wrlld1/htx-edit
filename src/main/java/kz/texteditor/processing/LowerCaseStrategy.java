package kz.texteditor.processing;

import java.util.Locale;

public class LowerCaseStrategy implements CaseStrategy {
    @Override
    public String apply(String text) {
        return text.toLowerCase(Locale.ROOT);
    }
}
