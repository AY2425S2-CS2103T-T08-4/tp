package seedu.address.model.job;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.testutil.JobBuilder;

public class JobContainsKeywordsPredicateTest {

    @Test
    public void equals() {
        List<String> firstPredicateKeywordList = Collections.singletonList("first");
        List<String> secondPredicateKeywordList = Arrays.asList("first", "second");

        JobContainsKeywordsPredicate firstPredicate = new JobContainsKeywordsPredicate(firstPredicateKeywordList);
        JobContainsKeywordsPredicate secondPredicate = new JobContainsKeywordsPredicate(secondPredicateKeywordList);

        // same object -> returns true
        assertTrue(firstPredicate.equals(firstPredicate));

        // same values -> returns true
        JobContainsKeywordsPredicate firstPredicateCopy = new JobContainsKeywordsPredicate(firstPredicateKeywordList);
        assertTrue(firstPredicate.equals(firstPredicateCopy));

        // different types -> returns false
        assertFalse(firstPredicate.equals(1));

        // null -> returns false
        assertFalse(firstPredicate.equals(null));

        // different job -> returns false
        assertFalse(firstPredicate.equals(secondPredicate));
    }

    @Test
    public void test_jobContainsKeywords_returnsTrue() {
        // one keyword
        JobContainsKeywordsPredicate predicate = new JobContainsKeywordsPredicate(Collections
                .singletonList("Software"));
        assertTrue(predicate.test(new JobBuilder().withJobTitle("Software Engineering").build()));

        // multiple keyword
        predicate = new JobContainsKeywordsPredicate(Arrays.asList("Software", "Engineering"));
        assertTrue(predicate.test(new JobBuilder().withJobTitle("Software Engineering").build()));

        // only one matching keyword
        predicate = new JobContainsKeywordsPredicate(Arrays.asList("Software", "AI"));
        assertTrue(predicate.test(new JobBuilder().withJobTitle("Software Engineering").build()));

        // mixed-case keywords
        predicate = new JobContainsKeywordsPredicate(Arrays.asList("SoftWAre", "EnginEERIng"));
        assertTrue(predicate.test(new JobBuilder().withJobTitle("Software Engineering").build()));
    }

    @Test
    public void test_jobDoesNotContainKeywords_returnsFalse() {
        // zero keywords
        JobContainsKeywordsPredicate predicate = new JobContainsKeywordsPredicate(Collections.emptyList());
        assertFalse(predicate.test(new JobBuilder().withJobTitle("Software Engineering").build()));

        // non-matching keyword
        predicate = new JobContainsKeywordsPredicate(Arrays.asList("Data"));
        assertFalse(predicate.test(new JobBuilder().withJobTitle("Software Engineering").build()));

        // keywords match job rounds, skills but does not match job title
        predicate = new JobContainsKeywordsPredicate(Arrays.asList("3", "pyThOn"));
        assertTrue(predicate.test(new JobBuilder().withJobTitle("Software Engineering").withJobRounds(3)
                .withSkills("Python").build()));
    }

    @Test
    public void toStringMethod() {
        List<String> keywords = List.of("keyword1", "keyword2");
        JobContainsKeywordsPredicate predicate = new JobContainsKeywordsPredicate(keywords);

        String expected = JobContainsKeywordsPredicate.class.getCanonicalName() + "{keywords=" + keywords + "}";
        assertEquals(expected, predicate.toString());
    }
}
