package seedu.address.model;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.nio.file.Path;
import java.util.List;
import java.util.function.Predicate;
import java.util.logging.Logger;
import java.util.stream.Collectors;

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
    private final StackableFilteredList<Person> filteredPersons;
    private final StackableFilteredList<Job> filteredJobs;
    private final FilteredList<Application> filteredApplications;
    private ViewState currentViewState = ViewState.PERSON_VIEW; // Default to person view
    private String applicationStatusFilter; // Current status filter, null when no filter is active
    private final DoublyLinkedList commandHistory;

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
        this.filteredPersons = new StackableFilteredList<>(this.addressBook.getPersonList());
        this.filteredJobs = new StackableFilteredList<>(this.addressBook.getJobList());
        this.filteredApplications = new FilteredList<>(this.applicationsManager.getApplicationList());
        commandHistory = new DoublyLinkedList();
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

    // =========== Command History
    // ================================================================================

    @Override
    public void addCommand(String command) {
        commandHistory.add(command);
        commandHistory.reset();
    }

    @Override
    public String getPrevCommand() {
        commandHistory.moveBack();
        return commandHistory.getCommand();
    }

    @Override
    public String getNextCommand() {
        commandHistory.moveForward();
        return commandHistory.getCommand();
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
        resetFilteredPersonList();
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
        resetFilteredJobList();
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
        return filteredPersons.getFilteredList();
    }

    @Override
    public void updateFilteredPersonList(Predicate<Person> predicate) {
        requireNonNull(predicate);
        filteredPersons.addPredicate(predicate);
    }

    public void resetFilteredPersonList() {
        filteredPersons.clearFilters();
    }

    // =========== Filtered Job List Accessors
    // ================================================================

    /**
     * Returns an unmodifiable view of the list of {@code Job} backed by the
     * internal list of {@code versionedAddressBook}
     */
    @Override
    public ObservableList<Job> getFilteredJobList() {
        return filteredJobs.getFilteredList();
    }

    @Override
    public void updateFilteredJobList(Predicate<Job> predicate) {
        requireNonNull(predicate);
        filteredJobs.addPredicate(predicate);
    }

    public void resetFilteredJobList() {
        filteredJobs.clearFilters();
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
    public ObservableList<Application> getFilteredApplicationList() {
        return filteredApplications;
    }

    @Override
    public void updateFilteredApplicationList(Predicate<Application> predicate) {
        requireNonNull(predicate);
        filteredApplications.setPredicate(predicate);
    }

    @Override
    public boolean isInJobView() {
        return currentViewState == ViewState.JOB_VIEW
            || currentViewState == ViewState.JOB_DETAIL_VIEW
            || currentViewState == ViewState.PERSON_DETAIL_VIEW;
    }

    @Override
    public ViewState getCurrentViewState() {
        return currentViewState;
    }

    @Override
    public void setViewState(ViewState viewState) {
        this.currentViewState = viewState;
    }

    /**
     * Toggles between person view and job view.
     * If in any job view, switches to person view.
     * If in person view, switches to job view.
     */
    public void toggleJobView() {
        if (isInJobView()) {
            setViewState(ViewState.PERSON_VIEW);
        } else {
            setViewState(ViewState.JOB_VIEW);
        }
    }

    // =========== Status Filter Methods ==========================================================================

    @Override
    public void setApplicationStatusFilter(String status) {
        this.applicationStatusFilter = status;
        logger.info("Application status filter set to: " + status);
    }

    @Override
    public String getApplicationStatusFilter() {
        return applicationStatusFilter;
    }

    @Override
    public void applyStatusFilter() {
        if (applicationStatusFilter == null) {
            // If no status filter, just reset all lists
            resetFilteredPersonList();
            resetFilteredJobList();
            updateFilteredApplicationList(PREDICATE_SHOW_ALL_APPLICATIONS);
            return;
        }

        // Apply the status filter to the application list
        Predicate<Application> statusPredicate = app ->
            Integer.toString(app.getApplicationStatus().applicationStatus).equals(applicationStatusFilter);
        updateFilteredApplicationList(statusPredicate);

        // Get jobs that have applications with the matching status
        List<Job> jobsWithMatchingApps = getFilteredApplicationList().stream()
            .map(Application::getJob)
            .distinct()
            .collect(Collectors.toList());

        // Filter the job list to only show jobs with matching applications
        updateFilteredJobList(job -> {
            // Only include jobs that have matching applications after filtering
            if (!jobsWithMatchingApps.contains(job)) {
                return false; // Job has no applications matching the filter
            }

            // Include this job since it has at least one application matching the filter
            return true;
        });

        // Get persons that have applications with the matching status
        List<Person> personsWithMatchingApps = getFilteredApplicationList().stream()
            .map(Application::getApplicant)
            .distinct()
            .collect(Collectors.toList());

        // Filter the person list to only show persons with matching applications
        updateFilteredPersonList(person -> personsWithMatchingApps.contains(person));

        logger.info("Applied filter for status: " + applicationStatusFilter);
        logger.info("Filtered to " + getFilteredApplicationList().size() + " applications");
        logger.info("Filtered to " + getFilteredJobList().size() + " jobs");
        logger.info("Filtered to " + getFilteredPersonList().size() + " persons");
    }

    @Override
    public void clearStatusFilter() {
        applicationStatusFilter = null;
        resetFilteredPersonList();
        resetFilteredJobList();
        updateFilteredApplicationList(PREDICATE_SHOW_ALL_APPLICATIONS);
        logger.info("Status filter cleared, reset all filtered lists");
    }

    @Override
    public List<Application> getApplicationsByJob(Job job) {
        requireNonNull(job);
        // Get all applications for the job directly from the manager
        List<Application> allJobApplications = applicationsManager.getApplicationsByJob(job);

        // If no status filter is active, return all applications for the job
        if (applicationStatusFilter == null) {
            return allJobApplications;
        }

        // Filter applications by the current status filter
        return allJobApplications.stream()
                .filter(app -> Integer.toString(app.getApplicationStatus().applicationStatus)
                        .equals(applicationStatusFilter))
                .collect(Collectors.toList());
    }

    @Override
    public List<Application> getApplicationsByPerson(Person person) {
        requireNonNull(person);
        // Get all applications for the person directly from the manager
        List<Application> allPersonApplications = applicationsManager.getApplicationsByPerson(person);

        // If no status filter is active, return all applications for the person
        if (applicationStatusFilter == null) {
            return allPersonApplications;
        }

        // Filter applications by the current status filter
        return allPersonApplications.stream()
                .filter(app -> Integer.toString(app.getApplicationStatus().applicationStatus)
                        .equals(applicationStatusFilter))
                .collect(Collectors.toList());
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
