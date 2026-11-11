package kz.texteditor.processing;

/**
 * Strategy interface for applying letter-case transformations to text
 * without terminal or file I/O.
 */
public interface CaseStrategy {
    String apply(String text);
}
