package seedu.address.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalApplications.ALICE_DS_MICROSOFT;
import static seedu.address.testutil.TypicalApplications.ALICE_SWE_GOOGLE;
import static seedu.address.testutil.TypicalApplications.BOB_SWE_GOOGLE;
import static seedu.address.testutil.TypicalApplications.getTypicalApplicationsManager;
import static seedu.address.testutil.TypicalPersons.ALICE;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.application.Application;
import seedu.address.model.application.exceptions.ApplicationNotFoundException;
import seedu.address.model.application.exceptions.InvalidApplicationStatusException;
import seedu.address.model.job.Job;
import seedu.address.model.person.Person;
import seedu.address.testutil.ApplicationBuilder;
import seedu.address.testutil.JobBuilder;
import seedu.address.testutil.PersonBuilder;
import seedu.address.testutil.TypicalPersons;

public class ApplicationsManagerTest {

    private final ApplicationsManager applicationsManager = new ApplicationsManager();

    @Test
    public void constructor() {
        assertEquals(Collections.emptyList(), applicationsManager.getApplicationList());
    }

    @Test
    public void resetData_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> applicationsManager.resetData(null));
    }

    @Test
    public void resetData_withValidReadOnlyApplicationsManager_replacesData() {
        ApplicationsManager newData = getTypicalApplicationsManager();
        applicationsManager.resetData(newData);
        assertEquals(newData, applicationsManager);
    }

    @Test
    public void hasApplication_nullApplication_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> applicationsManager.hasApplication(null));
    }

    @Test
    public void hasApplication_applicationNotInApplicationsManager_returnsFalse() {
        assertFalse(applicationsManager.hasApplication(ALICE_SWE_GOOGLE));
    }

    @Test
    public void hasApplication_applicationInApplicationsManager_returnsTrue() {
        applicationsManager.addApplication(ALICE_SWE_GOOGLE);
        assertTrue(applicationsManager.hasApplication(ALICE_SWE_GOOGLE));
    }

    @Test
    public void addApplication_nullApplication_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> applicationsManager.addApplication(null));
    }

    @Test
    public void addApplication_validApplication_applicationAdded() {
        applicationsManager.addApplication(ALICE_SWE_GOOGLE);
        assertTrue(applicationsManager.hasApplication(ALICE_SWE_GOOGLE));
    }

    @Test
    public void setApplication_nullTargetApplication_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> applicationsManager.setApplication(null, ALICE_SWE_GOOGLE));
    }

    @Test
    public void setApplication_nullEditedApplication_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> applicationsManager.setApplication(ALICE_SWE_GOOGLE, null));
    }

    @Test
    public void setApplication_targetApplicationNotInApplicationsManager_throwsApplicationNotFoundException() {
        assertThrows(ApplicationNotFoundException.class,
                () -> applicationsManager.setApplication(ALICE_SWE_GOOGLE, ALICE_SWE_GOOGLE));
    }

    @Test
    public void setApplication_validApplications_applicationUpdated() {
        applicationsManager.addApplication(ALICE_SWE_GOOGLE);

        Application editedApplication = new ApplicationBuilder(ALICE_SWE_GOOGLE)
                .withApplicationStatus(2)
                .build();

        applicationsManager.setApplication(ALICE_SWE_GOOGLE, editedApplication);

        ApplicationsManager expectedApplicationsManager = new ApplicationsManager();
        expectedApplicationsManager.addApplication(editedApplication);

        assertEquals(expectedApplicationsManager, applicationsManager);
    }

    @Test
    public void deleteApplication_nullApplication_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> applicationsManager.deleteApplication(null));
    }

    @Test
    public void deleteApplication_applicationNotInApplicationsManager_throwsApplicationNotFoundException() {
        assertThrows(ApplicationNotFoundException.class, () -> applicationsManager.deleteApplication(ALICE_SWE_GOOGLE));
    }

    @Test
    public void deleteApplication_validApplication_applicationRemoved() {
        applicationsManager.addApplication(ALICE_SWE_GOOGLE);
        applicationsManager.deleteApplication(ALICE_SWE_GOOGLE);
        assertFalse(applicationsManager.hasApplication(ALICE_SWE_GOOGLE));
    }

    @Test
    public void updatePerson_nullParameters_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> applicationsManager.updatePerson(null, ALICE));
        assertThrows(NullPointerException.class, () -> applicationsManager.updatePerson(ALICE, null));
    }

    @Test
    public void updatePerson_personNotInApplications_noChange() {
        // Add an application unrelated to the person being updated
        applicationsManager.addApplication(BOB_SWE_GOOGLE);

        // Try to update a person with no applications
        Person oldPerson = ALICE;
        Person newPerson = new PersonBuilder(ALICE).withName("Alice Updated").build();

        applicationsManager.updatePerson(oldPerson, newPerson);

        // ApplicationsManager should remain unchanged
        ApplicationsManager expectedApplicationsManager = new ApplicationsManager();
        expectedApplicationsManager.addApplication(BOB_SWE_GOOGLE);

        assertEquals(expectedApplicationsManager, applicationsManager);
    }

    @Test
    public void updatePerson_personInApplications_applicationsUpdated() {
        // Add applications for the person being updated
        applicationsManager.addApplication(ALICE_SWE_GOOGLE);
        applicationsManager.addApplication(ALICE_DS_MICROSOFT);

        // Update the person
        Person oldPerson = ALICE;
        Person newPerson = new PersonBuilder(ALICE).withName("Alice Updated").build();

        applicationsManager.updatePerson(oldPerson, newPerson);

        // Check that all applications were updated
        for (Application app : applicationsManager.getApplicationList()) {
            if (app.getApplicant().equals(newPerson)) {
                assertEquals(newPerson, app.getApplicant());
            }
        }

        // Verify old applications are no longer in the manager
        assertFalse(applicationsManager.hasApplication(ALICE_SWE_GOOGLE));
        assertFalse(applicationsManager.hasApplication(ALICE_DS_MICROSOFT));
    }

    @Test
    public void updateJob_nullParameters_throwsNullPointerException() {
        assertThrows(NullPointerException.class,
                () -> applicationsManager.updateJob(null, TypicalPersons.SOFTWARE_ENGINEER_GOOGLE));
        assertThrows(NullPointerException.class,
                () -> applicationsManager.updateJob(TypicalPersons.SOFTWARE_ENGINEER_GOOGLE, null));
    }

    @Test
    public void updateJob_jobNotInApplications_noChange() {
        // Add an application unrelated to the job being updated
        applicationsManager.addApplication(ALICE_DS_MICROSOFT);

        // Try to update a job with no applications
        Job oldJob = TypicalPersons.SOFTWARE_ENGINEER_GOOGLE;
        Job newJob = new JobBuilder(oldJob).withJobTitle("Updated Software Engineer").build();

        applicationsManager.updateJob(oldJob, newJob);

        // ApplicationsManager should remain unchanged
        ApplicationsManager expectedApplicationsManager = new ApplicationsManager();
        expectedApplicationsManager.addApplication(ALICE_DS_MICROSOFT);

        assertEquals(expectedApplicationsManager, applicationsManager);
    }

    @Test
    public void updateJob_jobInApplications_applicationsUpdated() {
        // Add applications for the job being updated
        applicationsManager.addApplication(ALICE_SWE_GOOGLE);
        applicationsManager.addApplication(BOB_SWE_GOOGLE);

        // Update the job
        Job oldJob = TypicalPersons.SOFTWARE_ENGINEER_GOOGLE;
        Job newJob = new JobBuilder(oldJob).withJobTitle("Updated Software Engineer").build();

        applicationsManager.updateJob(oldJob, newJob);

        // Check that all applications were updated
        for (Application app : applicationsManager.getApplicationList()) {
            if (app.getJob().equals(newJob)) {
                assertEquals(newJob, app.getJob());
            }
        }

        // Verify old applications are no longer in the manager
        assertFalse(applicationsManager.hasApplication(ALICE_SWE_GOOGLE));
        assertFalse(applicationsManager.hasApplication(BOB_SWE_GOOGLE));
    }

    @Test
    public void updateJob_decreasedJobRounds_applicationsUpdatedSuccessfully() {
        // Add application with status 2
        applicationsManager.addApplication(BOB_SWE_GOOGLE);

        // Update job to have 3 rounds (application with status 2 is still valid)
        Job oldJob = TypicalPersons.SOFTWARE_ENGINEER_GOOGLE;
        Job newJob = new JobBuilder(oldJob).withJobRounds(3).build();

        applicationsManager.updateJob(oldJob, newJob);

        // Verify application was updated with new job
        Optional<Application> updatedApp = applicationsManager.getApplicationByPersonAndJob(
                BOB_SWE_GOOGLE.getApplicant(), newJob);
        assertTrue(updatedApp.isPresent());
        assertEquals(newJob, updatedApp.get().getJob());
    }

    @Test
    public void updateJob_invalidApplicationStatus_throwsInvalidApplicationStatusException() {
        // Add application with status 3
        Application appWithStatus3 = new ApplicationBuilder(BOB_SWE_GOOGLE)
                .withApplicationStatus(3)
                .build();

        applicationsManager.addApplication(appWithStatus3);

        // Update job to have only 2 rounds (making the status 3 invalid)
        Job oldJob = TypicalPersons.SOFTWARE_ENGINEER_GOOGLE;
        Job newJob = new JobBuilder(oldJob).withJobRounds(2).build();

        // Should throw exception since application status exceeds new job rounds
        assertThrows(InvalidApplicationStatusException.class, () -> applicationsManager.updateJob(oldJob, newJob));
    }

    @Test
    public void removePersonApplications_nullPerson_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> applicationsManager.removePersonApplications(null));
    }

    @Test
    public void removePersonApplications_personWithNoApplications_noChange() {
        // Add an application for a different person
        applicationsManager.addApplication(BOB_SWE_GOOGLE);

        // Try to remove applications for a person with no applications
        applicationsManager.removePersonApplications(ALICE);

        // ApplicationsManager should remain unchanged
        ApplicationsManager expectedApplicationsManager = new ApplicationsManager();
        expectedApplicationsManager.addApplication(BOB_SWE_GOOGLE);

        assertEquals(expectedApplicationsManager, applicationsManager);
    }

    @Test
    public void removePersonApplications_personWithApplications_applicationsRemoved() {
        // Add applications for multiple persons
        applicationsManager.addApplication(ALICE_SWE_GOOGLE);
        applicationsManager.addApplication(ALICE_DS_MICROSOFT);
        applicationsManager.addApplication(BOB_SWE_GOOGLE);

        // Remove all applications for ALICE
        applicationsManager.removePersonApplications(ALICE);

        // Verify ALICE's applications are removed, but BOB's remain
        assertFalse(applicationsManager.hasApplication(ALICE_SWE_GOOGLE));
        assertFalse(applicationsManager.hasApplication(ALICE_DS_MICROSOFT));
        assertTrue(applicationsManager.hasApplication(BOB_SWE_GOOGLE));
    }

    @Test
    public void removeJobApplications_nullJob_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> applicationsManager.removeJobApplications(null));
    }

    @Test
    public void removeJobApplications_jobWithNoApplications_noChange() {
        // Add an application for a different job
        applicationsManager.addApplication(ALICE_DS_MICROSOFT);

        // Try to remove applications for a job with no applications
        applicationsManager.removeJobApplications(TypicalPersons.SOFTWARE_ENGINEER_GOOGLE);

        // ApplicationsManager should remain unchanged
        ApplicationsManager expectedApplicationsManager = new ApplicationsManager();
        expectedApplicationsManager.addApplication(ALICE_DS_MICROSOFT);

        assertEquals(expectedApplicationsManager, applicationsManager);
    }

    @Test
    public void removeJobApplications_jobWithApplications_applicationsRemoved() {
        // Add applications for multiple jobs
        applicationsManager.addApplication(ALICE_SWE_GOOGLE);
        applicationsManager.addApplication(BOB_SWE_GOOGLE);
        applicationsManager.addApplication(ALICE_DS_MICROSOFT);

        // Remove all applications for SOFTWARE_ENGINEER_GOOGLE
        applicationsManager.removeJobApplications(TypicalPersons.SOFTWARE_ENGINEER_GOOGLE);

        // Verify SOFTWARE_ENGINEER_GOOGLE applications are removed, but others remain
        assertFalse(applicationsManager.hasApplication(ALICE_SWE_GOOGLE));
        assertFalse(applicationsManager.hasApplication(BOB_SWE_GOOGLE));
        assertTrue(applicationsManager.hasApplication(ALICE_DS_MICROSOFT));
    }

    @Test
    public void getApplicationsByPerson_nullPerson_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> applicationsManager.getApplicationsByPerson(null));
    }

    @Test
    public void getApplicationsByPerson_personWithNoApplications_returnsEmptyList() {
        // Add an application for a different person
        applicationsManager.addApplication(BOB_SWE_GOOGLE);

        // Get applications for a person with no applications
        List<Application> applications = applicationsManager.getApplicationsByPerson(ALICE);

        // List should be empty
        assertTrue(applications.isEmpty());
    }

    @Test
    public void getApplicationsByPerson_personWithApplications_returnsApplicationsList() {
        // Add applications for multiple persons
        applicationsManager.addApplication(ALICE_SWE_GOOGLE);
        applicationsManager.addApplication(ALICE_DS_MICROSOFT);
        applicationsManager.addApplication(BOB_SWE_GOOGLE);

        // Get applications for ALICE
        List<Application> applications = applicationsManager.getApplicationsByPerson(ALICE);

        // Should return both of ALICE's applications
        assertEquals(2, applications.size());
        assertTrue(applications.contains(ALICE_SWE_GOOGLE));
        assertTrue(applications.contains(ALICE_DS_MICROSOFT));
    }

    @Test
    public void getApplicationsByJob_nullJob_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> applicationsManager.getApplicationsByJob(null));
    }

    @Test
    public void getApplicationsByJob_jobWithNoApplications_returnsEmptyList() {
        // Add an application for a different job
        applicationsManager.addApplication(ALICE_DS_MICROSOFT);

        // Get applications for a job with no applications
        List<Application> applications = applicationsManager.getApplicationsByJob(
                TypicalPersons.SOFTWARE_ENGINEER_GOOGLE);

        // List should be empty
        assertTrue(applications.isEmpty());
    }

    @Test
    public void getApplicationsByJob_jobWithApplications_returnsApplicationsList() {
        // Add applications for multiple jobs
        applicationsManager.addApplication(ALICE_SWE_GOOGLE);
        applicationsManager.addApplication(BOB_SWE_GOOGLE);
        applicationsManager.addApplication(ALICE_DS_MICROSOFT);

        // Get applications for SOFTWARE_ENGINEER_GOOGLE
        List<Application> applications = applicationsManager.getApplicationsByJob(
                TypicalPersons.SOFTWARE_ENGINEER_GOOGLE);

        // Should return both SOFTWARE_ENGINEER_GOOGLE applications
        assertEquals(2, applications.size());
        assertTrue(applications.contains(ALICE_SWE_GOOGLE));
        assertTrue(applications.contains(BOB_SWE_GOOGLE));
    }

    @Test
    public void getApplicationByPersonAndJob_nullParameters_throwsNullPointerException() {
        assertThrows(NullPointerException.class,
                () -> applicationsManager.getApplicationByPersonAndJob(null, TypicalPersons.SOFTWARE_ENGINEER_GOOGLE));
        assertThrows(NullPointerException.class, () -> applicationsManager.getApplicationByPersonAndJob(ALICE, null));
    }

    @Test
    public void getApplicationByPersonAndJob_applicationDoesNotExist_returnsEmptyOptional() {
        // Add a different application
        applicationsManager.addApplication(BOB_SWE_GOOGLE);

        // Get application for a person-job combination that doesn't exist
        Optional<Application> application = applicationsManager.getApplicationByPersonAndJob(
                ALICE, TypicalPersons.SOFTWARE_ENGINEER_GOOGLE);

        // Should return empty optional
        assertFalse(application.isPresent());
    }

    @Test
    public void getApplicationByPersonAndJob_applicationExists_returnsApplication() {
        // Add multiple applications
        applicationsManager.addApplication(ALICE_SWE_GOOGLE);
        applicationsManager.addApplication(ALICE_DS_MICROSOFT);
        applicationsManager.addApplication(BOB_SWE_GOOGLE);

        // Get application for a specific person-job combination
        Optional<Application> application = applicationsManager.getApplicationByPersonAndJob(
                ALICE, TypicalPersons.SOFTWARE_ENGINEER_GOOGLE);

        // Should return the correct application
        assertTrue(application.isPresent());
        assertEquals(ALICE_SWE_GOOGLE, application.get());
    }

    @Test
    public void advanceApplication_nullApplication_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> applicationsManager.advanceApplication(null, 1));
    }

    @Test
    public void advanceApplication_applicationNotInManager_throwsApplicationNotFoundException() {
        assertThrows(ApplicationNotFoundException.class,
                () -> applicationsManager.advanceApplication(ALICE_SWE_GOOGLE, 1));
    }

    @Test
    public void advanceApplication_invalidRounds_throwsInvalidApplicationStatusException() {
        // Add application
        applicationsManager.addApplication(ALICE_SWE_GOOGLE);

        // Try to advance by too many rounds
        int jobRounds = ALICE_SWE_GOOGLE.getJob().getJobRounds().jobRounds;
        int currentStatus = ALICE_SWE_GOOGLE.getApplicationStatus().applicationStatus;
        int roundsToExceed = jobRounds - currentStatus + 1;

        assertThrows(InvalidApplicationStatusException.class,
                () -> applicationsManager.advanceApplication(ALICE_SWE_GOOGLE, roundsToExceed));
    }

    @Test
    public void advanceApplication_validRounds_returnsAdvancedApplication() {
        // Add application
        applicationsManager.addApplication(ALICE_SWE_GOOGLE);

        // Current status
        int currentStatus = ALICE_SWE_GOOGLE.getApplicationStatus().applicationStatus;

        // Advance by 1 round
        Application advancedApplication = applicationsManager.advanceApplication(ALICE_SWE_GOOGLE, 1);

        // Check that application was advanced
        assertEquals(currentStatus + 1, advancedApplication.getApplicationStatus().applicationStatus);

        // Check that the application in the manager was updated
        Optional<Application> updatedApp = applicationsManager.getApplicationByPersonAndJob(
                ALICE, TypicalPersons.SOFTWARE_ENGINEER_GOOGLE);
        assertTrue(updatedApp.isPresent());
        assertEquals(currentStatus + 1, updatedApp.get().getApplicationStatus().applicationStatus);
    }

    @Test
    public void toString_returnsCorrectString() {
        // Empty ApplicationsManager
        String expectedString = new ToStringBuilder(applicationsManager)
                .add("applications", applicationsManager.getApplicationList())
                .toString();
        assertEquals(expectedString, applicationsManager.toString());

        // Non-empty ApplicationsManager
        applicationsManager.addApplication(ALICE_SWE_GOOGLE);
        expectedString = new ToStringBuilder(applicationsManager)
                .add("applications", applicationsManager.getApplicationList())
                .toString();
        assertEquals(expectedString, applicationsManager.toString());
    }

    @Test
    public void equals() {
        // same values -> returns true
        ApplicationsManager applicationsManagerCopy = new ApplicationsManager();
        assertTrue(applicationsManager.equals(applicationsManagerCopy));

        // same object -> returns true
        assertTrue(applicationsManager.equals(applicationsManager));

        // null -> returns false
        assertFalse(applicationsManager.equals(null));

        // different types -> returns false
        assertFalse(applicationsManager.equals(5));

        // different applications -> returns false
        ApplicationsManager differentApplicationsManager = new ApplicationsManager();
        differentApplicationsManager.addApplication(ALICE_SWE_GOOGLE);
        assertFalse(applicationsManager.equals(differentApplicationsManager));

        // same applications -> returns true
        applicationsManager.addApplication(ALICE_SWE_GOOGLE);
        assertTrue(applicationsManager.equals(differentApplicationsManager));
    }
}
