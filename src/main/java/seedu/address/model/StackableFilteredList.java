package seedu.address.model;

import java.util.Objects;
import java.util.function.Predicate;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;


/**
 * Wraps an ObservableList and filter its content using the provided {@code Predicate}.
 * {@code Predicate} are combined with past {@code Predicate} using logical AND.
 * Default {@code Predicate} at initialisation is true, and all elements will be matched.
 * All changes in the ObservableList are propagated immediately to the FilteredList.
 *
 * @param <E> Element of the ObservableList
 */
public class StackableFilteredList<E> {
    private final ObservableList<E> originalList;
    private FilteredList<E> filteredList;
    private Predicate<E> combinedPredicate;

    /**
     * Creates a StackableFilteredList
     * @param list List of elements to keep track of.
     */
    public StackableFilteredList(ObservableList<E> list) {
        this.originalList = list;
        this.filteredList = new FilteredList<>(list);
        this.combinedPredicate = unused -> true;
    }

    /**
     * Combines {@code Predicate} with past {@code Predicate} using logical AND.
     */
    public void addPredicate(Predicate<E> newPredicate) {
        combinedPredicate = combinedPredicate.and(newPredicate);
        filteredList.setPredicate(combinedPredicate);
    }

    /**
     * Resets filter to match all elements in original list.
     */
    public void clearFilters() {
        combinedPredicate = item -> true;
        filteredList.setPredicate(combinedPredicate);
    }

    public ObservableList<E> getOriginalList() {
        return originalList;
    }

    public FilteredList<E> getFilteredList() {
        return filteredList;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof StackableFilteredList<?>)) {
            return false;
        }

        StackableFilteredList<?> otherStackableFilteredList = (StackableFilteredList<?>) other;

        // Point to note: Equality between 2 StackableFilteredList only considers FilteredList elements.
        // Predicate tests equality using reference equality, so we cannot use Predicate as part of
        // equality checking. Easiest fix would be to create a wrapper class and implement equals()
        // and hashCode(), unless we can prove that for any 2 FilteredList, if their elements are identical
        // then their combinedPredicate are the same.
        return filteredList.equals(otherStackableFilteredList.filteredList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(filteredList, combinedPredicate);
    }
}
