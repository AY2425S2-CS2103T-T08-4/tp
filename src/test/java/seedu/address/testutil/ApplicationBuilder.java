package seedu.address.testutil;

import seedu.address.model.application.Application;
import seedu.address.model.application.ApplicationStatus;
import seedu.address.model.job.Job;
import seedu.address.model.person.Person;

/**
 * A utility class to help with building Application objects.
 */
public class ApplicationBuilder {
    public static final int DEFAULT_APPLICATION_STATUS = 0;

    private Person applicant;
    private Job job;
    private ApplicationStatus applicationStatus;

    /**
     * Creates an {@code ApplicationBuilder} with default details.
     * Uses the first person and job from typical test data.
     */
    public ApplicationBuilder() {
        this.applicant = TypicalPersons.ALICE;
        this.job = TypicalPersons.SOFTWARE_ENGINEER_GOOGLE;
        this.applicationStatus = new ApplicationStatus(DEFAULT_APPLICATION_STATUS);
    }

    /**
     * Initializes the ApplicationBuilder with the data of
     * {@code applicationToCopy}.
     */
    public ApplicationBuilder(Application applicationToCopy) {
        this.applicant = applicationToCopy.getApplicant();
        this.job = applicationToCopy.getJob();
        this.applicationStatus = applicationToCopy.getApplicationStatus();
    }

    /**
     * Sets the {@code Person} of the {@code Application} that we are building.
     */
    public ApplicationBuilder withApplicant(Person applicant) {
        this.applicant = applicant;
        return this;
    }

    /**
     * Sets the {@code Job} of the {@code Application} that we are building.
     */
    public ApplicationBuilder withJob(Job job) {
        this.job = job;
        return this;
    }

    /**
     * Sets the {@code ApplicationStatus} of the {@code Application} that we are
     * building.
     */
    public ApplicationBuilder withApplicationStatus(int applicationStatus) {
        this.applicationStatus = new ApplicationStatus(applicationStatus);
        return this;
    }

    /**
     * Builds and returns an Application with the current attributes.
     */
    public Application build() {
        return new Application(applicant, job, applicationStatus);
    }
}
