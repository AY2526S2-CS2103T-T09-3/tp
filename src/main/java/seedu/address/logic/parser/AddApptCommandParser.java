package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_RECURRENCE;

import java.time.LocalDateTime;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.AddApptCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.recurrence.Recurrence;

/**
 * Parses input arguments and creates a new {@code AddApptCommand} object.
 */
public class AddApptCommandParser implements Parser<AddApptCommand> {

    @Override
    public AddApptCommand parse(String args) throws ParseException {
        requireNonNull(args);
        AppointmentDescriptionParser.ExtractionResult descriptionResult =
                AppointmentDescriptionParser.extract(args, PREFIX_DATE, PREFIX_RECURRENCE);
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(descriptionResult.getRemainingArgs(),
                PREFIX_DATE, PREFIX_RECURRENCE);

        Index index = ParserUtil.parseIndex(argMultimap.getPreamble(), AddApptCommand.MESSAGE_USAGE);
        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_DATE, PREFIX_RECURRENCE);

        if (argMultimap.getValue(PREFIX_DATE).isEmpty() || descriptionResult.getDescription().isEmpty()) {
            throw new ParseException(String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT,
                    AddApptCommand.MESSAGE_USAGE));
        }

        LocalDateTime appointmentStart =
                ParserUtil.parseIsoDateTime(argMultimap.getValue(PREFIX_DATE).get());
        Recurrence recurrence = Recurrence.NONE;
        if (argMultimap.getValue(PREFIX_RECURRENCE).isPresent()) {
            recurrence = ParserUtil.parseRecurrence(argMultimap.getValue(PREFIX_RECURRENCE).get());
        }

        String description = descriptionResult.getDescription().orElseThrow();
        if (description.isEmpty()) {
            throw new ParseException(String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT,
                    AddApptCommand.MESSAGE_USAGE));
        }

        return new AddApptCommand(index, appointmentStart, recurrence, description);
    }
}
