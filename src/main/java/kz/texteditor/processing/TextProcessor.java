package kz.texteditor.processing;

/**
 * Component interface for the text processing pipeline.
 * Implementations transform text purely in memory without terminal I/O.
 */
public interface TextProcessor {
    String process(String text);
}
