package seedu.address.logic;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import seedu.address.logic.parser.Prefix;
import seedu.address.model.job.Job;
import seedu.address.model.person.Person;

/**
 * Container for user visible messages.
 */
public class Messages {

    public static final String MESSAGE_UNKNOWN_COMMAND = "Unknown command";
    public static final String MESSAGE_INVALID_COMMAND_FORMAT = "Invalid command format! \n%1$s";
    public static final String MESSAGE_INVALID_PERSON_DISPLAYED_INDEX = "The person index provided is invalid";
    public static final String MESSAGE_INVALID_JOB_DISPLAYED_INDEX = "The job index provided is invalid";
    public static final String MESSAGE_PERSONS_LISTED_OVERVIEW = "%1$d persons listed!";
    public static final String MESSAGE_JOBS_LISTED_OVERVIEW = "%1$d jobs listed!";
    public static final String MESSAGE_DUPLICATE_FIELDS = "Multiple values specified "
            + "for the following single-valued field(s): ";

    /**
     * Returns an error message indicating the duplicate prefixes.
     */
    public static String getErrorMessageForDuplicatePrefixes(Prefix... duplicatePrefixes) {
        assert duplicatePrefixes.length > 0;

        Set<String> duplicateFields = Stream.of(duplicatePrefixes).map(Prefix::toString).collect(Collectors.toSet());

        return MESSAGE_DUPLICATE_FIELDS + String.join(" ", duplicateFields);
    }

    /**
     * Formats the {@code person} for display to the user.
     */
    public static String format(Person person) {
        final StringBuilder builder = new StringBuilder();
        builder.append(person.getName()).append(" ; Phone: ").append(person.getPhone()).append(" ; Email: ")
                .append(person.getEmail()).append(" ; Address: ").append(person.getAddress()).append(" ; School: ")
                .append(person.getSchool()).append(" ; Degree: ").append(person.getDegree()).append(" ; Skills: ");
        person.getSkills().forEach(skill -> builder.append(skill).append(" "));
        return builder.toString();
    }

    /**
     * Formats the {@code job} for display to the user.
     */
    public static String format(Job job) {
        final StringBuilder builder = new StringBuilder();
        builder.append(job.getJobTitle()).append(" ; Number of rounds: ").append(job.getJobRounds())
                .append(" ; Skills: ");
        job.getSkills().forEach(skill -> builder.append(skill).append(" "));
        return builder.toString();
    }

}
