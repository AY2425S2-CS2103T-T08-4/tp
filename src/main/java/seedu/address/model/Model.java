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

    /**
     * Returns a list of applications associated with a specific person.
     * @return A list of applications associated with the person
     */
    List<Application> getApplicationsByPerson(Person person);

    /**
     * Returns a list of applications associated with a specific job.
     * @return A list of applications associated with the job
     */
    List<Application> getApplicationsByJob(Job job);

    /** Returns an unmodifiable view of the filtered application list */
    ObservableList<Application> getFilteredApplicationList();

    /**
     * Updates the filter of the filtered application list to filter by the given
     * {@code predicate}.
     *
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredApplicationList(Predicate<Application> predicate);

    /** Returns an unmodifiable list of all applications in the address book */
    List<Application> getApplicationList();
}
