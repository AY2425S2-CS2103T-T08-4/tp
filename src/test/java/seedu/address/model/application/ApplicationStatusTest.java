package seedu.address.model.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class ApplicationStatusTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new ApplicationStatus((String) null));
    }

    @Test
    public void constructor_invalidApplicationStatus_throwsIllegalArgumentException() {
        // negative number
        assertThrows(IllegalArgumentException.class, () -> new ApplicationStatus(-1));

        // invalid string
        String invalidApplicationStatus = "abc";
        assertThrows(IllegalArgumentException.class, () -> new ApplicationStatus(invalidApplicationStatus));
    }

    @Test
    public void isValidApplicationStatus_invalidIntegerStatus_returnsFalse() {
        // negative status
        assertFalse(ApplicationStatus.isValidApplicationStatus(-1));
    }

    @Test
    public void isValidApplicationStatus_validIntegerStatus_returnsTrue() {
        // zero
        assertTrue(ApplicationStatus.isValidApplicationStatus(0));

        // positive numbers
        assertTrue(ApplicationStatus.isValidApplicationStatus(1));
        assertTrue(ApplicationStatus.isValidApplicationStatus(10));
        assertTrue(ApplicationStatus.isValidApplicationStatus(Integer.MAX_VALUE));
    }

    @Test
    public void isValidApplicationStatus_invalidStringStatus_returnsFalse() {
        // null
        assertThrows(NullPointerException.class, () -> ApplicationStatus.isValidApplicationStatus((String) null));

        // empty string
        assertFalse(ApplicationStatus.isValidApplicationStatus(""));

        // spaces only
        assertFalse(ApplicationStatus.isValidApplicationStatus(" "));

        // non-numeric
        assertFalse(ApplicationStatus.isValidApplicationStatus("abc"));

        // alphanumeric
        assertFalse(ApplicationStatus.isValidApplicationStatus("1a"));

        // negative number
        assertFalse(ApplicationStatus.isValidApplicationStatus("-1"));
    }

    @Test
    public void isValidApplicationStatus_validStringStatus_returnsTrue() {
        // zero
        assertTrue(ApplicationStatus.isValidApplicationStatus("0"));

        // positive numbers
        assertTrue(ApplicationStatus.isValidApplicationStatus("1"));
        assertTrue(ApplicationStatus.isValidApplicationStatus("10"));
        assertTrue(ApplicationStatus.isValidApplicationStatus(Integer.toString(Integer.MAX_VALUE)));
    }

    @Test
    public void compareTo_smallerStatus_returnsNegative() {
        ApplicationStatus status1 = new ApplicationStatus(1);
        ApplicationStatus status2 = new ApplicationStatus(2);
        assertTrue(status1.compareTo(status2) < 0);
    }

    @Test
    public void compareTo_equalStatus_returnsZero() {
        ApplicationStatus status1 = new ApplicationStatus(1);
        ApplicationStatus status2 = new ApplicationStatus(1);
        assertEquals(0, status1.compareTo(status2));
    }

    @Test
    public void compareTo_largerStatus_returnsPositive() {
        ApplicationStatus status1 = new ApplicationStatus(2);
        ApplicationStatus status2 = new ApplicationStatus(1);
        assertTrue(status1.compareTo(status2) > 0);
    }

    @Test
    public void equals() {
        ApplicationStatus status = new ApplicationStatus(1);

        // same object -> returns true
        assertTrue(status.equals(status));

        // same value -> returns true
        ApplicationStatus sameValueStatus = new ApplicationStatus(1);
        assertTrue(status.equals(sameValueStatus));

        // null -> returns false
        assertFalse(status.equals(null));

        // different value -> returns false
        ApplicationStatus differentStatus = new ApplicationStatus(2);
        assertFalse(status.equals(differentStatus));
    }

    @Test
    public void toString_validStatus_returnsStringRepresentation() {
        ApplicationStatus status = new ApplicationStatus(1);
        assertEquals("1", status.toString());
    }
}
