package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.ApplicationsManager;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.application.Application;
import seedu.address.model.application.ApplicationStatus;
import seedu.address.model.job.Job;
import seedu.address.model.person.Person;
import seedu.address.testutil.AddressBookBuilder;
import seedu.address.testutil.JobBuilder;
import seedu.address.testutil.PersonBuilder;
import seedu.address.testutil.TypicalPersons;

public class FindAppCommandTest {

    private Model model = new ModelManager(new AddressBookBuilder().withPerson(TypicalPersons.ALICE)
            .withPerson(TypicalPersons.BENSON).build(),
            new ApplicationsManager(),
            new UserPrefs());

    @Test
    public void constructor_withStatus_createsCommand() {
        FindAppCommand command = new FindAppCommand("1");
        assertTrue(command instanceof FindAppCommand);
    }

    @Test
    public void constructor_withJobIndexAndStatus_createsCommand() {
        FindAppCommand command = new FindAppCommand(Index.fromOneBased(1), "1");
        assertTrue(command instanceof FindAppCommand);
    }

    @Test
    public void execute_withStatusInPersonView_success() throws CommandException {
        // Setup model with some applications
        Job job = new JobBuilder().build();
        Person person = new PersonBuilder().build();
        Application application = new Application(person, job, new ApplicationStatus(1));
        model.addJob(job);
        model.addPerson(person);
        model.addApplication(application);

        // Execute command
        FindAppCommand command = new FindAppCommand("1");
        CommandResult result = command.execute(model);

        // Verify result
        assertEquals(String.format(FindAppCommand.MESSAGE_SUCCESS, "1"), result.getFeedbackToUser());
        assertFalse(result.isRefreshJobView());
        assertFalse(result.isClearView());
        assertEquals(Model.ViewState.PERSON_VIEW, model.getCurrentViewState());
    }

    @Test
    public void execute_inDetailView_clearsView() throws CommandException {
        // Setup model with some applications
        Job job = new JobBuilder().build();
        Person person = new PersonBuilder().build();
        Application application = new Application(person, job, new ApplicationStatus(1));
        model.addJob(job);
        model.addPerson(person);
        model.addApplication(application);
        model.setViewState(Model.ViewState.JOB_DETAIL_VIEW);

        // Execute command
        FindAppCommand command = new FindAppCommand("1");
        CommandResult result = command.execute(model);

        // Verify result
        assertEquals(String.format(FindAppCommand.MESSAGE_SUCCESS, "1"), result.getFeedbackToUser());
        assertFalse(result.isRefreshJobView());
        assertTrue(result.isClearView());
        assertEquals(Model.ViewState.JOB_VIEW, model.getCurrentViewState());
    }

    @Test
    public void execute_withNoMatchingApplications_clearsFilter() throws CommandException {
        // Setup model with some applications
        Job job = new JobBuilder().build();
        Person person = new PersonBuilder().build();
        Application application = new Application(person, job, new ApplicationStatus(1));
        model.addJob(job);
        model.addPerson(person);
        model.addApplication(application);
        model.setViewState(Model.ViewState.JOB_VIEW);

        // Execute command with non-existent status
        FindAppCommand command = new FindAppCommand("999");
        CommandResult result = command.execute(model);

        // Verify result
        assertEquals(String.format(FindAppCommand.MESSAGE_NO_MATCHES, "999"), result.getFeedbackToUser());
        assertTrue(result.isRefreshJobView());
        assertFalse(result.isClearView());
    }

    @Test
    public void execute_withStatusInJobView_success() throws CommandException {
        // Setup model with some applications
        Job job = new JobBuilder().build();
        Person person = new PersonBuilder().build();
        Application application = new Application(person, job, new ApplicationStatus(1));
        model.addJob(job);
        model.addPerson(person);
        model.addApplication(application);
        model.setViewState(Model.ViewState.JOB_VIEW);

        // Execute command
        FindAppCommand command = new FindAppCommand("1");
        CommandResult result = command.execute(model);

        // Verify result
        assertEquals(String.format(FindAppCommand.MESSAGE_SUCCESS, "1"), result.getFeedbackToUser());
        assertTrue(result.isRefreshJobView());
        assertFalse(result.isClearView());
    }

    @Test
    public void execute_withJobIndexAndStatus_success() throws CommandException {
        // Setup model with some applications
        Job job = new JobBuilder().build();
        Person person = new PersonBuilder().build();
        Application application = new Application(person, job, new ApplicationStatus(1));
        model.addJob(job);
        model.addPerson(person);
        model.addApplication(application);
        model.setViewState(Model.ViewState.JOB_VIEW);

        // Execute command with job index and status
        FindAppCommand command = new FindAppCommand(Index.fromOneBased(1), "1");
        CommandResult result = command.execute(model);

        // Verify result
        assertEquals(String.format(FindAppCommand.MESSAGE_SUCCESS, "1"), result.getFeedbackToUser());
        assertTrue(result.isRefreshJobView());
        assertFalse(result.isClearView());
    }

    @Test
    public void execute_withInvalidJobIndex_throwsCommandException() {
        // Setup model with some applications
        Job job = new JobBuilder().build();
        Person person = new PersonBuilder().build();
        Application application = new Application(person, job, new ApplicationStatus(1));
        model.addJob(job);
        model.addPerson(person);
        model.addApplication(application);
        model.setViewState(Model.ViewState.JOB_VIEW);

        // Execute command with invalid job index
        FindAppCommand command = new FindAppCommand(Index.fromOneBased(999), "1");
        assertThrows(CommandException.class, FindAppCommand.MESSAGE_JOB_NOT_FOUND, () -> command.execute(model));
    }

    @Test
    public void equals() {
        FindAppCommand findAppCommand1 = new FindAppCommand("1");
        FindAppCommand findAppCommand2 = new FindAppCommand("1");
        FindAppCommand findAppCommand3 = new FindAppCommand("2");
        FindAppCommand findAppCommand4 = new FindAppCommand(Index.fromOneBased(1), "1");

        // same object -> returns true
        assertTrue(findAppCommand1.equals(findAppCommand1));

        // same values -> returns true
        assertTrue(findAppCommand1.equals(findAppCommand2));

        // different values -> returns false
        assertFalse(findAppCommand1.equals(findAppCommand3));

        // different types -> returns false
        assertFalse(findAppCommand1.equals(findAppCommand4));
    }
}
