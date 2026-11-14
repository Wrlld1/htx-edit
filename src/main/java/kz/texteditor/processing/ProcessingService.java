package kz.texteditor.processing;

import java.util.Map;
import java.util.Objects;

/**
 * Service entry point that dynamically assembles the CaseStrategy and
 * TextProcessorDecorator chain based on the provided ProcessingOptions,
 * executing transformations in the fixed order: Case -> Whitespace -> Punctuation -> Quotes.
 */
public class ProcessingService {
    private static final Map<CaseMode, CaseStrategy> STRATEGIES = Map.of(
            CaseMode.KEEP, new KeepCaseStrategy(),
            CaseMode.UPPER, new UpperCaseStrategy(),
            CaseMode.LOWER, new LowerCaseStrategy()
    );

    public String process(String text, ProcessingOptions options) {
        Objects.requireNonNull(text, "Input text cannot be null");
        Objects.requireNonNull(options, "Processing options cannot be null");

        CaseStrategy strategy = Objects.requireNonNull(
                STRATEGIES.get(options.caseMode()),
                "Unsupported case mode: " + options.caseMode()
        );
        TextProcessor processor = new BaseTextProcessor(strategy);

        if (options.removeExtraSpaces()) processor = new WhitespaceDecorator(processor);
        if (options.fixPunctuation()) processor = new PunctuationDecorator(processor);
        if (options.trimQuotes()) processor = new QuotesDecorator(processor);

        return processor.process(text);
    }
}
