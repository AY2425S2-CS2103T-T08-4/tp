package seedu.address.model;

import java.nio.file.Path;
import java.util.List;
import java.util.function.Predicate;

import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.model.application.Application;
import seedu.address.model.job.Job;
import seedu.address.model.person.Person;

/**
 * The API of the Model component.
 */
public interface Model {
    /** {@code Predicate} that always evaluate to true */
    Predicate<Person> PREDICATE_SHOW_ALL_PERSONS = unused -> true;

    /** {@code Predicate} that always evaluate to true */
    Predicate<Job> PREDICATE_SHOW_ALL_JOBS = unused -> true;

    /** {@code Predicate} that always evaluate to true */
    Predicate<Application> PREDICATE_SHOW_ALL_APPLICATIONS = unused -> true;

    /**
     * Replaces user prefs data with the data in {@code userPrefs}.
     */
    void setUserPrefs(ReadOnlyUserPrefs userPrefs);

    /**
     * Returns the user prefs.
     */
    ReadOnlyUserPrefs getUserPrefs();

    /**
     * Returns the user prefs' GUI settings.
     */
    GuiSettings getGuiSettings();

    /**
     * Sets the user prefs' GUI settings.
     */
    void setGuiSettings(GuiSettings guiSettings);

    /**
     * Returns the user prefs' address book file path.
     */
    Path getAddressBookFilePath();

    /**
     * Sets the user prefs' address book file path.
     */
    void setAddressBookFilePath(Path addressBookFilePath);

    /**
     * Returns the user prefs' applications manager file path.
     */
    Path getApplicationsManagerFilePath();

    /**
     * Sets the user prefs' applications manager file path.
     */
    void setApplicationsManagerFilePath(Path applicationsManagerFilePath);

    /**
     * Adds the previous input of the user to the command history.
     *
     * @param command last input command
     */
    void addCommand(String command);

    /**
     * Gets the previous command in the command history
     */
    String getPrevCommand();

    /**
     * Gets the next command in the command history
     */
    String getNextCommand();

    /**
     * Replaces address book data with the data in {@code addressBook}.
     */
    void setAddressBook(ReadOnlyAddressBook addressBook);

    /** Returns the AddressBook */
    ReadOnlyAddressBook getAddressBook();

    /**
     * Returns true if a person with the same identity as {@code person} exists in
     * the address book.
     */
    boolean hasPerson(Person person);

    /**
     * Deletes the given person. The person must exist in the address book.
     */
    void deletePerson(Person target);

    /**
     * Adds the given person. {@code person} must not already exist in the address
     * book.
     */
    void addPerson(Person person);

    /**
     * Replaces the given person {@code target} with {@code editedPerson}.
     * {@code target} must exist in the address book. The person identity of
     * {@code editedPerson} must not be the same as another existing person in the
     * address book.
     */
    void setPerson(Person target, Person editedPerson);

    /** Returns an unmodifiable view of the filtered person list */
    ObservableList<Person> getFilteredPersonList();

    /**
     * Updates the filter of the filtered person list to filter by the given
     * {@code predicate}.
     *
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredPersonList(Predicate<Person> predicate);

    /**
     * Resets the filter of the filtered person list to show all people
     */
    void resetFilteredPersonList();

    /**
     * Returns true if a job with the same identity as {@code job} exists in the
     * address book.
     */
    boolean hasJob(Job job);

    /**
     * Deletes the given job. The job must exist in the address book.
     */
    void deleteJob(Job target);

    /**
     * Adds the given job. {@code job} must not already exist in the address book.
     */
    void addJob(Job job);

    /**
     * Replaces the given job {@code target} with {@code editedJob}. {@code target}
     * must exist in the address book. The job identity of {@code editedJob} must
     * not be the same as another existing job in the address book.
     */
    void setJob(Job target, Job editedJob);

    /** Returns an unmodifiable view of the filtered job list */
    ObservableList<Job> getFilteredJobList();

    /**
     * Updates the filter of the filtered job list to filter by the given
     * {@code predicate}.
     *
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredJobList(Predicate<Job> predicate);

    /**
     * Resets the filter of the filtered job list to show all jobs
     */
    void resetFilteredJobList();

    // =========== ApplicationsManager Methods
    // =============================================================

    /**
     * Returns the ApplicationsManager.
     */
    ReadOnlyApplicationsManager getApplicationsManager();

    /**
     * Replaces applications manager data with the data in
     * {@code applicationsManager}.
     */
    void setApplicationsManager(ReadOnlyApplicationsManager applicationsManager);

    /**
     * Returns true if an application with the same identity as {@code application}
     * exists in the applications manager.
     */
    boolean hasApplication(Application application);

    /**
     * Deletes the given application. The application must exist in the applications
     * manager.
     */
    void deleteApplication(Application target);

    /**
     * Adds the given application. {@code application} must not already exist in the
     * applications manager.
     */
    void addApplication(Application application);

    /**
     * Replaces the given application {@code target} with {@code editedApplication}.
     * {@code target} must exist in the applications manager. The application
     * identity of {@code editedApplication} must not be the same as another
     * existing application in the applications manager.
     */
    void setApplication(Application target, Application editedApplication);

    /**
     * Advances the given application by the specified number of rounds.
     *
     * @return The updated application
     */
    Application advanceApplication(Application application, int rounds);

    /** Returns an unmodifiable view of the filtered application list */
    ObservableList<Application> getFilteredApplicationList();

    /**
     * Updates the filter of the filtered application list to filter by the given
     * {@code predicate}.
     *
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredApplicationList(Predicate<Application> predicate);

    /**
     * View states for the application.
     */
    enum ViewState {
        PERSON_VIEW, // Default view showing persons
        JOB_VIEW, // View showing jobs
        JOB_DETAIL_VIEW, // View showing details of a specific job
        PERSON_DETAIL_VIEW // View showing details of a specific person
        // Add more views as needed
    }

    /**
     * Returns the current view state of the application.
     */
    ViewState getCurrentViewState();

    /**
     * Sets the current view state of the application.
     */
    void setViewState(ViewState viewState);

    /**
     * Toggles between person view and job view.
     * If in any job view, switches to person view.
     * If in person view, switches to job view.
     */
    void toggleJobView();

    /**
     * Returns true if the current view is job-related and can access job listings and operations.
     * This includes JOB_VIEW, JOB_DETAIL_VIEW, and PERSON_DETAIL_VIEW
     *  (since it shows person details within a job context).
     */
    default boolean isInJobView() {
        return getCurrentViewState() == ViewState.JOB_VIEW
            || getCurrentViewState() == ViewState.JOB_DETAIL_VIEW
            || getCurrentViewState() == ViewState.PERSON_DETAIL_VIEW;
    }

    /**
     * Sets a global application status filter.
     * @param status The application status to filter by, or null to clear the filter
     */
    void setApplicationStatusFilter(String status);

    /**
     * Gets the current application status filter.
     * @return The current status filter, or null if no filter is set
     */
    String getApplicationStatusFilter();

    /**
     * Applies the current status filter to both job and person views.
     * This updates the filtered job, person, and application lists.
     */
    void applyStatusFilter();

    /**
     * Clears the application status filter and resets all filtered lists.
     */
    void clearStatusFilter();

    /**
     * Gets a list of applications for a specific job, respecting any active status filters.
     * If no status filter is active, returns all applications for the job.
     *
     * @param job The job to get applications for
     * @return List of applications for the job, filtered by status if applicable
     */
    List<Application> getApplicationsByJob(Job job);

    /**
     * Gets a list of applications for a specific person, respecting any active status filters.
     * If no status filter is active, returns all applications for the person.
     *
     * @param person The person to get applications for
     * @return List of applications for the person, filtered by status if applicable
     */
    List<Application> getApplicationsByPerson(Person person);
}

