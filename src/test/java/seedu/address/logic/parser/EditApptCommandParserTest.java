package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.CommandTestUtil.APPOINTMENT_DESCRIPTION_DESC;
import static seedu.address.logic.commands.CommandTestUtil.APPOINTMENT_RECURRENCE_DESC;
import static seedu.address.logic.commands.CommandTestUtil.APPOINTMENT_START_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_APPOINTMENT_START_DESC;
import static seedu.address.logic.commands.CommandTestUtil.PREAMBLE_NON_EMPTY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SESSION;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.EditApptCommand;
import seedu.address.logic.commands.EditCommand;
import seedu.address.model.recurrence.Recurrence;

public class EditApptCommandParserTest {

    private static final String MESSAGE_INVALID_FORMAT =
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditApptCommand.MESSAGE_USAGE);

    private final EditApptCommandParser parser = new EditApptCommandParser();

    @Test
    public void parse_missingParts_failure() {
        assertParseFailure(parser, "", MESSAGE_INVALID_FORMAT);
        assertParseFailure(parser, "1", MESSAGE_INVALID_FORMAT);
        assertParseFailure(parser, "1 " + PREFIX_SESSION + "1", EditCommand.MESSAGE_NOT_EDITED);
    }

    @Test
    public void parse_invalidPreamble_failure() {
        assertParseFailure(parser, "-1 " + PREFIX_SESSION + "1" + APPOINTMENT_START_DESC, MESSAGE_INVALID_FORMAT);
        assertParseFailure(parser,
                PREAMBLE_NON_EMPTY + " 1 " + PREFIX_SESSION + "1" + APPOINTMENT_START_DESC,
                MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidDate_failure() {
        assertParseFailure(parser, "1 " + PREFIX_SESSION + "1" + INVALID_APPOINTMENT_START_DESC,
                ParserUtil.MESSAGE_INVALID_DATE_TIME);
    }

    @Test
    public void parse_validSingleField_success() {
        assertParseSuccess(parser, "1 " + PREFIX_SESSION + "1" + APPOINTMENT_START_DESC,
                new EditApptCommand(INDEX_FIRST_PERSON, INDEX_FIRST_PERSON,
                        Optional.of(LocalDateTime.parse("2026-01-13T08:00:00")),
                        Optional.empty(),
                        Optional.empty()));
    }

    @Test
    public void parse_validMultipleFields_success() {
        assertParseSuccess(parser, "1 " + PREFIX_SESSION + "1"
                        + APPOINTMENT_START_DESC + APPOINTMENT_RECURRENCE_DESC + APPOINTMENT_DESCRIPTION_DESC,
                new EditApptCommand(INDEX_FIRST_PERSON, INDEX_FIRST_PERSON,
                        Optional.of(LocalDateTime.parse("2026-01-13T08:00:00")),
                        Optional.of(Recurrence.WEEKLY),
                        Optional.of("Weekly algebra practice")));
    }

    @Test
    public void parse_quotedDescriptionContainingPrefixLikeText_success() {
        assertParseSuccess(parser, "1 " + PREFIX_SESSION + "1 dsc/\" d d/lol \"",
                new EditApptCommand(INDEX_FIRST_PERSON, INDEX_FIRST_PERSON,
                        Optional.empty(),
                        Optional.empty(),
                        Optional.of("d d/lol")));
    }
}
