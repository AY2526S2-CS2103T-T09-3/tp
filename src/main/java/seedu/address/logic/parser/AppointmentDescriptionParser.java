package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;

import java.util.Arrays;
import java.util.Optional;

import seedu.address.logic.Messages;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Extracts the {@code dsc/} field for appointment commands.
 * Supports quoted descriptions so prefix-like text such as {@code d/} inside the description
 * is not misinterpreted as a new field.
 */
final class AppointmentDescriptionParser {

    private AppointmentDescriptionParser() {}

    static ExtractionResult extract(String args, Prefix... otherPrefixes) throws ParseException {
        requireNonNull(args);

        int descriptionStart = findValidPrefixPosition(args, CliSyntax.PREFIX_DESCRIPTION, 0);
        if (descriptionStart == -1) {
            return new ExtractionResult(args, Optional.empty());
        }

        int valueStart = descriptionStart + CliSyntax.PREFIX_DESCRIPTION.getPrefix().length();
        ParseBounds bounds = parseDescriptionBounds(args, valueStart, otherPrefixes);

        int duplicateDescriptionStart = findValidPrefixPosition(args, CliSyntax.PREFIX_DESCRIPTION, bounds.endExclusive);
        if (duplicateDescriptionStart != -1) {
            throw new ParseException(Messages.getErrorMessageForDuplicatePrefixes(CliSyntax.PREFIX_DESCRIPTION));
        }

        String description = bounds.quoted
                ? args.substring(valueStart + 1, bounds.endExclusive - 1)
                : args.substring(valueStart, bounds.endExclusive);
        String remainingArgs = (args.substring(0, descriptionStart) + " " + args.substring(bounds.endExclusive)).trim();

        return new ExtractionResult(remainingArgs, Optional.of(description.trim()));
    }

    private static ParseBounds parseDescriptionBounds(String args, int valueStart, Prefix... otherPrefixes)
            throws ParseException {
        if (valueStart < args.length() && args.charAt(valueStart) == '"') {
            int closingQuote = args.indexOf('"', valueStart + 1);
            if (closingQuote == -1) {
                throw new ParseException("Appointment description is missing a closing quote.");
            }
            return new ParseBounds(closingQuote + 1, true);
        }

        int endExclusive = args.length();
        for (Prefix prefix : Arrays.asList(otherPrefixes)) {
            int prefixPosition = findValidPrefixPosition(args, prefix, valueStart);
            if (prefixPosition != -1 && prefixPosition < endExclusive) {
                endExclusive = prefixPosition;
            }
        }

        return new ParseBounds(endExclusive, false);
    }

    private static int findValidPrefixPosition(String argsString, Prefix prefix, int fromIndex) {
        int searchIndex = Math.max(fromIndex, 0);
        String prefixValue = prefix.getPrefix();

        while (searchIndex <= argsString.length() - prefixValue.length()) {
            int prefixIndex = argsString.indexOf(prefixValue, searchIndex);
            if (prefixIndex == -1) {
                return -1;
            }

            boolean isAtStart = prefixIndex == 0;
            boolean hasLeadingWhitespace = !isAtStart && Character.isWhitespace(argsString.charAt(prefixIndex - 1));
            if (isAtStart || hasLeadingWhitespace) {
                return prefixIndex;
            }

            searchIndex = prefixIndex + 1;
        }

        return -1;
    }

    static final class ExtractionResult {
        private final String remainingArgs;
        private final Optional<String> description;

        private ExtractionResult(String remainingArgs, Optional<String> description) {
            this.remainingArgs = remainingArgs;
            this.description = description;
        }

        String getRemainingArgs() {
            return remainingArgs;
        }

        Optional<String> getDescription() {
            return description;
        }
    }

    private static final class ParseBounds {
        private final int endExclusive;
        private final boolean quoted;

        private ParseBounds(int endExclusive, boolean quoted) {
            this.endExclusive = endExclusive;
            this.quoted = quoted;
        }
    }
}
