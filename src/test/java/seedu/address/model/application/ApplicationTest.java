package seedu.address.model.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BOB;
import static seedu.address.testutil.TypicalPersons.DATA_SCIENTIST_MICROSOFT;
import static seedu.address.testutil.TypicalPersons.SOFTWARE_ENGINEER_GOOGLE;

import org.junit.jupiter.api.Test;

import seedu.address.model.application.exceptions.InvalidApplicationStatusException;
import seedu.address.model.job.Job;
import seedu.address.model.person.Person;
import seedu.address.testutil.ApplicationBuilder;
import seedu.address.testutil.TypicalApplications;

public class ApplicationTest {

    @Test
    public void constructor_invalidApplicationStatus_throwsInvalidApplicationStatusException() {
        Person applicant = ALICE;
        Job job = SOFTWARE_ENGINEER_GOOGLE;

        // application status exceeds job rounds
        int jobRounds = job.getJobRounds().jobRounds;
        ApplicationStatus invalidStatus = new ApplicationStatus(jobRounds + 1);
        assertThrows(InvalidApplicationStatusException.class, () -> new Application(applicant, job, invalidStatus));
    }

    @Test
    public void constructor_validApplicationStatus_success() {
        Person applicant = ALICE;
        Job job = SOFTWARE_ENGINEER_GOOGLE;

        // application status equals job rounds (valid edge case)
        int jobRounds = job.getJobRounds().jobRounds;
        ApplicationStatus validStatus = new ApplicationStatus(jobRounds);
        new Application(applicant, job, validStatus); // Should not throw exception
    }

    @Test
    public void advance_negativeRounds_throwsIllegalArgumentException() {
        Application application = TypicalApplications.ALICE_SWE_GOOGLE;
        assertThrows(IllegalArgumentException.class, () -> application.advance(-1));
    }

    @Test
    public void advance_exceedJobRounds_throwsInvalidApplicationStatusException() {
        Application application = TypicalApplications.ALICE_SWE_GOOGLE;
        Job job = application.getJob();
        int currentStatus = application.getApplicationStatus().applicationStatus;
        int jobRounds = job.getJobRounds().jobRounds;
        int roundsToExceed = jobRounds - currentStatus + 1;

        assertThrows(InvalidApplicationStatusException.class, () -> application.advance(roundsToExceed));
    }

    @Test
    public void advance_validRounds_returnsNewApplicationWithUpdatedStatus() {
        Application application = TypicalApplications.ALICE_SWE_GOOGLE;
        int currentStatus = application.getApplicationStatus().applicationStatus;

        Application advancedApplication = application.advance(1);

        // Check that the original application is unchanged
        assertEquals(currentStatus, application.getApplicationStatus().applicationStatus);

        // Check that the advanced application has updated status
        assertEquals(currentStatus + 1, advancedApplication.getApplicationStatus().applicationStatus);

        // Check that other properties remain the same
        assertEquals(application.getApplicant(), advancedApplication.getApplicant());
        assertEquals(application.getJob(), advancedApplication.getJob());
    }

    @Test
    public void equals() {
        Application application = TypicalApplications.ALICE_SWE_GOOGLE;

        // same object -> returns true
        assertTrue(application.equals(application));

        // null -> returns false
        assertFalse(application.equals(null));

        // different type -> returns false
        assertFalse(application.equals(5));

        // same properties -> returns true
        Application applicationCopy = new Application(
                application.getApplicant(),
                application.getJob(),
                application.getApplicationStatus());
        assertTrue(application.equals(applicationCopy));

        // different applicant -> returns false
        Application differentApplicant = new ApplicationBuilder(application)
                .withApplicant(BOB)
                .build();
        assertFalse(application.equals(differentApplicant));

        // different job -> returns false
        Application differentJob = new ApplicationBuilder(application)
                .withJob(DATA_SCIENTIST_MICROSOFT)
                .build();
        assertFalse(application.equals(differentJob));

        // different status -> returns false
        Application differentStatus = new ApplicationBuilder(application)
                .withApplicationStatus(application.getApplicationStatus().applicationStatus + 1)
                .build();
        assertFalse(application.equals(differentStatus));
    }

    @Test
    public void toString_returnsFormattedString() {
        Application application = TypicalApplications.ALICE_SWE_GOOGLE;
        String expected = String.format("Application: %s (Status: %d/%d)",
                application.getJob().getJobTitle(),
                application.getApplicationStatus().applicationStatus,
                application.getJob().getJobRounds().jobRounds);
        assertEquals(expected, application.toString());
    }
}
