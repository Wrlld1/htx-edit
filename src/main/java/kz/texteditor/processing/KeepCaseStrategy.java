package kz.texteditor.processing;

/**
 * Concrete Strategy that leaves the original letter case unchanged.
 */
public class KeepCaseStrategy implements CaseStrategy {
    @Override
    public String apply(String text) {
        return text;
    }
}
