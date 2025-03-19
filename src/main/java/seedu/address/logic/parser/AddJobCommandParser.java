package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_COMPANY_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_JOB_ROUNDS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_JOB_TITLE;

import java.util.stream.Stream;

import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.commands.AddJobCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.job.CompanyName;
import seedu.address.model.job.Job;
import seedu.address.model.job.JobRounds;
import seedu.address.model.job.JobTitle;

/**
 * Parses input arguments and creates a new AddJobCommand object
 */
public class AddJobCommandParser implements Parser<AddJobCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the AddJobCommand
     * and returns an AddJobCommand object for execution.
     *
     * @throws ParseException if the user input does not conform the expected format
     */
    public AddJobCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_JOB_TITLE, PREFIX_COMPANY_NAME,
                PREFIX_JOB_ROUNDS);
        if (!arePrefixesPresent(argMultimap, PREFIX_JOB_TITLE, PREFIX_COMPANY_NAME, PREFIX_JOB_ROUNDS)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_JOB_TITLE, PREFIX_COMPANY_NAME, PREFIX_JOB_ROUNDS);
        JobTitle title = ParserUtil.parseJobTitle(argMultimap.getValue(PREFIX_JOB_TITLE).get());
        CompanyName companyName = ParserUtil.parseCompanyName(argMultimap.getValue(PREFIX_COMPANY_NAME).get());
        JobRounds jobRounds = ParserUtil.parseJobRounds(argMultimap.getValue(PREFIX_JOB_ROUNDS).get());

        Job job = new Job(title, companyName, jobRounds);

        return new AddJobCommand(job);
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values
     * in the given {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }

}
