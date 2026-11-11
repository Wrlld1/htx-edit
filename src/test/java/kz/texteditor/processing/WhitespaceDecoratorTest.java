package kz.texteditor.processing;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class WhitespaceDecoratorTest {
    @ParameterizedTest
    @CsvSource({
            "'  hello   world  ', 'hello world'",
            "'\thello \t\t world\t', 'hello world'",
            "'  line 1  \n   \n\tline   2\t\n', 'line 1\n\nline 2\n'",
            "'', ''"
    })
    void rulesAreEnforced(String input, String expected) {
        WhitespaceDecorator decorator = new WhitespaceDecorator(new BaseTextProcessor(new KeepCaseStrategy()));
        String output = decorator.process(input);
        assertEquals(expected, output);
    }

    @Test
    void decoratorChainingWorks() {
        WhitespaceDecorator formatUpper = new WhitespaceDecorator(new BaseTextProcessor(new UpperCaseStrategy()));
        String output = formatUpper.process("  Привет,   world!  ");
        assertEquals("ПРИВЕТ, WORLD!", output);
    }
}
