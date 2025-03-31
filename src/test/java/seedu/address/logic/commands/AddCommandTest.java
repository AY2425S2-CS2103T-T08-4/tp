package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.ALICE;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
import seedu.address.testutil.PersonBuilder;

public class AddCommandTest {

    private Model model;
    private Model expectedModel;

    @BeforeEach
    public void setUp() {
        model = new ModelStubAcceptingPersonAdded();
        expectedModel = new ModelStubAcceptingPersonAdded();
        // Set the view state to PERSON_VIEW since AddCommand can only be executed in person view
        model.setViewState(Model.ViewState.PERSON_VIEW);
        expectedModel.setViewState(Model.ViewState.PERSON_VIEW);
    }

    @Test
    public void constructor_nullPerson_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new AddCommand(null));
    }

    @Test
    public void execute_personAcceptedByModel_addSuccessful() throws CommandException {
        ModelStubAcceptingPersonAdded modelStub = new ModelStubAcceptingPersonAdded();
        Person validPerson = new PersonBuilder().build();

        // Set view state to PERSON_VIEW
        modelStub.setViewState(Model.ViewState.PERSON_VIEW);
        
        // Execute the command
        CommandResult commandResult = new AddCommand(validPerson).execute(modelStub);

        // Verify the result directly
        assertEquals(String.format(AddCommand.MESSAGE_SUCCESS, Messages.format(validPerson)), commandResult.getFeedbackToUser());
        assertEquals(Arrays.asList(validPerson), modelStub.personsAdded);
    }

    @Test
    public void execute_duplicatePerson_throwsCommandException() {
        Person validPerson = new PersonBuilder().build();
        AddCommand addCommand = new AddCommand(validPerson);
        model.addPerson(validPerson);

        assertThrows(CommandException.class, AddCommand.MESSAGE_DUPLICATE_PERSON, () -> addCommand.execute(model));
    }

    @Test
    public void equals() {
        Person alice = new PersonBuilder().withName("Alice").build();
        Person bob = new PersonBuilder().withName("Bob").build();
        AddCommand addAliceCommand = new AddCommand(alice);
        AddCommand addBobCommand = new AddCommand(bob);

        // same object -> returns true
        assertTrue(addAliceCommand.equals(addAliceCommand));

        // same values -> returns true
        AddCommand addAliceCommandCopy = new AddCommand(alice);
        assertTrue(addAliceCommand.equals(addAliceCommandCopy));

        // different types -> returns false
        assertFalse(addAliceCommand.equals(1));

        // null -> returns false
        assertFalse(addAliceCommand.equals(null));

        // different person -> returns false
        assertFalse(addAliceCommand.equals(addBobCommand));
    }

    @Test
    public void toStringMethod() {
        AddCommand addCommand = new AddCommand(ALICE);
        String expected = AddCommand.class.getCanonicalName() + "{toAdd=" + ALICE + "}";
        assertEquals(expected, addCommand.toString());
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
    }


    /**
     * A Model stub that contains a single person.
     */
    private class ModelStubWithPerson extends ModelStub {
        private final Person person;
        private Model.ViewState viewState = Model.ViewState.PERSON_VIEW;

        ModelStubWithPerson(Person person) {
            requireNonNull(person);
            this.person = person;
        }

        @Override
        public boolean hasPerson(Person person) {
            requireNonNull(person);
            return this.person.isSamePerson(person);
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

    /**
     * A Model stub that always accept the person being added.
     */
    private class ModelStubAcceptingPersonAdded extends ModelStub {
        final ArrayList<Person> personsAdded = new ArrayList<>();
        private Model.ViewState viewState = Model.ViewState.PERSON_VIEW;

        @Override
        public boolean hasPerson(Person person) {
            requireNonNull(person);
            return personsAdded.stream().anyMatch(person::isSamePerson);
        }

        @Override
        public void addPerson(Person person) {
            requireNonNull(person);
            personsAdded.add(person);
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
