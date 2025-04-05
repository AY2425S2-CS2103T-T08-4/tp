package seedu.address.model.job;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_JOB_ROUNDS_3;
import static seedu.address.logic.commands.CommandTestUtil.VALID_JOB_TITLE_DATA_SCIENTIST;
import static seedu.address.logic.commands.CommandTestUtil.VALID_SKILL_PYTHON;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.DATA_SCIENTIST_MICROSOFT;
import static seedu.address.testutil.TypicalPersons.SOFTWARE_ENGINEER_GOOGLE;

import org.junit.jupiter.api.Test;

import seedu.address.testutil.JobBuilder;

public class JobTest {
    @Test
    public void asObservableList_modifyList_throwsUnsupportedOperationException() {
        Job job = new JobBuilder().build();
        assertThrows(UnsupportedOperationException.class, () -> job.getSkills().remove(0));
    }

    @Test
    public void isSameJob() {
        // same object -> returns true
        assertTrue(SOFTWARE_ENGINEER_GOOGLE.isSameJob(SOFTWARE_ENGINEER_GOOGLE));

        // null -> return false
        assertFalse(SOFTWARE_ENGINEER_GOOGLE.isSameJob(null));

        // same job title, all other attributes different -> returns true
        Job editedJob = new JobBuilder(SOFTWARE_ENGINEER_GOOGLE).withJobRounds(VALID_JOB_ROUNDS_3)
                .withSkills(VALID_SKILL_PYTHON).build();
        assertTrue(SOFTWARE_ENGINEER_GOOGLE.isSameJob(editedJob));

        // different name, all other attributes same -> returns false
        editedJob = new JobBuilder(SOFTWARE_ENGINEER_GOOGLE).withJobTitle(VALID_JOB_TITLE_DATA_SCIENTIST).build();
        assertFalse(SOFTWARE_ENGINEER_GOOGLE.isSameJob(editedJob));

        // name differs in case, all other attributes same -> false
        Job editedJob2 = new JobBuilder(DATA_SCIENTIST_MICROSOFT)
                .withJobTitle(VALID_JOB_TITLE_DATA_SCIENTIST.toLowerCase()).build();
        assertFalse(DATA_SCIENTIST_MICROSOFT.isSameJob(editedJob2));

        // name with trailing spaces, all other attributes same -> return false
        String jobTitlewWithTrailingSpaces = VALID_JOB_TITLE_DATA_SCIENTIST + " ";
        editedJob2 = new JobBuilder(DATA_SCIENTIST_MICROSOFT).withJobTitle(jobTitlewWithTrailingSpaces).build();
        assertFalse(DATA_SCIENTIST_MICROSOFT.isSameJob(editedJob2));
    }

    @Test
    public void equals() {
        // same values -> return true
        Job sweCopy = new JobBuilder(SOFTWARE_ENGINEER_GOOGLE).build();
        assertTrue(SOFTWARE_ENGINEER_GOOGLE.equals(sweCopy));

        // same object -> returns true
        assertTrue(SOFTWARE_ENGINEER_GOOGLE.equals(SOFTWARE_ENGINEER_GOOGLE));

        // null -> returns false
        assertFalse(SOFTWARE_ENGINEER_GOOGLE.equals(null));

        // different type -> returns false
        assertFalse(SOFTWARE_ENGINEER_GOOGLE.equals(5));

        // different job title -> returns false
        Job editedSwe = new JobBuilder(SOFTWARE_ENGINEER_GOOGLE).withJobTitle(VALID_JOB_TITLE_DATA_SCIENTIST).build();
        assertFalse(SOFTWARE_ENGINEER_GOOGLE.equals(editedSwe));

        // different job rounds -> return false
        editedSwe = new JobBuilder(SOFTWARE_ENGINEER_GOOGLE).withJobRounds(VALID_JOB_ROUNDS_3).build();
        assertFalse(SOFTWARE_ENGINEER_GOOGLE.equals(editedSwe));

        // different skills -> returns false
        editedSwe = new JobBuilder(SOFTWARE_ENGINEER_GOOGLE).withSkills(VALID_SKILL_PYTHON).build();
        assertFalse(SOFTWARE_ENGINEER_GOOGLE.equals(editedSwe));
    }
}
