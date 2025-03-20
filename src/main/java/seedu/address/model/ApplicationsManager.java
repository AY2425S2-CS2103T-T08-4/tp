package seedu.address.model;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.application.Application;
import seedu.address.model.application.UniqueApplicationList;
import seedu.address.model.application.exceptions.ApplicationNotFoundException;
import seedu.address.model.application.exceptions.InvalidApplicationStatusException;
import seedu.address.model.job.Job;
import seedu.address.model.job.UniqueJobList;
import seedu.address.model.person.Person;
import seedu.address.model.person.UniquePersonList;

/**
 * Wraps all application data at the applications-manager level Duplicates are
 * not allowed (by Application::equals comparison)
 */
public class ApplicationsManager implements ReadOnlyApplicationsManager {
    private final UniquePersonList personList;
    private final UniqueJobList jobList;
    private final UniqueApplicationList applicationList;

    public ApplicationsManager() {
        this.personList = new UniquePersonList();
        this.jobList = new UniqueJobList();
        this.applicationList = new UniqueApplicationList();
    }

    /**
     * Creates an ApplicationsManager using the Applications in the
     * {@code toBeCopied}
     */
    public ApplicationsManager(ReadOnlyApplicationsManager toBeCopied) {
        this();
        resetData(toBeCopied);
    }

    //// list overwrite operations

    /**
     * Replaces the contents of the application list with {@code applications}.
     * {@code applications} must not contain duplicate applications.
     */
    public void setApplicationList(UniqueApplicationList applicationList) {
        this.applicationList.setApplications(applicationList);
    }

    /**
     * Resets the existing data of this {@code ApplicationsManager} with {@code newData}.
     */
    public void resetData(ReadOnlyApplicationsManager newData) {
        requireNonNull(newData);
        this.setApplicationList(newData.getUniqueApplicationList());
    }

    //// application-level operations

    /**
     * Returns true if an application identical to {@code application} exists in the
     * applications manager.
     */
    public boolean hasApplication(Application application) {
        requireNonNull(application);
        return this.applicationList.contains(application);
    }

    /**
     * Adds an application to the applications manager. The application must not
     * already exist in the applications manager.
     */
    public void addApplication(Application application) {
        this.applicationList.add(application);
    }

    /**
     * Replaces the given application {@code target} in the list with
     * {@code editedApplication}. {@code target} must exist in the applications
     * manager. The application identity of {@code editedApplication} must not be
     * the same as another existing application in the applications manager.
     */
    public void setApplication(Application target, Application editedApplication) {
        requireNonNull(editedApplication);
        this.applicationList.setApplication(target, editedApplication);
    }

    /**
     * Removes {@code key} from this {@code ApplicationsManager}. {@code key} must exist in the applications manager.
     */
    public void removeApplication(Application key) {
        this.applicationList.remove(key);
    }

    /**
     * Updates all applications involving {@code person} after the person has been
     * modified.
     *
     * @param oldPerson The person before modification
     * @param newPerson The person after modification
     */
    public void updatePerson(Person oldPerson, Person newPerson) {
        requireNonNull(oldPerson);
        requireNonNull(newPerson);

        // Find all applications involving this person and update them
        this.getApplicationsByPerson(oldPerson).forEach(application -> {
            Application newApp = new Application(
                    newPerson.getPhone(), application.getJob().getJobTitle(), application.getJob().getJobCompany(),
                    application.getApplicationStatus(), application.getPersonList(), application.getJobList(),
                    application.getApplicationList());
            this.setApplication(application, newApp);
        });
    }

    /**
     * Updates all applications involving {@code job} after the job has been
     * modified.
     *
     * @param oldJob The job before modification
     * @param newJob The job after modification
     */
    public void updateJob(Job oldJob, Job newJob) {
        requireNonNull(oldJob);
        requireNonNull(newJob);

        // Find all applications involving this job and update them
        this.getApplicationsByJob(oldJob).forEach(application -> {
            // Check if application status is still valid with new job
            if (application.getApplicationStatus().applicationStatus() > newJob.getJobRounds().jobRounds) {
                throw new InvalidApplicationStatusException();
            }

            Application newApp = new Application(
                    application.getApplicant().getPhone(), newJob.getJobTitle(), newJob.getJobCompany(),
                    application.getApplicationStatus(), application.getPersonList(), application.getJobList(),
                    application.getApplicationList());
            this.setApplication(application, newApp);
        });
    }

    /**
     * Removes all applications associated with the given person.
     *
     * @param person The person whose applications should be removed
     */
    public void removePersonApplications(Person person) {
        requireNonNull(person);

        // Find and remove all applications for this person
        List<Application> toRemove = this.getApplicationsByPerson(person);
        toRemove.forEach(this::removeApplication);
    }

    /**
     * Removes all applications associated with the given job.
     *
     * @param job The job whose applications should be removed
     */
    public void removeJobApplications(Job job) {
        requireNonNull(job);

        // Find and remove all applications for this job
        List<Application> toRemove = this.getApplicationsByJob(job);
        toRemove.forEach(this::removeApplication);
    }

    /**
     * Gets all applications associated with a specific person.
     *
     * @param person The person whose applications to retrieve
     * @return A list of applications associated with the person
     */
    public List<Application> getApplicationsByPerson(Person person) {
        requireNonNull(person);

        return this.applicationList.asUnmodifiableObservableList().stream()
                .filter(application -> application.getApplicant().equals(person)).collect(Collectors.toList());
    }

    /**
     * Gets all applications associated with a specific job.
     *
     * @param job The job whose applications to retrieve
     * @return A list of applications associated with the job
     */
    public List<Application> getApplicationsByJob(Job job) {
        requireNonNull(job);

        return this.applicationList.asUnmodifiableObservableList().stream()
                .filter(application -> application.getJob().equals(job)).collect(Collectors.toList());
    }

    /**
     * Advances an application by the specified number of rounds.
     *
     * @param application The application to advance
     * @param rounds      The number of rounds to advance by
     * @return The updated application
     * @throws ApplicationNotFoundException      if the application does not exist
     * @throws InvalidApplicationStatusException if advancing would exceed job
     *                                           rounds
     */
    public Application advanceApplication(Application application, int rounds) {
        requireNonNull(application);
        if (!this.hasApplication(application)) {
            throw new ApplicationNotFoundException();
        }
        Application advancedApplication = application.advance(rounds);
        this.setApplication(application, advancedApplication);
        return advancedApplication;
    }

    //// util methods

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("applications", this.applicationList).toString();
    }

    public UniquePersonList getPersonList() {
        return this.personList;
    }

    public UniqueJobList getJobList() {
        return this.jobList;
    }

    @Override
    public UniqueApplicationList getUniqueApplicationList() {
        return this.applicationList;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof ApplicationsManager otherApplicationsManager)) {
            return false;
        }

        return this.applicationList.equals(otherApplicationsManager.applicationList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.applicationList);
    }
}
