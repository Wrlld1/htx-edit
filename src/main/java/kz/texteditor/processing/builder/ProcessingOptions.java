package kz.texteditor.processing.builder;

public record ProcessingOptions(
        boolean removeExtraSpaces,
        boolean fixPunctuation,
        boolean trimQuotes,
        CaseMode caseMode
) {
    public ProcessingOptions {
        if (caseMode == null) {
            throw new IllegalArgumentException("Case mode cannot be null");
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    public static ProcessingOptions defaults() {
        return builder().build();
    }

    public Builder toBuilder() {
        return builder()
                .removeExtraSpaces(removeExtraSpaces)
                .fixPunctuation(fixPunctuation)
                .trimQuotes(trimQuotes)
                .caseMode(caseMode);
    }

    public static class Builder {
        private boolean removeExtraSpaces = false;
        private boolean fixPunctuation = false;
        private boolean trimQuotes = false;
        private CaseMode caseMode = CaseMode.KEEP;

        public Builder removeExtraSpaces(boolean enabled) {
            removeExtraSpaces = enabled;
            return this;
        }

        public Builder fixPunctuation(boolean enabled) {
            fixPunctuation = enabled;
            return this;
        }

        public Builder trimQuotes(boolean enabled) {
            trimQuotes = enabled;
            return this;
        }

        public Builder caseMode(CaseMode mode) {
            caseMode = mode;
            return this;
        }

        public ProcessingOptions build() {
            return new ProcessingOptions(
                    removeExtraSpaces,
                    fixPunctuation,
                    trimQuotes,
                    caseMode
            );
        }
    }
}