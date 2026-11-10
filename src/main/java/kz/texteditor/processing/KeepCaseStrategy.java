package kz.texteditor.processing;

public class KeepCaseStrategy implements CaseStrategy {
    @Override
    public String apply(String text) {
        return text;
    }
}
