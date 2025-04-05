package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.SOFTWARE_ENGINEER_GOOGLE;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.ReadOnlyApplicationsManager;
import seedu.address.model.ReadOnlyUserPrefs;
import seedu.address.model.application.Application;
import seedu.address.model.job.Job;
import seedu.address.model.person.Person;
import seedu.address.testutil.JobBuilder;

public class AddJobCommandTest {
    private Model model;
    @BeforeEach
    public void setUp() {
        model = new ModelStubAcceptingJobAdded();
    }

    @Test
    public void constructor_nullJob_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new AddJobCommand(null));
    }

    @Test
    public void execute_jobAcceptedByModel_addSuccessful() throws CommandException {
        ModelStubAcceptingJobAdded modelStub = new ModelStubAcceptingJobAdded();
        Job validJob = new JobBuilder().build();

        CommandResult commandResult = new AddJobCommand(validJob).execute(modelStub);

        assertEquals(String.format(AddJobCommand.MESSAGE_SUCCESS, Messages.format(validJob)),
                commandResult.getFeedbackToUser());
        assertEquals(Arrays.asList(validJob), modelStub.jobsAdded);
    }

    @Test
    public void execute_duplicateJob_throwsCommandException() {
        Job validJob = new JobBuilder().build();
        AddJobCommand addJobCommand = new AddJobCommand(validJob);
        model.addJob(validJob);

        assertThrows(CommandException.class, AddJobCommand.MESSAGE_DUPLICATE_JOB, () -> addJobCommand.execute(model));
    }

    @Test
    public void equals() {
        Job swe = new JobBuilder().withJobTitle("Software Engineering").build();
        Job dsa = new JobBuilder().withJobTitle("Data Scientist").build();
        AddJobCommand addSweCommand = new AddJobCommand(swe);
        AddJobCommand addDsaCommand = new AddJobCommand(dsa);

        // same object -> returns true
        assertTrue(addSweCommand.equals(addSweCommand));

        // same values -> returns true
        AddJobCommand addSweCommandCopy = new AddJobCommand(swe);
        assertTrue(addSweCommand.equals(addSweCommandCopy));

        // different types -> returns false
        assertFalse(addSweCommand.equals(1));

        // null -> returns false
        assertFalse(addSweCommand.equals(null));

        // different jobs -> return false
        assertFalse(addSweCommand.equals(addDsaCommand));
    }

    @Test
    public void toStringMethod() {
        AddJobCommand addJobCommand = new AddJobCommand(SOFTWARE_ENGINEER_GOOGLE);
        String expected = AddJobCommand.class.getCanonicalName() + "{job=" + SOFTWARE_ENGINEER_GOOGLE + "}";
        assertEquals(expected, addJobCommand.toString());
    }
    /**
     * A default model stub that have all of the methods failing.
     */
    private class ModelStub implements Model {
        @Override
        public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setViewState(Model.ViewState viewState) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public Model.ViewState getCurrentViewState() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public Model.ViewState getViewState() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void toggleJobView() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean isInJobView() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyUserPrefs getUserPrefs() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public GuiSettings getGuiSettings() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setGuiSettings(GuiSettings guiSettings) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public Path getAddressBookFilePath() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setAddressBookFilePath(Path addressBookFilePath) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void addCommand(String command) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public String getPrevCommand() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public String getNextCommand() {
            throw new AssertionError("This method should not be called.");
        }

        public ReadOnlyAddressBook getAddressBook() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setAddressBook(ReadOnlyAddressBook newData) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public Path getApplicationsManagerFilePath() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setApplicationsManagerFilePath(Path path) {
            throw new AssertionError("This method should not be called.");
        }

        // =========== Person Operations
        // =============================================================

        @Override
        public boolean hasPerson(Person person) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void deletePerson(Person target) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void addPerson(Person person) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setPerson(Person target, Person editedPerson) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ObservableList<Person> getFilteredPersonList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void updateFilteredPersonList(Predicate<Person> predicate) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void resetFilteredPersonList() {
            throw new AssertionError("This method should not be called.");
        }

        // =========== Job Operations
        // =============================================================

        @Override
        public boolean hasJob(Job job) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void deleteJob(Job job) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void addJob(Job job) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setJob(Job target, Job editedJob) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ObservableList<Job> getFilteredJobList() {
            return null;
        }

        @Override
        public ReadOnlyApplicationsManager getApplicationsManager() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ObservableList<Application> getFilteredApplicationList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void deleteApplication(Application application) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void addApplication(Application application) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public Application advanceApplication(Application application, int steps) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public List<Application> getApplicationsByJob(Job job) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public List<Application> getFilteredApplicationsByJob(Job job) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public List<Application> getApplicationsByPerson(Person person) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public List<Application> getFilteredApplicationsByPerson(Person person) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public Optional<Application> getApplicationByPersonAndJob(Person person, Job job) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void updateFilteredJobList(Predicate<Job> predicate) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void resetFilteredJobList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setApplicationsManager(ReadOnlyApplicationsManager applicationsManager) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean hasApplication(Application application) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setApplication(Application target, Application editedApplication) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void updateFilteredApplicationList(Predicate<Application> predicate) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void resetFilteredApplicationList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void reapplyJobFilters() {
            throw new AssertionError("This method should not be called.");
        }
    }

    private class ModelStubAcceptingJobAdded extends AddJobCommandTest.ModelStub {
        final ArrayList<Job> jobsAdded = new ArrayList<>();
        private Model.ViewState viewState = ViewState.JOB_VIEW;

        @Override
        public boolean hasJob(Job job) {
            requireNonNull(job);
            return jobsAdded.stream().anyMatch(job::isSameJob);
        }

        @Override
        public void addJob(Job job) {
            requireNonNull(job);
            jobsAdded.add(job);
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            return new AddressBook();
        }

        @Override
        public void setViewState(Model.ViewState viewState) {
            this.viewState = viewState;
        }

        @Override
        public Model.ViewState getCurrentViewState() {
            return viewState;
        }

        @Override
        public boolean isInJobView() {
            return viewState == Model.ViewState.JOB_VIEW
                    || viewState == Model.ViewState.JOB_DETAIL_VIEW
                    || viewState == Model.ViewState.PERSON_DETAIL_VIEW;
        }
    }
}
