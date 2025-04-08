package seedu.address.testutil;

import java.util.Set;

import seedu.address.model.job.Job;
import seedu.address.model.job.JobRounds;
import seedu.address.model.job.JobTitle;
import seedu.address.model.skill.Skill;

/**
 * A utility class containing a list of {@code Job} objects to be used in tests.
 */
public class TypicalJobs {
    public static final Job SOFTWARE_ENGINEER = new Job(
            new JobTitle("Software Engineer"),
            new JobRounds(3),
            Set.of(new Skill("Python"))
    );
}
