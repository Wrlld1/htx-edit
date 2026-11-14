package kz.texteditor.processing;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class WhitespaceDecoratorTest {
    private final WhitespaceDecorator decorator = new WhitespaceDecorator(
                                                  new BaseTextProcessor(
                                                  new KeepCaseStrategy()));

    @ParameterizedTest
    @CsvSource({
            "'  hello   world  ', 'hello world'",
            "'\thello \t\t world\t', 'hello world'",
            "'  line 1  \n   \n\tline   2\t\n', 'line 1\n\nline 2\n'",
            "'', ''"
    })
    void rulesAreEnforced(String input, String expected) {
        String output = decorator.process(input);
        assertEquals(expected, output);
    }

    @ParameterizedTest
    @CsvSource({
            "'  ПриВеТ,   WoRlD!  ', 'ПриВеТ, WoRlD!'",
            "'  Привет ,мир!Как   дела ?  ', 'Привет ,мир!Как дела ?'",
            "'  « Привет,   мир! »  ', '« Привет, мир! »'",
            "'  \" Привет,   мир! \"  ', '\" Привет, мир! \"'",
            "'line 1\n\n\nline 2', 'line 1\n\n\nline 2'"
    })
    void doesNotInterfereWithOtherDecorators(String input, String expected) {
        String output = decorator.process(input);
        assertEquals(expected, output);
    }

    @Test
    void decoratorChainingWorks() {
        TextProcessor processor = new WhitespaceDecorator(
                                  new BaseTextProcessor(
                                  new UpperCaseStrategy()));
        String output = processor.process("  Привет,   world!  ");
        assertEquals("ПРИВЕТ, WORLD!", output);
    }
}
