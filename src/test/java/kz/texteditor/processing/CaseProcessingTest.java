package kz.texteditor.processing;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.util.DefaultLocale;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CaseProcessingTest {
    @ParameterizedTest
    @CsvSource({
            "hello, HELLO",
            "привет, ПРИВЕТ",
            "сәлем, СӘЛЕМ",
            "'Привет, World!', 'ПРИВЕТ, WORLD!'",
            "'', ''"
    })
    void upperCaseConvertsAllSupportedLanguages(String input, String expected) {
        CaseStrategy strategy = new UpperCaseStrategy();
        String output = strategy.apply(input);
        assertEquals(expected, output);
    }

    @ParameterizedTest
    @CsvSource({
            "HELLO, hello",
            "ПРИВЕТ, привет",
            "СӘЛЕМ, сәлем",
            "'ПРИВЕТ, World!', 'привет, world!'",
            "'', ''"
    })
    void lowerCaseConvertsAllSupportedLanguages(String input, String expected) {
        CaseStrategy strategy = new LowerCaseStrategy();
        String output = strategy.apply(input);
        assertEquals(expected, output);
    }

    @ParameterizedTest
    @CsvSource({
            "Hello, Hello",
            "Привет, Привет",
            "Сәлем, Сәлем",
            "'Привет, World!', 'Привет, World!'",
            "'', ''"
    })
    void keepCaseDoesNothing(String input, String expected) {
        CaseStrategy strategy = new KeepCaseStrategy();
        String output = strategy.apply(input);
        assertEquals(expected, output);
    }

    @Test
    void textProcessorDelegatesToStrategy() {
        assertEqualsProcessed(new UpperCaseStrategy(), "Hello", "HELLO");
        assertEqualsProcessed(new LowerCaseStrategy(), "Привет", "привет");
        assertEqualsProcessed(new KeepCaseStrategy(), "Сәлем", "Сәлем");
    }

    @Test
    @DefaultLocale("tr-TR")
    void caseConversionIgnoresSystemLocale() {
        assertEquals("II", new UpperCaseStrategy().apply("iI"));
        assertEquals("ii", new LowerCaseStrategy().apply("iI"));
        assertEquals("iI", new KeepCaseStrategy().apply("iI"));
    }

    private void assertEqualsProcessed(CaseStrategy strategy, String input, String expected) {
        BaseTextProcessor textProcessor = new BaseTextProcessor(strategy);
        String output = textProcessor.process(input);
        assertEquals(expected, output);
    }
}
