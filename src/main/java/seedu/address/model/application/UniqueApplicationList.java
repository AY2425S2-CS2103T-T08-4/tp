package seedu.address.model.application;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Iterator;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.model.application.exceptions.ApplicationNotFoundException;
import seedu.address.model.application.exceptions.DuplicateApplicationException;
import seedu.address.model.job.JobCompany;
import seedu.address.model.job.JobTitle;
import seedu.address.model.person.Phone;

/**
 * A list of applications that enforces uniqueness between its elements and does
 * not allow nulls. An application is considered unique by comparing using
 * {@code Application#equals(Object)}.
 * Supports a minimal set of list operations.
 */
public class UniqueApplicationList implements Iterable<Application> {
    private final ObservableList<Application> internalList = FXCollections.observableArrayList();

    public UniqueApplicationList() {
    }

    /**
     * Returns true if the list contains an equivalent application as the given
     * argument.
     */
    public boolean contains(Application toCheck) {
        requireNonNull(toCheck);
        return internalList.stream().anyMatch(app -> app.equals(toCheck));
    }

    /**
     * Adds an application to the list. The application must not already exist in the list.
     */
    public void add(Application toAdd) {
        requireNonNull(toAdd);
        if (contains(toAdd)) {
            throw new DuplicateApplicationException();
        }
        internalList.add(toAdd);
    }

    /**
     * Replaces the application {@code target} in the list with
     * {@code editedApplication}. {@code target} must exist in the list. The
     * application identity of {@code editedApplication} must not be the same as
     * another existing application in the list.
     */
    public void setApplication(Application target, Application editedApplication) {
        requireAllNonNull(target, editedApplication);

        int index = internalList.indexOf(target);
        if (index == -1) {
            throw new ApplicationNotFoundException();
        }

        if (!target.equals(editedApplication) && contains(editedApplication)) {
            throw new DuplicateApplicationException();
        }
        internalList.set(index, editedApplication);
    }

    /**
     * Removes the equivalent application from the list. The application must exist
     * in the list.
     */
    public void remove(Application toRemove) {
        requireNonNull(toRemove);
        if (!internalList.remove(toRemove)) {
            throw new ApplicationNotFoundException();
        }
    }

    public void setApplications(UniqueApplicationList replacement) {
        requireNonNull(replacement);
        internalList.setAll(replacement.internalList);
    }

    /**
     * Replaces the contents of this list with {@code applications}.
     * {@code applications} must not contain duplicate applications.
     */
    public void setApplications(List<Application> applications) {
        requireAllNonNull(applications);
        if (!areApplicationsUnique(applications)) {
            throw new DuplicateApplicationException();
        }
        this.internalList.setAll(applications);
    }

    /**
     * Returns the backing list as an unmodifiable {@code ObservableList}.
     */
    public ObservableList<Application> asUnmodifiableObservableList() {
        return FXCollections.unmodifiableObservableList(this.internalList);
    }

    public ObservableList<Application> searchApplications(Phone phone, JobTitle jobTitle, JobCompany jobCompany,
                                                          ApplicationStatus applicationStatus) {
        requireAllNonNull(phone, jobTitle, jobCompany, applicationStatus);
        List<Application> filteredList = this.internalList.stream()
                .filter(application -> application.getApplicant().getPhone().equals(phone))
                .filter(application -> application.getJob().getJobTitle().equals(jobTitle))
                .filter(application -> application.getJob().getJobCompany().equals(jobCompany))
                .filter(application -> application.getApplicationStatus().equals(applicationStatus)).toList();
        return FXCollections.unmodifiableObservableList(FXCollections.observableList(filteredList));
    }

    @Override
    public Iterator<Application> iterator() {
        return this.internalList.iterator();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        // instanceof handles nulls
        if (!(other instanceof UniqueApplicationList otherUniqueApplicationList)) {
            return false;
        }
        return this.internalList.equals(otherUniqueApplicationList.internalList);
    }

    @Override
    public int hashCode() {
        return this.internalList.hashCode();
    }

    @Override
    public String toString() {
        return this.internalList.toString();
    }

    /**
     * Returns true if {@code applications} contains only unique applications.
     */
    private boolean areApplicationsUnique(List<Application> applications) {
        for (int i = 0; i < applications.size() - 1; i++) {
            for (int j = i + 1; j < applications.size(); j++) {
                if (applications.get(i).equals(applications.get(j))) {
                    return false;
                }
            }
        }
        return true;
    }
}
