package seedu.address.model.job;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class JobRoundsTest {

    @Test
    public void constructor_invalidJobRounds_throwsIllegalArgumentException() {
        int invalidJobRound = 20;
        assertThrows(IllegalArgumentException.class, () -> new JobRounds(invalidJobRound));
    }

    @Test
    public void isValidJobRounds() {
        // invalid job rounds
        assertFalse(JobRounds.isValidJobRounds(-2)); // negative job rounds
        assertFalse(JobRounds.isValidJobRounds(20)); // more than max job rounds
        assertFalse(JobRounds.isValidJobRounds(0)); // no job rounds

        // valid job rounds
        assertTrue(JobRounds.isValidJobRounds(10)); // exactly max job rounds
        assertTrue(JobRounds.isValidJobRounds(1));
    }

    @Test
    public void equals() {
        JobRounds jobRounds = new JobRounds(5);

        // same values -> returns true
        assertTrue(jobRounds.equals(new JobRounds(5)));

        // same object -> returns true
        assertTrue(jobRounds.equals(jobRounds));

        // different types -> returns false
        assertFalse(jobRounds.equals("Hello"));

        // different values -> returns false
        assertFalse(jobRounds.equals(new JobRounds(1))); // less than job rounds
        assertFalse(jobRounds.equals(new JobRounds(7))); // more than job rounds
    }
}
