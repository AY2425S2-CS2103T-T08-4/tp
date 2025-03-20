package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_APPLICATION_STATUS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DEGREE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMPLOYMENT_TYPE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_JOB_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_JOB_COMPANY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_JOB_ROUNDS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_JOB_SKILLS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_JOB_TITLE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SCHOOL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import seedu.address.logic.commands.AddApplicationCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.application.Application;
import seedu.address.model.application.ApplicationStatus;
import seedu.address.model.application.UniqueApplicationList;
import seedu.address.model.job.JobCompany;
import seedu.address.model.job.JobTitle;
import seedu.address.model.job.UniqueJobList;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;
import seedu.address.model.person.UniquePersonList;

import java.util.stream.Stream;

public class AddApplicationCommandParser implements Parser<AddApplicationCommand> {
    private final UniquePersonList existingPersonList = new UniquePersonList();
    private final UniqueJobList existingJobList = new UniqueJobList();
    private final UniqueApplicationList existingApplicationList; // Injected Dependency.

    public AddApplicationCommandParser(UniqueApplicationList existingApplicationList) {
        this.existingApplicationList = existingApplicationList;
    }

    public AddApplicationCommand parse(String args) throws ParseException {
        // Still enable tokenization of all Prefixes below lest end-user opts so.
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, // Person prefixes below.
                PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL, PREFIX_ADDRESS, PREFIX_SCHOOL, PREFIX_DEGREE, PREFIX_TAG,
                PREFIX_JOB_TITLE, PREFIX_JOB_COMPANY, PREFIX_JOB_ROUNDS, // Job Prefixes here and below.
                PREFIX_JOB_SKILLS, PREFIX_JOB_ADDRESS, PREFIX_EMPLOYMENT_TYPE,
                PREFIX_APPLICATION_STATUS); // Application Prefixes here.
        // Essential Prefixes forming primary search key below.
        if (!arePrefixesPresent(argMultimap,
                PREFIX_NAME, PREFIX_JOB_TITLE, PREFIX_JOB_COMPANY, PREFIX_APPLICATION_STATUS)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    AddApplicationCommand.MESSAGE_USAGE));
        }
        // Similar to tokenization, still enable uniqueness-verification of all Prefixes below lest end-user opts so.
        argMultimap.verifyNoDuplicatePrefixesFor( // Person Prefixes below.
                PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL, PREFIX_ADDRESS, PREFIX_SCHOOL, PREFIX_DEGREE,
                PREFIX_JOB_TITLE, PREFIX_JOB_COMPANY, PREFIX_JOB_ROUNDS, // Job Prefixes here and below.
                PREFIX_JOB_SKILLS, PREFIX_JOB_ADDRESS, PREFIX_EMPLOYMENT_TYPE,
                PREFIX_APPLICATION_STATUS); // Application Prefix below.
        // Parse candidate details below.
        Phone phone = ParserUtil.parsePhone(argMultimap.getValue(PREFIX_PHONE).get());
        // Parse job details below.
        JobTitle title = ParserUtil.parseJobTitle(argMultimap.getValue(PREFIX_JOB_TITLE).get());
        JobCompany jobCompany = ParserUtil.parseJobCompany(argMultimap.getValue(PREFIX_JOB_COMPANY).get());
        // Parse Application details below.
        ApplicationStatus applicationStatus = ParserUtil.parseApplicationStatus(
                argMultimap.getValue(PREFIX_APPLICATION_STATUS).get());
        Application application = new Application(phone, title, jobCompany, applicationStatus,
                this.existingPersonList, this.existingJobList, this.existingApplicationList);
        return new AddApplicationCommand(application);
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values
     * in the given {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}
