package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_RECURRENCE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SESSION;

import java.time.LocalDateTime;
import java.util.Optional;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.EditApptCommand;
import seedu.address.logic.commands.EditCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.recurrence.Recurrence;

/**
 * Parses input arguments and creates a new {@code EditApptCommand} object.
 */
public class EditApptCommandParser implements Parser<EditApptCommand> {

    @Override
    public EditApptCommand parse(String args) throws ParseException {
        requireNonNull(args);
        AppointmentDescriptionParser.ExtractionResult descriptionResult =
                AppointmentDescriptionParser.extract(args, PREFIX_SESSION, PREFIX_DATE, PREFIX_RECURRENCE);
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(descriptionResult.getRemainingArgs(),
                PREFIX_SESSION, PREFIX_DATE, PREFIX_RECURRENCE);

        Index personIndex = ParserUtil.parseIndex(argMultimap.getPreamble(), EditApptCommand.MESSAGE_USAGE);
        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_SESSION, PREFIX_DATE, PREFIX_RECURRENCE);

        if (argMultimap.getValue(PREFIX_SESSION).isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditApptCommand.MESSAGE_USAGE));
        }

        Index sessionIndex = ParserUtil.parseIndex(argMultimap.getValue(PREFIX_SESSION).get(),
                EditApptCommand.MESSAGE_USAGE);

        Optional<LocalDateTime> appointmentStart = Optional.empty();
        if (argMultimap.getValue(PREFIX_DATE).isPresent()) {
            appointmentStart = Optional.of(ParserUtil.parseIsoDateTime(argMultimap.getValue(PREFIX_DATE).get()));
        }

        Optional<Recurrence> recurrence = Optional.empty();
        if (argMultimap.getValue(PREFIX_RECURRENCE).isPresent()) {
            recurrence = Optional.of(ParserUtil.parseRecurrence(argMultimap.getValue(PREFIX_RECURRENCE).get()));
        }

        Optional<String> description = Optional.empty();
        if (descriptionResult.getDescription().isPresent()) {
            String parsedDescription = descriptionResult.getDescription().get().trim();
            if (parsedDescription.isEmpty()) {
                throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditApptCommand.MESSAGE_USAGE));
            }
            description = Optional.of(parsedDescription);
        }

        if (appointmentStart.isEmpty() && recurrence.isEmpty() && description.isEmpty()) {
            throw new ParseException(EditCommand.MESSAGE_NOT_EDITED);
        }

        return new EditApptCommand(personIndex, sessionIndex, appointmentStart, recurrence, description);
    }
}
