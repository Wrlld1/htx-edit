package kz.texteditor.console;

/**
 * Text statistics.
 * Characters are Unicode code points (including spaces and line breaks),
 * words are sequences of non-whitespace characters, and lines are the number of LF characters + 1.
 */
public record TextStatistics(int characters, int words, int lines) {

    public static TextStatistics of(String text) {
        int characters = text.codePointCount(0, text.length());
        int words = 0;
        int lines = 1;
        boolean inWord = false;
        for (int i = 0; i < text.length(); ) {
            int cp = text.codePointAt(i);
            i += Character.charCount(cp);
            if (cp == '\n') {
                lines++;
            }
            if (Character.isWhitespace(cp)) {
                inWord = false;
            } else if (!inWord) {
                inWord = true;
                words++;
            }
        }
        return new TextStatistics(characters, words, lines);
    }

    @Override
    public String toString() {
        return "символов: " + characters + ", слов: " + words + ", строк: " + lines;
    }
}
