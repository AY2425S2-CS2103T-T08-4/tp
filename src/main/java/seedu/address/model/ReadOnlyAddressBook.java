package seedu.address.model;

import javafx.collections.ObservableList;
import seedu.address.model.application.Application;
import seedu.address.model.application.UniqueApplicationList;
import seedu.address.model.job.Job;
import seedu.address.model.job.UniqueJobList;
import seedu.address.model.person.Person;
import seedu.address.model.person.UniquePersonList;

import java.util.List;

/**
 * Unmodifiable view of an address book
 */
public interface ReadOnlyAddressBook {
    /**
     * Returns a modifiable view of the persons list with greater exposure warranted by its use in
     * Persons-related commands. USE WITH CAUTION.
     */
    UniquePersonList getUniquePersonList();

    /**
     * Returns a modifiable view of the jobs list with greater exposure warranted by its use in
     * Jobs-related commands. USE WITH CAUTION.
     */
    UniqueJobList getUniqueJobList();

    /**
     * Returns a modifiable view of the applications list with greater exposure warranted by its use in
     * Applications-related commands. USE WITH CAUTION.
     */
    UniqueApplicationList getUniqueApplicationList();
}
