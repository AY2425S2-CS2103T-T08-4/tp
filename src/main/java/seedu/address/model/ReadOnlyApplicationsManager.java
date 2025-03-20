package seedu.address.model;

import seedu.address.model.application.UniqueApplicationList;

/**
 * Unmodifiable view of the applications manager
 */
public interface ReadOnlyApplicationsManager {
    /**
     * Returns an unmodifiable view of the applications list. This list will not
     * contain any duplicate applications.
     */
    UniqueApplicationList getUniqueApplicationList();
}
