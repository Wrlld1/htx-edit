package kz.texteditor.processing;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class QuotesDecoratorTest {
    private final QuotesDecorator decorator = new QuotesDecorator(
                                              new BaseTextProcessor(
                                              new KeepCaseStrategy()));

    @ParameterizedTest
    @CsvSource({
            "'« hello world »', '«hello world»'",
            "'« Привет, мир! »', '«Привет, мир!»'",
            "'\" Привет, мир! \"', '\"Привет, мир!\"'",
            "'«\t  Привет  \t»', '«Привет»'",
            "'« один » и \" два \" и \" три \"', '«один» и \"два\" и \"три\"'",
            "'« Привет, \" мир \"! »', '«Привет, \"мир\"!»'",
            "'\" Привет \"\n« мир »', '\"Привет\"\n«мир»'",
            "'', ''"
    })
    void rulesAreEnforced(String input, String expected) {
        String output = decorator.process(input);
        assertEquals(expected, output);
    }

    @ParameterizedTest
    @CsvSource({
            "'\"  Привет   мир  \"', '\"Привет   мир\"'",
            "'  \" Привет \"  ', '  \"Привет\"  '",
            "'« Привет ,мир ! »', '«Привет ,мир !»'",
            "'Он сказал \" Привет, мир!', 'Он сказал \" Привет, мир!'",
            "'Он сказал « Привет, мир!', 'Он сказал « Привет, мир!'"
    })
    void doesNotInterfereWithOtherDecorators(String input, String expected) {
        String output = decorator.process(input);
        assertEquals(expected, output);
    }

    @Test
    void fullDecoratorChainWorks() {
        TextProcessor processor = new QuotesDecorator(
                                  new PunctuationDecorator(
                                  new WhitespaceDecorator(
                                  new BaseTextProcessor(
                                  new UpperCaseStrategy()))));
        String output = processor.process("  «  Привет ,   мир ! »  ");
        assertEquals("«ПРИВЕТ, МИР!»", output);
    }
}