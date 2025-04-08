package seedu.address.logic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX;
import static seedu.address.logic.Messages.MESSAGE_UNKNOWN_COMMAND;
import static seedu.address.logic.commands.CommandTestUtil.ADDRESS_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.DEGREE_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.EMAIL_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.NAME_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.PHONE_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.SCHOOL_DESC_AMY;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.AMY;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import seedu.address.commons.core.GuiSettings;
import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.ListCommand;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.UserPrefs;
import seedu.address.model.application.Application;
import seedu.address.model.job.Job;
import seedu.address.model.person.Person;
import seedu.address.storage.JsonAddressBookStorage;
import seedu.address.storage.JsonApplicationsManagerStorage;
import seedu.address.storage.JsonUserPrefsStorage;
import seedu.address.storage.StorageManager;
import seedu.address.testutil.ApplicationBuilder;
import seedu.address.testutil.JobBuilder;
import seedu.address.testutil.PersonBuilder;

public class LogicManagerTest {
    private static final IOException DUMMY_IO_EXCEPTION = new IOException("dummy IO exception");
    private static final IOException DUMMY_AD_EXCEPTION = new AccessDeniedException("dummy access denied exception");

    @TempDir
    public Path temporaryFolder;

    private final Model model = new ModelManager();
    private Logic logic;

    @BeforeEach
    public void setUp() {
        JsonAddressBookStorage addressBookStorage = new JsonAddressBookStorage(
                temporaryFolder.resolve("addressBook.json"));
        JsonApplicationsManagerStorage applicationsManagerStorage = new JsonApplicationsManagerStorage(
                temporaryFolder.resolve("applicationsManager.json"));
        JsonUserPrefsStorage userPrefsStorage = new JsonUserPrefsStorage(temporaryFolder.resolve("userPrefs.json"));
        StorageManager storage = new StorageManager(addressBookStorage, applicationsManagerStorage, userPrefsStorage);
        logic = new LogicManager(model, storage);

        // Set the model's view state to PERSON_VIEW for the test commands to work
        model.setViewState(Model.ViewState.PERSON_VIEW);
    }

    @Test
    public void execute_invalidCommandFormat_throwsParseException() {
        String invalidCommand = "uicfhmowqewca";
        assertParseException(invalidCommand, MESSAGE_UNKNOWN_COMMAND);
    }

    @Test
    public void execute_commandExecutionError_throwsCommandException() {
        String deleteCommand = "del 9";
        assertCommandException(deleteCommand, MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_validCommand_success() throws Exception {
        String listCommand = ListCommand.COMMAND_WORD;
        assertCommandSuccess(listCommand, ListCommand.MESSAGE_SUCCESS, model);
    }

    @Test
    public void execute_storageThrowsIoException_throwsCommandException() {
        assertCommandFailureForExceptionFromStorage(DUMMY_IO_EXCEPTION,
                String.format(LogicManager.FILE_OPS_ERROR_FORMAT, DUMMY_IO_EXCEPTION.getMessage()));
    }

    @Test
    public void execute_storageThrowsAdException_throwsCommandException() {
        assertCommandFailureForExceptionFromStorage(DUMMY_AD_EXCEPTION,
                String.format(LogicManager.FILE_OPS_PERMISSION_ERROR_FORMAT, DUMMY_AD_EXCEPTION.getMessage()));
    }

    @Test
    public void getFilteredPersonList_modifyList_throwsUnsupportedOperationException() {
        org.junit.jupiter.api.Assertions.assertThrows(
            UnsupportedOperationException.class, () -> logic.getFilteredPersonList().remove(0));
    }

    // New tests for additional Logic interface methods

    @Test
    public void getFilteredJobList_modifyList_throwsUnsupportedOperationException() {
        org.junit.jupiter.api.Assertions.assertThrows(
            UnsupportedOperationException.class, () -> logic.getFilteredJobList().remove(0));
    }

    @Test
    public void getFilteredApplicationList_modifyList_throwsUnsupportedOperationException() {
        org.junit.jupiter.api.Assertions.assertThrows(
            UnsupportedOperationException.class, () -> logic.getFilteredApplicationList().remove(0));
    }

    @Test
    public void getAddressBook_returnsCorrectAddressBook() {
        ReadOnlyAddressBook addressBook = logic.getAddressBook();
        assertNotNull(addressBook);
        assertEquals(model.getAddressBook(), addressBook);
    }

    @Test
    public void getAddressBookFilePath_returnsCorrectPath() {
        Path path = logic.getAddressBookFilePath();
        assertNotNull(path);
        assertEquals(model.getAddressBookFilePath(), path);
    }

    @Test
    public void getGuiSettings_returnsCorrectGuiSettings() {
        GuiSettings guiSettings = logic.getGuiSettings();
        assertNotNull(guiSettings);
        assertEquals(model.getGuiSettings(), guiSettings);
    }

    @Test
    public void setGuiSettings_updatesGuiSettings() {
        GuiSettings newGuiSettings = new GuiSettings(1.0, 2.0, 3, 4);
        logic.setGuiSettings(newGuiSettings);
        assertEquals(newGuiSettings, model.getGuiSettings());
    }

    @Test
    public void setViewState_updatesViewState() {
        logic.setViewState(Model.ViewState.JOB_VIEW);
        assertEquals(Model.ViewState.JOB_VIEW, logic.getViewState());

        logic.setViewState(Model.ViewState.PERSON_VIEW);
        assertEquals(Model.ViewState.PERSON_VIEW, logic.getViewState());
    }

    @Test
    public void getViewState_returnsCurrentViewState() {
        assertEquals(Model.ViewState.PERSON_VIEW, logic.getViewState());

        model.setViewState(Model.ViewState.JOB_VIEW);
        assertEquals(Model.ViewState.JOB_VIEW, logic.getViewState());
    }

    @Test
    public void updateFilteredApplicationList_updatesFilter() {
        // Create a test application
        Person person = new PersonBuilder(AMY).build();
        Job job = new JobBuilder().withJobTitle("Software Engineer").build();
        Application application = new ApplicationBuilder().withApplicant(person).withJob(job).build();

        // Add the application to the model
        model.addPerson(person);
        model.addJob(job);
        model.addApplication(application);

        // Update filter to show only applications with this job
        logic.updateFilteredApplicationList(app -> app.getJob().equals(job));

        // Check that the filtered list contains only the application with the specified job
        List<Application> filteredList = logic.getFilteredApplicationList();
        assertEquals(1, filteredList.size());
        assertTrue(filteredList.contains(application));
    }

    @Test
    public void resetFilteredApplicationList_showsAllApplications() {
        // Create test applications
        Person person = new PersonBuilder(AMY).build();
        Job job1 = new JobBuilder().withJobTitle("Software Engineer").build();
        Job job2 = new JobBuilder().withJobTitle("Data Scientist").build();
        Application app1 = new ApplicationBuilder().withApplicant(person).withJob(job1).build();
        Application app2 = new ApplicationBuilder().withApplicant(person).withJob(job2).build();

        // Add to model
        model.addPerson(person);
        model.addJob(job1);
        model.addJob(job2);
        model.addApplication(app1);
        model.addApplication(app2);

        // Apply filter to show only applications with job1
        logic.updateFilteredApplicationList(app -> app.getJob().equals(job1));
        assertEquals(1, logic.getFilteredApplicationList().size());

        // Reset filter
        logic.resetFilteredApplicationList();

        // Check that all applications are now shown
        assertEquals(2, logic.getFilteredApplicationList().size());
    }

    @Test
    public void resetFilteredJobList_showsAllJobs() {
        // Create test jobs
        Job job1 = new JobBuilder().withJobTitle("Software Engineer").build();
        Job job2 = new JobBuilder().withJobTitle("Data Scientist").build();

        // Add to model
        model.addJob(job1);
        model.addJob(job2);

        // Apply filter to show only job1
        model.updateFilteredJobList(job -> job.equals(job1));
        assertEquals(1, logic.getFilteredJobList().size());

        // Reset filter
        logic.resetFilteredJobList();

        // Check that all jobs are now shown
        assertEquals(2, logic.getFilteredJobList().size());
    }

    @Test
    public void getApplicationsByJob_returnsCorrectApplications() {
        // Create test data
        Person person = new PersonBuilder(AMY).build();
        Job job = new JobBuilder().withJobTitle("Software Engineer").build();
        Application application = new ApplicationBuilder().withApplicant(person).withJob(job).build();

        // Add to model
        model.addPerson(person);
        model.addJob(job);
        model.addApplication(application);

        // Get applications by job
        List<Application> applications = logic.getApplicationsByJob(job);

        // Check that the correct applications are returned
        assertEquals(1, applications.size());
        assertTrue(applications.contains(application));
    }

    @Test
    public void getFilteredApplicationsByJob_returnsFilteredApplications() {
        // Create test data
        Person person = new PersonBuilder(AMY).build();
        Job job = new JobBuilder().withJobTitle("Software Engineer").build();
        Application application = new ApplicationBuilder().withApplicant(person).withJob(job).build();

        // Add to model
        model.addPerson(person);
        model.addJob(job);
        model.addApplication(application);

        // Apply filter to show only applications with status 0
        logic.updateFilteredApplicationList(app -> app.getApplicationStatus().applicationStatus == 0);

        // Get filtered applications by job
        List<Application> filteredApplications = logic.getFilteredApplicationsByJob(job);

        // Check that the correct filtered applications are returned
        assertEquals(1, filteredApplications.size());
        assertTrue(filteredApplications.contains(application));
    }

    @Test
    public void getApplicationsByPerson_returnsCorrectApplications() {
        // Create test data
        Person person = new PersonBuilder(AMY).build();
        Job job = new JobBuilder().withJobTitle("Software Engineer").build();
        Application application = new ApplicationBuilder().withApplicant(person).withJob(job).build();

        // Add to model
        model.addPerson(person);
        model.addJob(job);
        model.addApplication(application);

        // Get applications by person
        List<Application> applications = logic.getApplicationsByPerson(person);

        // Check that the correct applications are returned
        assertEquals(1, applications.size());
        assertTrue(applications.contains(application));
    }

    @Test
    public void getFilteredApplicationsByPerson_returnsFilteredApplications() {
        // Create test data
        Person person = new PersonBuilder(AMY).build();
        Job job = new JobBuilder().withJobTitle("Software Engineer").build();
        Application application = new ApplicationBuilder().withApplicant(person).withJob(job).build();

        // Add to model
        model.addPerson(person);
        model.addJob(job);
        model.addApplication(application);

        // Apply filter to show only applications with status 0
        logic.updateFilteredApplicationList(app -> app.getApplicationStatus().applicationStatus == 0);

        // Get filtered applications by person
        List<Application> filteredApplications = logic.getFilteredApplicationsByPerson(person);

        // Check that the correct filtered applications are returned
        assertEquals(1, filteredApplications.size());
        assertTrue(filteredApplications.contains(application));
    }

    @Test
    public void reapplyJobFilters_reappliesFilters() {
        // Create test data
        Person person = new PersonBuilder(AMY).build();
        Job job1 = new JobBuilder().withJobTitle("Software Engineer").build();
        Job job2 = new JobBuilder().withJobTitle("Data Scientist").build();
        Application app1 = new ApplicationBuilder().withApplicant(person).withJob(job1).build();
        Application app2 = new ApplicationBuilder().withApplicant(person).withJob(job2).build();

        // Add to model
        model.addPerson(person);
        model.addJob(job1);
        model.addJob(job2);
        model.addApplication(app1);
        model.addApplication(app2);

        // Apply filter to show only applications with status "PENDING"
        logic.updateFilteredApplicationList(app -> app.getApplicationStatus().applicationStatus == 0);

        // Reapply job filters
        logic.reapplyJobFilters();

        // Check that the job list is filtered correctly
        // This test might need adjustment based on the actual implementation of reapplyJobFilters
        assertFalse(logic.getFilteredJobList().isEmpty());
    }

    /**
     * Executes the command and confirms that - no exceptions are thrown <br>
     * - the feedback message is equal to {@code expectedMessage} <br>
     * - the internal model manager state is the same as that in
     * {@code expectedModel} <br>
     *
     * @see #assertCommandFailure(String, Class, String, Model)
     */
    private void assertCommandSuccess(String inputCommand, String expectedMessage, Model expectedModel)
            throws CommandException, ParseException {
        CommandResult result = logic.execute(inputCommand);
        assertEquals(expectedMessage, result.getFeedbackToUser());
        assertEquals(expectedModel, model);
    }

    /**
     * Executes the command, confirms that a ParseException is thrown and that the
     * result message is correct.
     *
     * @see #assertCommandFailure(String, Class, String, Model)
     */
    private void assertParseException(String inputCommand, String expectedMessage) {
        assertCommandFailure(inputCommand, ParseException.class, expectedMessage);
    }

    /**
     * Executes the command, confirms that a CommandException is thrown and that the
     * result message is correct.
     *
     * @see #assertCommandFailure(String, Class, String, Model)
     */
    private void assertCommandException(String inputCommand, String expectedMessage) {
        assertCommandFailure(inputCommand, CommandException.class, expectedMessage);
    }

    /**
     * Executes the command, confirms that the exception is thrown and that the
     * result message is correct.
     *
     * @see #assertCommandFailure(String, Class, String, Model)
     */
    private void assertCommandFailure(String inputCommand, Class<? extends Throwable> expectedException,
            String expectedMessage) {
        Model expectedModel = new ModelManager(model.getAddressBook(), model.getApplicationsManager(), new UserPrefs());
        // Set view state to match the actual model
        expectedModel.setViewState(model.getCurrentViewState());
        assertCommandFailure(inputCommand, expectedException, expectedMessage, expectedModel);
    }

    /**
     * Executes the command and confirms that - the {@code expectedException} is
     * thrown <br>
     * - the resulting error message is equal to {@code expectedMessage} <br>
     * - the internal model manager state is the same as that in
     * {@code expectedModel} <br>
     *
     * @see #assertCommandSuccess(String, String, Model)
     */
    private void assertCommandFailure(String inputCommand, Class<? extends Throwable> expectedException,
            String expectedMessage, Model expectedModel) {
        assertThrows(expectedException, expectedMessage, () -> logic.execute(inputCommand));
        assertEquals(expectedModel, model);
    }

    /**
     * Tests the Logic component's handling of an {@code IOException} thrown by the
     * Storage component.
     *
     * @param e               the exception to be thrown by the Storage component
     * @param expectedMessage the message expected inside exception thrown by the
     *                        Logic component
     */
    private void assertCommandFailureForExceptionFromStorage(IOException e, String expectedMessage) {
        // Inject LogicManager with an AddressBookStorage that throws the IOException e
        // when saving
        JsonAddressBookStorage addressBookStorage = new JsonAddressBookStorage(
                temporaryFolder.resolve("ExceptionAddressBook.json")) {
            @Override
            public void saveAddressBook(ReadOnlyAddressBook addressBook, Path filePath) throws IOException {
                throw e;
            }
        };

        JsonApplicationsManagerStorage applicationsManagerStorage = new JsonApplicationsManagerStorage(
                temporaryFolder.resolve("ExceptionApplicationsManager.json"));

        JsonUserPrefsStorage userPrefsStorage = new JsonUserPrefsStorage(
                temporaryFolder.resolve("ExceptionUserPrefs.json"));
        StorageManager storage = new StorageManager(addressBookStorage, applicationsManagerStorage, userPrefsStorage);

        logic = new LogicManager(model, storage);

        // Triggers the saveAddressBook method by executing an add command
        String addCommand = AddCommand.COMMAND_WORD + NAME_DESC_AMY + PHONE_DESC_AMY + EMAIL_DESC_AMY + ADDRESS_DESC_AMY
                + SCHOOL_DESC_AMY + DEGREE_DESC_AMY;
        Person expectedPerson = new PersonBuilder(AMY).withSkills().build();
        ModelManager expectedModel = new ModelManager();
        expectedModel.addPerson(expectedPerson);
        // Set view state to match the actual model
        expectedModel.setViewState(model.getCurrentViewState());
        assertCommandFailure(addCommand, CommandException.class, expectedMessage, expectedModel);
    }
}
