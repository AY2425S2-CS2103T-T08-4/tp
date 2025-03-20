package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMPLOYMENT_TYPE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_JOB_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_JOB_COMPANY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_JOB_ROUNDS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_JOB_SKILLS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_JOB_TITLE;

import java.util.stream.Stream;

import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.commands.DeleteJobCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.job.Job;
import seedu.address.model.job.JobAddress;
import seedu.address.model.job.JobCompany;
import seedu.address.model.job.JobRounds;
import seedu.address.model.job.JobSkills;
import seedu.address.model.job.JobTitle;
import seedu.address.model.job.JobType;

/**
 * Parses input arguments and creates a new DeleteJobCommand object
 */
public class DeleteJobCommandParser implements Parser<DeleteJobCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the DeleteJobCommand
     * and returns a DeleteJobCommand object for execution.
     *
     * @throws ParseException if the user input does not conform the expected format
     */
    public DeleteJobCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_JOB_TITLE, PREFIX_JOB_COMPANY,
                PREFIX_JOB_ROUNDS, PREFIX_JOB_SKILLS, PREFIX_JOB_ADDRESS, PREFIX_EMPLOYMENT_TYPE);
        if (!arePrefixesPresent(argMultimap, PREFIX_JOB_TITLE, PREFIX_JOB_COMPANY, PREFIX_JOB_ROUNDS,
                PREFIX_JOB_SKILLS, PREFIX_JOB_ADDRESS, PREFIX_EMPLOYMENT_TYPE)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_JOB_TITLE, PREFIX_JOB_COMPANY, PREFIX_JOB_ROUNDS);
        JobTitle title = ParserUtil.parseJobTitle(argMultimap.getValue(PREFIX_JOB_TITLE).get());
        JobCompany jobCompany = ParserUtil.parseJobCompany(argMultimap.getValue(PREFIX_JOB_COMPANY).get());
        JobRounds jobRounds = ParserUtil.parseJobRounds(argMultimap.getValue(PREFIX_JOB_ROUNDS).get());
        JobSkills jobSkills = ParserUtil.parseJobSkills(argMultimap.getValue(PREFIX_JOB_SKILLS).get());
        JobAddress jobAddress = ParserUtil.parseJobAddress(argMultimap.getValue(PREFIX_JOB_ADDRESS).get());
        JobType jobType = ParserUtil.parseJobType(argMultimap.getValue(PREFIX_EMPLOYMENT_TYPE).get());
        Job job = new Job(title, jobCompany, jobRounds, jobSkills, jobAddress, jobType);

        return new DeleteJobCommand(job);
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values
     * in the given {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }

}
