package seedu.address.logic.parser;

/**
 * Contains Command Line Interface (CLI) syntax definitions common to multiple commands.
 */
public class CliSyntax {
    /** Prefixes */
    // Person-specific Prefixes below, all being a single character each.
    public static final Prefix PREFIX_NAME = new Prefix("n/");
    public static final Prefix PREFIX_PHONE = new Prefix("p/");
    public static final Prefix PREFIX_EMAIL = new Prefix("e/");
    public static final Prefix PREFIX_ADDRESS = new Prefix("a/");
    public static final Prefix PREFIX_SKILL = new Prefix("k/"); // k for "knowledge" or "know-how".
    public static final Prefix PREFIX_SCHOOL = new Prefix("s/");
    public static final Prefix PREFIX_DEGREE = new Prefix("d/");
    // Job-specific Prefixes below, all being a double character starting with 'j' each.
    public static final Prefix PREFIX_JOB_TITLE = new Prefix("jt/");
    public static final Prefix PREFIX_JOB_ROUNDS = new Prefix("jr/");
    // Application-specific Prefixes below, all being a double character starting with 'a' each.
    public static final Prefix PREFIX_APPLICATION_STATUS = new Prefix("as/");
    // Index-specific Prefixes below, all being a double character starting with 'i' each.
    public static final Prefix PREFIX_JOB_INDEX = new Prefix("ij/");
    public static final Prefix PREFIX_PERSON_INDEX = new Prefix("ip/");
    public static final Prefix PREFIX_APPLICATION_INDEX = new Prefix("ia/");
}
