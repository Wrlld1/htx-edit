package kz.texteditor.processing;

import kz.texteditor.processing.decorator.BaseTextProcessor;
import kz.texteditor.processing.decorator.PunctuationDecorator;
import kz.texteditor.processing.decorator.TextProcessor;
import kz.texteditor.processing.strategy.KeepCaseStrategy;
import kz.texteditor.processing.strategy.LowerCaseStrategy;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PunctuationDecoratorTest {
    private final PunctuationDecorator decorator = new PunctuationDecorator(
                                                   new BaseTextProcessor(
                                                   new KeepCaseStrategy()));

    @ParameterizedTest
    @CsvSource({
            "'Привет ,мир !', 'Привет, мир!'",
            "'Подожди ...что ?!', 'Подожди... что?!'",
            "'Подожди ... что ?!', 'Подожди... что?!'",
            "'Привет,   мир!', 'Привет, мир!'",
            "'«Привет ,мир!»', '«Привет, мир!»'",
            "'\"Привет ,мир!\"', '\"Привет, мир!\"'",
            "'Он сказал :«Привет ,мир!»', 'Он сказал: «Привет, мир!»'",
            "'Он сказал :\"Привет ,мир!\"', 'Он сказал: \"Привет, мир!\"'",
            "'Он сказал:   \"Привет ,мир!\"', 'Он сказал: \"Привет, мир!\"'",
            "'Привет ,\"мир\"', 'Привет, \"мир\"'",
            "'Привет ,мир !\nПока ,мир .', 'Привет, мир!\nПока, мир.'",
            "'', ''"
    })
    void rulesAreEnforced(String input, String expected) {
        String output = decorator.process(input);
        assertEquals(expected, output);
    }

    @ParameterizedTest
    @CsvSource({
            "'  Привет   ,мир!  ', '  Привет, мир!  '",
            "'« Привет ,мир! »', '« Привет, мир! »'",
            "'\" Привет ,мир! \"', '\" Привет, мир! \"'",
            "'Он сказал: \"Привет ,мир!\"', 'Он сказал: \"Привет, мир!\"'"
    })
    void doesNotInterfereWithOtherDecorators(String input, String expected) {
        String output = decorator.process(input);
        assertEquals(expected, output);
    }

    @Test
    void decoratorChainingWorks() {
        TextProcessor processor = new PunctuationDecorator(
                                  new BaseTextProcessor(
                                  new LowerCaseStrategy()));
        String output = processor.process("ПРИВЕТ ,мир !");
        assertEquals("привет, мир!", output);
    }
}
