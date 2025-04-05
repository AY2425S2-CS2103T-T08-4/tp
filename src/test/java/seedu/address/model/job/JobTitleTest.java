package seedu.address.model.job;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class JobTitleTest {
    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new JobTitle(null));
    }

    @Test
    public void constructor_invalidJobTitle_throwsIllegalArgumentException() {
        String invalidJobTitle = "";
        assertThrows(IllegalArgumentException.class, () -> new JobTitle(invalidJobTitle));
    }

    @Test
    public void isValidJobTitle() {
        // null skill name
        assertThrows(NullPointerException.class, () -> JobTitle.isValidJobTitle(null));

        // invalid job titles
        assertThrows(IllegalArgumentException.class, () -> new JobTitle("")); // empty string
        assertThrows(IllegalArgumentException.class, () -> new JobTitle(" ")); // space only
        assertFalse(JobTitle.isValidJobTitle(" Test")); // start with space
        assertFalse(JobTitle.isValidJobTitle(" 96"));
        assertThrows(IllegalArgumentException.class, () -> new JobTitle("*$")); // special characters
        assertThrows(IllegalArgumentException.class, () ->
                new JobTitle("Soft-ware")); // special characters within alphabets
        assertThrows(IllegalArgumentException.class, () -> new JobTitle("78#8")); // special characters within numbers
        assertThrows(IllegalArgumentException.class, () ->
                new JobTitle("Soft*($96")); // special characters within numbers and alphabets

        // valid job titles
        assertTrue(JobTitle.isValidJobTitle("Test ")); // trailing white space
        assertTrue(JobTitle.isValidJobTitle("96 "));
        assertTrue(JobTitle.isValidJobTitle("Test 96")); // alphanumeric
    }
}
