package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMPLOYMENT_TYPE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_JOB_ROUNDS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_JOB_SKILLS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_JOB_TITLE;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.EditJobCommand;
import seedu.address.logic.commands.EditJobCommand.EditJobDescriptor;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new EditCommand object
 */
public class EditJobCommandParser implements Parser<EditJobCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the
     * EditCommand and returns an EditCommand object for execution.
     *
     * @throws ParseException if the user input does not conform the expected format
     */
    public EditJobCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_JOB_TITLE, PREFIX_JOB_ROUNDS,
                PREFIX_JOB_SKILLS, PREFIX_EMPLOYMENT_TYPE);

        Index index;

        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (ParseException pe) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditJobCommand.MESSAGE_USAGE), pe);
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_JOB_TITLE, PREFIX_JOB_ROUNDS, PREFIX_JOB_SKILLS,
                PREFIX_EMPLOYMENT_TYPE);

        EditJobDescriptor editJobDescriptor = new EditJobDescriptor();

        if (argMultimap.getValue(PREFIX_JOB_TITLE).isPresent()) {
            editJobDescriptor.setJobTitle(ParserUtil.parseJobTitle(argMultimap.getValue(PREFIX_JOB_TITLE).get()));
        }
        if (argMultimap.getValue(PREFIX_JOB_ROUNDS).isPresent()) {
            editJobDescriptor.setJobRounds(ParserUtil.parseJobRounds(argMultimap.getValue(PREFIX_JOB_ROUNDS).get()));
        }
        if (argMultimap.getValue(PREFIX_JOB_SKILLS).isPresent()) {
            editJobDescriptor.setJobSkills(ParserUtil.parseJobSkills(argMultimap.getValue(PREFIX_JOB_SKILLS).get()));
        }
        if (argMultimap.getValue(PREFIX_EMPLOYMENT_TYPE).isPresent()) {
            editJobDescriptor.setJobType(ParserUtil.parseJobType(
                    argMultimap.getValue(PREFIX_EMPLOYMENT_TYPE).get()));
        }

        if (!editJobDescriptor.isAnyFieldEdited()) {
            throw new ParseException(EditJobCommand.MESSAGE_NOT_EDITED);
        }

        return new EditJobCommand(index, editJobDescriptor);
    }
}
