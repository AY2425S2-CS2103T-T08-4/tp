package seedu.address.logic.parser;

/**
 * Contains Command Line Interface (CLI) syntax definitions common to multiple
 * commands
 */
public class CliSyntax {

    /* Prefix definitions */
    public static final Prefix PREFIX_NAME = new Prefix("n/");
    public static final Prefix PREFIX_PHONE = new Prefix("p/");
    public static final Prefix PREFIX_EMAIL = new Prefix("e/");
    public static final Prefix PREFIX_ADDRESS = new Prefix("a/");
    public static final Prefix PREFIX_TAG = new Prefix("t/");
    public static final Prefix PREFIX_SCHOOL = new Prefix("s/");
    public static final Prefix PREFIX_DEGREE = new Prefix("d/");
    public static final Prefix PREFIX_JOB_TITLE = new Prefix("jt/");
    public static final Prefix PREFIX_JOB_ROUNDS = new Prefix("jr/");
    public static final Prefix PREFIX_JOB_SKILLS = new Prefix("js/");
    public static final Prefix PREFIX_EMPLOYMENT_TYPE = new Prefix("em/"); // Correspond to job type.
    public static final Prefix PREFIX_JOB_INDEX = new Prefix("ji/");
    public static final Prefix PREFIX_PERSON_INDEX = new Prefix("pi/");
    public static final Prefix PREFIX_ROUNDS = new Prefix("r/");
}
