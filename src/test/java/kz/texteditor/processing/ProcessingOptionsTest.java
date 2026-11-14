package kz.texteditor.processing;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProcessingOptionsTest {

    @Test
    void defaultOptions() {
        ProcessingOptions options = ProcessingOptions.defaults();

        assertFalse(options.removeExtraSpaces());
        assertFalse(options.fixPunctuation());
        assertFalse(options.trimQuotes());
        assertEquals(CaseMode.KEEP, options.caseMode());
    }

    @Test
    void customOptions() {
        ProcessingOptions options = ProcessingOptions.builder()
                .removeExtraSpaces(true)
                .fixPunctuation(true)
                .trimQuotes(true)
                .caseMode(CaseMode.LOWER)
                .build();

        assertTrue(options.removeExtraSpaces());
        assertTrue(options.fixPunctuation());
        assertTrue(options.trimQuotes());
        assertEquals(CaseMode.LOWER, options.caseMode());
    }

    @Test
    void nullCaseModeIsRejected() {
        assertThrows(IllegalArgumentException.class, () ->
                ProcessingOptions.builder().caseMode(null).build()
        );
    }

    @Test
    void builderDoesNotChangePreviousOptions() {
        ProcessingOptions.Builder builder = ProcessingOptions.builder();

        ProcessingOptions first = builder.build();
        ProcessingOptions second = builder.fixPunctuation(true).build();

        assertFalse(first.fixPunctuation());
        assertTrue(second.fixPunctuation());
    }
}