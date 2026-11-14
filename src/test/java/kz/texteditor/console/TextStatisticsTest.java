package kz.texteditor.console;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TextStatisticsTest {

    @Test
    void emptyTextHasOneLine() {
        assertEquals(new TextStatistics(0, 0, 1), TextStatistics.of(""));
    }

    @Test
    void countsCharactersWordsAndLines() {
        assertEquals(new TextStatistics(11, 2, 1), TextStatistics.of("hello world"));
        assertEquals(new TextStatistics(5, 2, 2), TextStatistics.of("ab\ncd"));
    }

    @Test
    void trailingLineFeedAddsLine() {
        assertEquals(2, TextStatistics.of("abc\n").lines());
    }

    @Test
    void countsUnicodeCodePoints() {
        assertEquals(3, TextStatistics.of("😀а😀").characters());
        assertEquals(6, TextStatistics.of("Привет").characters());
    }

    @Test
    void multipleWhitespaceDoesNotCreateWords() {
        assertEquals(2, TextStatistics.of("  a \t\n  b  ").words());
    }
}
