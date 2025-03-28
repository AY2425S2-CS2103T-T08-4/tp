package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class CommandResultTest {
    @Test
    public void equals() {
        CommandResult commandResult = CommandResult.withFeedback("feedback");

        // same values -> returns true
        assertTrue(commandResult.equals(CommandResult.withFeedback("feedback")));

        // same object -> returns true
        assertTrue(commandResult.equals(commandResult));

        // null -> returns false
        assertFalse(commandResult.equals(null));

        // different types -> returns false
        assertFalse(commandResult.equals(0.5f));

        // different feedbackToUser value -> returns false
        assertFalse(commandResult.equals(CommandResult.withFeedback("different")));

        // different showHelp value -> returns false
        assertFalse(commandResult.equals(CommandResult.withHelp("feedback")));

        // different exit value -> returns false
        assertFalse(commandResult.equals(CommandResult.withExit("feedback")));

        // different refreshApplications value -> returns false
        assertFalse(commandResult.equals(CommandResult.withRefreshApplications("feedback")));
    }

    @Test
    public void hashcode() {
        CommandResult commandResult = CommandResult.withFeedback("feedback");

        // same values -> returns same hashcode
        assertEquals(commandResult.hashCode(), CommandResult.withFeedback("feedback").hashCode());

        // different feedbackToUser value -> returns different hashcode
        assertNotEquals(commandResult.hashCode(), CommandResult.withFeedback("different").hashCode());

        // different showHelp value -> returns different hashcode
        assertNotEquals(commandResult.hashCode(), CommandResult.withHelp("feedback").hashCode());

        // different exit value -> returns different hashcode
        assertNotEquals(commandResult.hashCode(), CommandResult.withExit("feedback").hashCode());

        // different refreshApplications value -> returns different hashcode
        assertNotEquals(commandResult.hashCode(), CommandResult.withRefreshApplications("feedback").hashCode());
    }

    @Test
    public void toStringMethod() {
        CommandResult commandResult = CommandResult.withFeedback("feedback");
        String expected = new seedu.address.commons.util.ToStringBuilder(commandResult)
                .add("feedbackToUser", "feedback")
                .add("showHelp", false)
                .add("exit", false)
                .add("toggleView", false)
                .add("viewJob", false)
                .add("viewPerson", false)
                .add("clearView", false)
                .add("refreshJobView", false)
                .add("jobIndex", -1)
                .add("personIndex", -1)
                .add("refreshApplications", false)
                .toString();

        assertEquals(expected, commandResult.toString());
    }
}
