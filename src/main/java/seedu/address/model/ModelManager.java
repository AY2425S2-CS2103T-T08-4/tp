package seedu.address.model;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.nio.file.Path;
import java.util.List;
import java.util.function.Predicate;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.application.Application;
import seedu.address.model.job.Job;
import seedu.address.model.person.Person;

/**
 * Represents the in-memory model of the address book data.
 */
public class ModelManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final AddressBook addressBook;
    private final ApplicationsManager applicationsManager;
    private final UserPrefs userPrefs;
    private final FilteredList<Person> filteredPersons;
    private final FilteredList<Job> filteredJobs;
    private final FilteredList<Application> filteredApplications;

    /**
     * Initializes a ModelManager with the given addressBook, applicationsManager,
     * and userPrefs.
     */
    public ModelManager(ReadOnlyAddressBook addressBook, ReadOnlyApplicationsManager applicationsManager,
            ReadOnlyUserPrefs userPrefs) {
        requireAllNonNull(addressBook, applicationsManager, userPrefs);

        logger.fine("Initializing with address book: " + addressBook + ", applications manager: " + applicationsManager
                + " and user prefs " + userPrefs);

        this.addressBook = new AddressBook(addressBook);
        this.applicationsManager = new ApplicationsManager(applicationsManager);
        this.userPrefs = new UserPrefs(userPrefs);
        filteredPersons = new FilteredList<>(this.addressBook.getPersonList());
        filteredJobs = new FilteredList<>(this.addressBook.getJobList());
        filteredApplications = new FilteredList<>(this.applicationsManager.getApplicationList());
    }

    public ModelManager() {
        this(new AddressBook(), new ApplicationsManager(), new UserPrefs());
    }

    // =========== UserPrefs
    // ==================================================================================

    @Override
    public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
        requireNonNull(userPrefs);
        this.userPrefs.resetData(userPrefs);
    }

    @Override
    public ReadOnlyUserPrefs getUserPrefs() {
        return userPrefs;
    }

    @Override
    public GuiSettings getGuiSettings() {
        return userPrefs.getGuiSettings();
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        requireNonNull(guiSettings);
        userPrefs.setGuiSettings(guiSettings);
    }

    @Override
    public Path getAddressBookFilePath() {
        return userPrefs.getAddressBookFilePath();
    }

    @Override
    public void setAddressBookFilePath(Path addressBookFilePath) {
        requireNonNull(addressBookFilePath);
        userPrefs.setAddressBookFilePath(addressBookFilePath);
    }

    @Override
    public Path getApplicationsManagerFilePath() {
        return userPrefs.getApplicationsManagerFilePath();
    }

    @Override
    public void setApplicationsManagerFilePath(Path applicationsManagerFilePath) {
        requireNonNull(applicationsManagerFilePath);
        userPrefs.setApplicationsManagerFilePath(applicationsManagerFilePath);
    }

    // =========== AddressBook
    // ================================================================================

    @Override
    public void setAddressBook(ReadOnlyAddressBook addressBook) {
        this.addressBook.resetData(addressBook);
    }

    @Override
    public ReadOnlyAddressBook getAddressBook() {
        return addressBook;
    }

    // =========== Person Operations
    // ==========================================================================

    @Override
    public boolean hasPerson(Person person) {
        requireNonNull(person);
        return addressBook.hasPerson(person);
    }

    @Override
    public void deletePerson(Person target) {
        // First remove any applications associated with this person
        applicationsManager.removePersonApplications(target);
        // Then remove the person
        addressBook.removePerson(target);
    }

    @Override
    public void addPerson(Person person) {
        addressBook.addPerson(person);
        updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
    }

    @Override
    public void setPerson(Person target, Person editedPerson) {
        requireAllNonNull(target, editedPerson);

        // Update the person in address book
        addressBook.setPerson(target, editedPerson);

        // Update any applications that contain this person
        applicationsManager.updatePerson(target, editedPerson);
    }

    // =========== Job Operations
    // =============================================================================

    @Override
    public boolean hasJob(Job job) {
        requireNonNull(job);
        return addressBook.hasJob(job);
    }

    @Override
    public void deleteJob(Job target) {
        // First remove any applications associated with this job
        applicationsManager.removeJobApplications(target);
        // Then remove the job
        addressBook.removeJob(target);
    }

    @Override
    public void addJob(Job job) {
        addressBook.addJob(job);
        updateFilteredJobList(PREDICATE_SHOW_ALL_JOBS);
    }

    @Override
    public void setJob(Job target, Job editedJob) {
        requireAllNonNull(target, editedJob);

        // Update the job in address book
        addressBook.setJob(target, editedJob);

        // Update any applications that contain this job
        applicationsManager.updateJob(target, editedJob);
    }

    // =========== Filtered Person List Accessors
    // =============================================================

    /**
     * Returns an unmodifiable view of the list of {@code Person} backed by the
     * internal list of {@code versionedAddressBook}
     */
    @Override
    public ObservableList<Person> getFilteredPersonList() {
        return filteredPersons;
    }

    @Override
    public void updateFilteredPersonList(Predicate<Person> predicate) {
        requireNonNull(predicate);
        filteredPersons.setPredicate(predicate);
    }

    // =========== Filtered Job List Accessors
    // ================================================================

    /**
     * Returns an unmodifiable view of the list of {@code Job} backed by the
     * internal list of {@code versionedAddressBook}
     */
    @Override
    public ObservableList<Job> getFilteredJobList() {
        return filteredJobs;
    }

    @Override
    public void updateFilteredJobList(Predicate<Job> predicate) {
        requireNonNull(predicate);
        filteredJobs.setPredicate(predicate);
    }

    // =========== ApplicationsManager Methods
    // =================================================================

    @Override
    public ReadOnlyApplicationsManager getApplicationsManager() {
        return applicationsManager;
    }

    @Override
    public void setApplicationsManager(ReadOnlyApplicationsManager applicationsManager) {
        this.applicationsManager.resetData(applicationsManager);
    }

    @Override
    public boolean hasApplication(Application application) {
        requireNonNull(application);
        return applicationsManager.hasApplication(application);
    }

    @Override
    public void deleteApplication(Application target) {
        applicationsManager.removeApplication(target);
    }

    @Override
    public void addApplication(Application application) {
        applicationsManager.addApplication(application);
        updateFilteredApplicationList(PREDICATE_SHOW_ALL_APPLICATIONS);
    }

    @Override
    public void setApplication(Application target, Application editedApplication) {
        requireAllNonNull(target, editedApplication);
        applicationsManager.setApplication(target, editedApplication);
    }

    @Override
    public Application advanceApplication(Application application, int rounds) {
        requireNonNull(application);
        return applicationsManager.advanceApplication(application, rounds);
    }

    @Override
    public List<Application> getApplicationsByPerson(Person person) {
        requireNonNull(person);
        return applicationsManager.getApplicationsByPerson(person);
    }

    @Override
    public List<Application> getApplicationsByJob(Job job) {
        requireNonNull(job);
        return applicationsManager.getApplicationsByJob(job);
    }

    @Override
    public ObservableList<Application> getFilteredApplicationList() {
        return filteredApplications;
    }

    @Override
    public void updateFilteredApplicationList(Predicate<Application> predicate) {
        requireNonNull(predicate);
        filteredApplications.setPredicate(predicate);
    }

    @Override
    public List<Application> getApplicationList() {
        return applicationsManager.getApplicationList();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof ModelManager)) {
            return false;
        }

        ModelManager otherModelManager = (ModelManager) other;
        return addressBook.equals(otherModelManager.addressBook)
                && applicationsManager.equals(otherModelManager.applicationsManager)
                && userPrefs.equals(otherModelManager.userPrefs)
                && filteredPersons.equals(otherModelManager.filteredPersons)
                && filteredJobs.equals(otherModelManager.filteredJobs)
                && filteredApplications.equals(otherModelManager.filteredApplications);
    }

}
