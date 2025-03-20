package seedu.address.model.application;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import javafx.collections.ObservableList;
import seedu.address.model.application.exceptions.DuplicateApplicationException;
import seedu.address.model.application.exceptions.InvalidApplicationStatusException;
import seedu.address.model.job.Job;
import seedu.address.model.job.JobCompany;
import seedu.address.model.job.JobTitle;
import seedu.address.model.job.UniqueJobList;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.UniquePersonList;

/**
 * Represents a job application with an applicant, job, and status.
 */
public class Application {
    private final Person applicant;
    private final Job job;
    private final ApplicationStatus applicationStatus;
    private final UniquePersonList personList;
    private final UniqueJobList jobList;
    private final UniqueApplicationList applicationList;

    /**
     * Constructs an Application with the specified applicant, job, and status, automating the process in background for
     * user vis-à-vis requiring them to laboriously type in all fields.
     * @param phone         The phone number of the person applying for the job.
     * @param jobTitle     The job being applied for.
     * @param jobCompany   The company name of the job being applied for.
     * @param applicationStatus The current status of the application.
     * @param personList        The list of unique persons from which to find the applicant by phone number
     * @param jobList           The list of unique jobs from which to find the job posting by job title and company name
     * @param applicationList   The list of unique applications necessary for stack-filter test on strict candidate key
     * @throws InvalidApplicationStatusException if status exceeds job rounds.
     */
    public Application(Phone phone, JobTitle jobTitle, JobCompany jobCompany, ApplicationStatus applicationStatus,
                       UniquePersonList personList, UniqueJobList jobList, UniqueApplicationList applicationList) {
        requireAllNonNull(phone, jobTitle, jobCompany, applicationStatus, personList, jobList, applicationList);
        this.personList = personList;
        this.jobList = jobList;
        this.applicationList = applicationList;
        ObservableList<Person> filteredPersons = personList.searchPersonByPhone(phone);
        ObservableList<Job> filteredJobs = jobList.searchJobByTitleAndCompany(jobTitle, jobCompany);
        ObservableList<Application> filteredApplications = applicationList.searchApplications(
                phone, jobTitle, jobCompany, applicationStatus);
        if (!filteredApplications.isEmpty()) {
            throw new DuplicateApplicationException();
        }
        if (filteredPersons.isEmpty() || filteredJobs.isEmpty()) {
            throw new InvalidApplicationStatusException();
        }
        this.applicant = filteredPersons.get(0);
        this.job = filteredJobs.get(0);
        if (this.job.getJobRounds().jobRounds < applicationStatus.applicationStatus()) {
            throw new InvalidApplicationStatusException();
        }
        this.applicationStatus = applicationStatus;
    }

    /**
     * Returns the applicant of this application.
     * @return The applicant.
     */
    public Person getApplicant() {
        return this.applicant;
    }

    /**
     * Returns the job of this application.
     * @return The job.
     */
    public Job getJob() {
        return this.job;
    }

    /**
     * Returns the status of this application.
     * @return The application status.
     */
    public ApplicationStatus getApplicationStatus() {
        return this.applicationStatus;
    }

    public UniquePersonList getPersonList() {
        return this.personList;
    }

    public UniqueJobList getJobList() {
        return this.jobList;
    }

    public UniqueApplicationList getApplicationList() {
        return this.applicationList;
    }

    /**
     * Advances the application status by the specified number of rounds. Returns a
     * new Application with the updated status.
     * @param rounds The number of rounds to advance.
     * @return A new Application with the updated status.
     * @throws IllegalArgumentException          if rounds is negative.
     * @throws InvalidApplicationStatusException if the new status would exceed job
     *                                           rounds.
     */
    public Application advance(int rounds) {
        if (rounds < 0) {
            throw new IllegalArgumentException("Cannot advance by a negative number of rounds");
        }
        int newStatus = this.applicationStatus.applicationStatus() + rounds;

        // Create new application with updated status
        return new Application(this.applicant.getPhone(), this.job.getJobTitle(), this.job.getJobCompany(),
                new ApplicationStatus(newStatus), this.personList, this.jobList, this.applicationList);
    }

    /**
     * Returns true if this application is the same as the specified object.
     *
     * @param other The object to compare to.
     * @return True if the objects are equal, false otherwise.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        // instanceof handles nulls.
        if (!(other instanceof Application otherApplication)) {
            return false;
        }
        return this.applicant.equals(otherApplication.applicant) && this.job.equals(otherApplication.job)
                && this.applicationStatus.equals(otherApplication.applicationStatus);
    }

    /**
     * Returns a string representation of the application.
     * @return A string representation.
     */
    @Override
    public String toString() {
        return String.format("Application: %s at %s (Status: %d/%d)",
                this.job.getJobTitle(),
                this.job.getJobCompany(),
                this.applicationStatus.applicationStatus(),
                this.job.getJobRounds().jobRounds);
    }
}
