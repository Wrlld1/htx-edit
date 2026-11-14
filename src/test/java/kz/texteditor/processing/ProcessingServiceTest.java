package kz.texteditor.processing;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ProcessingServiceTest {
    private final ProcessingService service = new ProcessingService();

    @ParameterizedTest
    @CsvSource({
            "false, false, false, KEEP,   '  « Привет , мир ! »  ', '  « Привет , мир ! »  '",
            "true,  false, false, KEEP,   '  « Привет ,   мир ! »  ', '« Привет , мир ! »'",
            "false, true,  false, KEEP,   '  « Привет , мир ! »  ', '  « Привет, мир! »  '",
            "false, false, true,  KEEP,   '  « Привет , мир ! »  ', '  «Привет , мир !»  '",
            "true,  true,  true,  UPPER,  '  « привет ,   мир ! »  ', '«ПРИВЕТ, МИР!»'",
            "true,  true,  true,  LOWER,  '  \" ПРИВЕТ ,   МИР ! \"  ', '\"привет, мир!\"'"
    })
    void processAccordingToOptions(boolean spaces,
                                   boolean punct,
                                   boolean quotes,
                                   CaseMode mode,
                                   String input,
                                   String expected) {
        ProcessingOptions options = ProcessingOptions.builder()
                .removeExtraSpaces(spaces)
                .fixPunctuation(punct)
                .trimQuotes(quotes)
                .caseMode(mode)
                .build();
        String output = service.process(input, options);
        assertEquals(expected, output);
    }

    @Test
    void nullTextThrowsException() {
        ProcessingOptions options = ProcessingOptions.defaults();
        assertThrows(NullPointerException.class, () -> service.process(null, options));
    }

    @Test
    void nullOptionsThrowsException() {
        assertThrows(NullPointerException.class, () -> service.process("text", null));
    }
}
