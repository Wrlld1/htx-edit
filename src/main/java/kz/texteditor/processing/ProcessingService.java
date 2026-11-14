package kz.texteditor.processing;

import java.util.Objects;

/**
 * Service entry point that dynamically assembles the CaseStrategy and
 * TextProcessorDecorator chain based on the provided ProcessingOptions,
 * executing transformations in the fixed order: Case -> Whitespace -> Punctuation -> Quotes.
 */
public class ProcessingService {
    public String process(String text, ProcessingOptions options) {
        Objects.requireNonNull(text, "Input text cannot be null");
        Objects.requireNonNull(options, "Processing options cannot be null");

        CaseStrategy strategy = switch (options.caseMode()) {
            case KEEP -> new KeepCaseStrategy();
            case UPPER -> new UpperCaseStrategy();
            case LOWER -> new LowerCaseStrategy();
        };
        TextProcessor processor = new BaseTextProcessor(strategy);

        if (options.removeExtraSpaces()) processor = new WhitespaceDecorator(processor);
        if (options.fixPunctuation()) processor = new PunctuationDecorator(processor);
        if (options.trimQuotes()) processor = new QuotesDecorator(processor);

        return processor.process(text);
    }
}
