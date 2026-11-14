package kz.texteditor.processing;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.util.DefaultLocale;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CaseProcessingTest {
    private static final CaseStrategy UPPER = new UpperCaseStrategy();
    private static final CaseStrategy LOWER = new LowerCaseStrategy();
    private static final CaseStrategy KEEP = new KeepCaseStrategy();

    @ParameterizedTest
    @CsvSource({
            "hello, HELLO",
            "привет ёжик, ПРИВЕТ ЁЖИК",
            "сәлем әіңғүұқөһ, СӘЛЕМ ӘІҢҒҮҰҚӨҺ",
            "'Привет, World!', 'ПРИВЕТ, WORLD!'",
            "'', ''"
    })
    void upperCaseConvertsAllSupportedLanguages(String input, String expected) {
        String output = UPPER.apply(input);
        assertEquals(expected, output);
    }

    @ParameterizedTest
    @CsvSource({
            "HELLO, hello",
            "ПРИВЕТ ЁЖИК, привет ёжик",
            "СӘЛЕМ ӘІҢҒҮҰҚӨҺ, сәлем әіңғүұқөһ",
            "'ПРИВЕТ, World!', 'привет, world!'",
            "'', ''"
    })
    void lowerCaseConvertsAllSupportedLanguages(String input, String expected) {
        String output = LOWER.apply(input);
        assertEquals(expected, output);
    }

    @ParameterizedTest
    @CsvSource({
            "Hello, Hello",
            "Привет Ёжик, Привет Ёжик",
            "Сәлем Әіңғүұқөһ, Сәлем Әіңғүұқөһ",
            "'Привет, World!', 'Привет, World!'",
            "'', ''"
    })
    void keepCaseDoesNothing(String input, String expected) {
        String output = KEEP.apply(input);
        assertEquals(expected, output);
    }

    @Test
    void textProcessorDelegatesToStrategy() {
        assertEqualsProcessed(UPPER, "Hello", "HELLO");
        assertEqualsProcessed(LOWER, "Привет", "привет");
        assertEqualsProcessed(KEEP, "Сәлем", "Сәлем");
    }

    @Test
    @DefaultLocale("tr-TR")
    void caseConversionIgnoresSystemLocale() {
        assertEquals("II", UPPER.apply("iI"));
        assertEquals("ii", LOWER.apply("iI"));
        assertEquals("iI", KEEP.apply("iI"));
    }

    private void assertEqualsProcessed(CaseStrategy strategy, String input, String expected) {
        BaseTextProcessor textProcessor = new BaseTextProcessor(strategy);
        String output = textProcessor.process(input);
        assertEquals(expected, output);
    }

    @Test
    void nullInputThrowsExceptionAcrossAllStrategiesAndBaseProcessor() {
        assertThrows(NullPointerException.class, () -> UPPER.apply(null));
        assertThrows(NullPointerException.class, () -> LOWER.apply(null));
        assertThrows(NullPointerException.class, () -> KEEP.apply(null));

        BaseTextProcessor processor = new BaseTextProcessor(KEEP);
        assertThrows(NullPointerException.class, () -> processor.process(null));
    }

    @Test
    void nullStrategyThrowsException() {
        assertThrows(NullPointerException.class, () -> new BaseTextProcessor(null));
    }

    @Test
    void nullDelegateThrowsException() {
        assertThrows(NullPointerException.class, () -> new WhitespaceDecorator(null));
        assertThrows(NullPointerException.class, () -> new PunctuationDecorator(null));
        assertThrows(NullPointerException.class, () -> new QuotesDecorator(null));
    }
}
