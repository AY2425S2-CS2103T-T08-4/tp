package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DEGREE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SCHOOL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.testutil.Assert.assertThrows;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.person.NameContainsKeywordsPredicate;
import seedu.address.model.person.Person;
import seedu.address.testutil.EditPersonDescriptorBuilder;

/**
 * Contains helper methods for testing commands.
 */
public class CommandTestUtil {

    public static final String VALID_NAME_AMY = "Amy Bee";
    public static final String VALID_NAME_BOB = "Bob Choo";
    public static final String VALID_PHONE_AMY = "11111111";
    public static final String VALID_PHONE_BOB = "22222222";
    public static final String VALID_EMAIL_AMY = "amy@example.com";
    public static final String VALID_EMAIL_BOB = "bob@example.com";
    public static final String VALID_ADDRESS_AMY = "Block 312, Amy Street 1";
    public static final String VALID_ADDRESS_BOB = "Block 123, Bobby Street 3";
    public static final String VALID_TAG_HUSBAND = "husband";
    public static final String VALID_TAG_FRIEND = "friend";
    public static final String VALID_SCHOOL_AMY = "NUS";
    public static final String VALID_SCHOOL_BOB = "NTU";
    public static final String VALID_DEGREE_AMY = "Computer Science";
    public static final String VALID_DEGREE_BOB = "Civil Engineering";

    public static final String NAME_DESC_AMY = " " + PREFIX_NAME + VALID_NAME_AMY;
    public static final String NAME_DESC_BOB = " " + PREFIX_NAME + VALID_NAME_BOB;
    public static final String PHONE_DESC_AMY = " " + PREFIX_PHONE + VALID_PHONE_AMY;
    public static final String PHONE_DESC_BOB = " " + PREFIX_PHONE + VALID_PHONE_BOB;
    public static final String EMAIL_DESC_AMY = " " + PREFIX_EMAIL + VALID_EMAIL_AMY;
    public static final String EMAIL_DESC_BOB = " " + PREFIX_EMAIL + VALID_EMAIL_BOB;
    public static final String ADDRESS_DESC_AMY = " " + PREFIX_ADDRESS + VALID_ADDRESS_AMY;
    public static final String ADDRESS_DESC_BOB = " " + PREFIX_ADDRESS + VALID_ADDRESS_BOB;
    public static final String SCHOOL_DESC_AMY = " " + PREFIX_SCHOOL + VALID_SCHOOL_AMY;
    public static final String SCHOOL_DESC_BOB = " " + PREFIX_SCHOOL + VALID_SCHOOL_BOB;
    public static final String TAG_DESC_FRIEND = " " + PREFIX_TAG + VALID_TAG_FRIEND;
    public static final String TAG_DESC_HUSBAND = " " + PREFIX_TAG + VALID_TAG_HUSBAND;
    public static final String DEGREE_DESC_AMY = " " + PREFIX_DEGREE + VALID_DEGREE_AMY;
    public static final String DEGREE_DESC_BOB = " " + PREFIX_DEGREE + VALID_DEGREE_BOB;

    public static final String INVALID_NAME_DESC = " " + PREFIX_NAME + "James&"; // '&' not allowed in names
    public static final String INVALID_PHONE_DESC = " " + PREFIX_PHONE + "911a"; // 'a' not allowed in phones
    public static final String INVALID_EMAIL_DESC = " " + PREFIX_EMAIL + "bob!yahoo"; // missing '@' symbol
    public static final String INVALID_ADDRESS_DESC = " " + PREFIX_ADDRESS; // empty string not allowed for addresses
    public static final String INVALID_TAG_DESC = " " + PREFIX_TAG + "hubby*"; // '*' not allowed in tags

    public static final String PREAMBLE_WHITESPACE = "\t  \r  \n";
    public static final String PREAMBLE_NON_EMPTY = "NonEmptyPreamble";

    public static final EditCommand.EditPersonDescriptor DESC_AMY;
    public static final EditCommand.EditPersonDescriptor DESC_BOB;

    static {
        DESC_AMY = new EditPersonDescriptorBuilder().withName(VALID_NAME_AMY).withSchool(VALID_SCHOOL_AMY)
                .withDegree(VALID_DEGREE_AMY).withPhone(VALID_PHONE_AMY).withEmail(VALID_EMAIL_AMY)
                .withAddress(VALID_ADDRESS_AMY).withTags(VALID_TAG_FRIEND).build();
        DESC_BOB = new EditPersonDescriptorBuilder().withName(VALID_NAME_BOB).withSchool(VALID_SCHOOL_BOB)
                .withDegree(VALID_DEGREE_BOB).withTags(VALID_TAG_FRIEND).build();
    }

    /**
     * Executes the given {@code command}, confirms that <br>
     * - the returned {@link CommandResult} matches {@code expectedCommandResult}
     * <br>
     * - the {@code actualModel} matches {@code expectedModel}
     */
    public static void assertCommandSuccess(Command command, Model actualModel, CommandResult expectedCommandResult,
            Model expectedModel) {
        try {
            CommandResult result = command.execute(actualModel);
            assertEquals(expectedCommandResult, result);
            assertEquals(expectedModel, actualModel);
        } catch (CommandException ce) {
            throw new AssertionError("Execution of command should not fail.", ce);
        }
    }

    /**
     * Convenience wrapper to
     * {@link #assertCommandSuccess(Command, Model, CommandResult, Model)} that
     * takes a string {@code expectedMessage}.
     */
    public static void assertCommandSuccess(Command command, Model actualModel, String expectedMessage,
            Model expectedModel) {
        // For ListCommand, we can directly use withFeedback since that's what the command returns
        if (command instanceof ListCommand) {
            CommandResult expectedCommandResult = CommandResult.withFeedback(expectedMessage);
            assertCommandSuccess(command, actualModel, expectedCommandResult, expectedModel);
            return;
        }

        // For other commands, execute and check
        CommandResult actualResult;
        try {
            actualResult = command.execute(actualModel);
        } catch (CommandException ce) {
            throw new AssertionError("Execution of command should not fail.", ce);
        }

        // Create the expected CommandResult based on the command type
        CommandResult expectedCommandResult;

        if (command instanceof FindAppCommand) {
            // For FindAppCommand, create the expected result based on the command's behavior
            if (actualResult.isClearView()) {
                expectedCommandResult = CommandResult.withClearView(expectedMessage);
            } else if (actualResult.isRefreshJobView()) {
                expectedCommandResult = CommandResult.withRefreshJobView(expectedMessage);
            } else {
                expectedCommandResult = CommandResult.withFeedback(expectedMessage);
            }
        } else if (command instanceof ClearCommand) {
            expectedCommandResult = CommandResult.withClearView(expectedMessage);
        } else if (command instanceof DeleteCommand || command instanceof AddCommand
                || command instanceof DeleteJobCommand) {
            expectedCommandResult = CommandResult.withFeedback(expectedMessage);
        } else {
            // Default case: use the actual result's flags to create the expected result
            if (actualResult.isShowHelp()) {
                expectedCommandResult = CommandResult.withHelp(expectedMessage);
            } else if (actualResult.isExit()) {
                expectedCommandResult = CommandResult.withExit(expectedMessage);
            } else if (actualResult.setToggleView()) {
                expectedCommandResult = CommandResult.withToggleView(expectedMessage);
            } else if (actualResult.isViewJob() && actualResult.isViewPerson()) {
                expectedCommandResult = CommandResult.withPersonView(expectedMessage, actualResult.getJobIndex(),
                        actualResult.getPersonIndex());
            } else if (actualResult.isViewJob()) {
                expectedCommandResult = CommandResult.withJobView(expectedMessage, actualResult.getJobIndex());
            } else if (actualResult.isClearView() && actualResult.isRefreshJobView()) {
                expectedCommandResult = CommandResult.withRefreshJobView(expectedMessage);
            } else if (actualResult.isClearView()) {
                expectedCommandResult = CommandResult.withClearView(expectedMessage);
            } else if (actualResult.isRefreshJobView()) {
                expectedCommandResult = CommandResult.withRefreshJobViewOnly(expectedMessage);
            } else if (actualResult.isRefreshApplications()) {
                expectedCommandResult = CommandResult.withRefreshApplications(expectedMessage);
            } else {
                expectedCommandResult = CommandResult.withFeedback(expectedMessage);
            }
        }

        assertEquals(expectedCommandResult, actualResult);
        assertEquals(expectedModel, actualModel);
    }

    /**
     * Executes the given {@code command}, confirms that <br>
     * - a {@code CommandException} is thrown <br>
     * - the CommandException message matches {@code expectedMessage} <br>
     * - the address book, filtered person list and selected person in
     * {@code actualModel} remain unchanged
     */
    public static void assertCommandFailure(Command command, Model actualModel, String expectedMessage) {
        // we are unable to defensively copy the model for comparison later, so we can
        // only do so by copying its components.
        AddressBook expectedAddressBook = new AddressBook(actualModel.getAddressBook());
        List<Person> expectedFilteredList = new ArrayList<>(actualModel.getFilteredPersonList());

        assertThrows(CommandException.class, expectedMessage, () -> command.execute(actualModel));
        assertEquals(expectedAddressBook, actualModel.getAddressBook());
        assertEquals(expectedFilteredList, actualModel.getFilteredPersonList());
    }

    /**
     * Updates {@code model}'s filtered list to show only the person at the given
     * {@code targetIndex} in the {@code model}'s address book.
     */
    public static void showPersonAtIndex(Model model, Index targetIndex) {
        assertTrue(targetIndex.getZeroBased() < model.getFilteredPersonList().size());

        Person person = model.getFilteredPersonList().get(targetIndex.getZeroBased());
        final String[] splitName = person.getName().fullName.split("\\s+");
        model.updateFilteredPersonList(new NameContainsKeywordsPredicate(Arrays.asList(splitName[0])));

        assertEquals(1, model.getFilteredPersonList().size());
    }
}
