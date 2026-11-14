package kz.texteditor.processing;

/**
 * Service entry point that dynamically assembles the CaseStrategy and
 * TextProcessorDecorator chain based on the provided ProcessingOptions,
 * executing transformations in the fixed order: Case -> Whitespace -> Punctuation -> Quotes.
 */
public class ProcessingService {
    public String process(String text, ProcessingOptions options) {
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
