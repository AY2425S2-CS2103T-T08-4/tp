package seedu.address.model.application;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import seedu.address.testutil.ApplicationBuilder;

public class ApplicationStatusPredicateTest {

    @Test
    public void equals() {
        int firstPredicateStatus = 1;
        int secondPredicateStatus = 2;

        ApplicationStatusPredicate firstPredicate = new ApplicationStatusPredicate(firstPredicateStatus);
        ApplicationStatusPredicate secondPredicate = new ApplicationStatusPredicate(secondPredicateStatus);

        // same object -> returns true
        assertTrue(firstPredicate.equals(firstPredicate));

        // same values -> returns true
        ApplicationStatusPredicate firstPredicateCopy = new ApplicationStatusPredicate(firstPredicateStatus);
        assertTrue(firstPredicate.equals(firstPredicateCopy));

        // different types -> returns false
        assertFalse(firstPredicate.equals(1));

        // null -> returns false
        assertFalse(firstPredicate.equals(null));

        // different status -> returns false
        assertFalse(firstPredicate.equals(secondPredicate));
    }

    @Test
    public void test_applicationStatusMatches_returnsTrue() {
        // Exact match
        ApplicationStatusPredicate predicate = new ApplicationStatusPredicate(1);
        assertTrue(predicate.test(new ApplicationBuilder().withApplicationStatus(1).build()));

        // Status 3 matches application with status 3
        predicate = new ApplicationStatusPredicate(3);
        assertTrue(predicate.test(new ApplicationBuilder().withApplicationStatus(3).build()));
    }

    @Test
    public void test_applicationStatusDoesNotMatch_returnsFalse() {
        // Status 1 doesn't match application with status 2
        ApplicationStatusPredicate predicate = new ApplicationStatusPredicate(1);
        assertFalse(predicate.test(new ApplicationBuilder().withApplicationStatus(2).build()));

        // Status 5 doesn't match application with status 3
        predicate = new ApplicationStatusPredicate(5);
        assertFalse(predicate.test(new ApplicationBuilder().withApplicationStatus(3).build()));
    }
} 