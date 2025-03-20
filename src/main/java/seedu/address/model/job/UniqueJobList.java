package seedu.address.model.job;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Iterator;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.model.application.Application;
import seedu.address.model.job.exceptions.DuplicateJobException;
import seedu.address.model.job.exceptions.JobNotFoundException;

/**
 * A list of jobs that enforces uniqueness between its elements and does not
 * allow nulls. A job is considered unique by comparing using
 * {@code Job#equals(Object)}.
 * Supports a minimal set of list operations.
 */
public class UniqueJobList implements Iterable<Job> {

    private final ObservableList<Job> internalList = FXCollections.observableArrayList();

    /**
     * Returns true if the list contains an equivalent job as the given argument.
     */
    public boolean contains(Job toCheck) {
        requireNonNull(toCheck);
        return this.internalList.stream().anyMatch(toCheck::equals);
    }

    /**
     * Adds a job to the list. The job must not already exist in the list.
     */
    public void add(Job toAdd) {
        requireNonNull(toAdd);
        if (contains(toAdd)) {
            throw new DuplicateJobException();
        }
        this.internalList.add(toAdd);
    }

    /**
     * Replaces the job {@code target} in the list with {@code editedJob}.
     * {@code target} must exist in the list. The job identity of {@code editedJob}
     * must not be the same as another existing job in the list.
     */
    public void setJob(Job target, Job editedJob) {
        requireAllNonNull(target, editedJob);

        int index = this.internalList.indexOf(target);
        if (index == -1) {
            throw new JobNotFoundException();
        }

        if (!target.equals(editedJob) && contains(editedJob)) {
            throw new DuplicateJobException();
        }

        this.internalList.set(index, editedJob);
    }

    /**
     * Removes the equivalent job from the list. The job must exist in the list.
     */
    public void remove(Job toRemove) {
        requireNonNull(toRemove);
        if (!this.internalList.remove(toRemove)) {
            throw new JobNotFoundException();
        }
    }

    public void setJobs(UniqueJobList replacement) {
        requireNonNull(replacement);
        this.internalList.setAll(replacement.internalList);
    }

    /**
     * Replaces the contents of this list with {@code jobs}. {@code jobs} must not
     * contain duplicate jobs.
     */
    public void setJobs(List<Job> jobs) {
        requireAllNonNull(jobs);
        if (!areJobsUnique(jobs)) {
            throw new DuplicateJobException();
        }
        this.internalList.setAll(jobs);
    }

    public ObservableList<Job> searchJobByTitleAndCompany(JobTitle jobTitle, JobCompany jobCompany) {
        requireAllNonNull(jobTitle, jobCompany);
        List<Job> jobFilteredList = this.internalList.stream()
                .filter(job -> job.getJobTitle().equals(jobTitle)
                        && job.getJobCompany().equals(jobCompany)).toList();
        return FXCollections.unmodifiableObservableList(FXCollections.observableList(jobFilteredList));
    }

    /**
     * Returns the backing list as an unmodifiable {@code ObservableList}.
     */
    public ObservableList<Job> asUnmodifiableObservableList() {
        return FXCollections.unmodifiableObservableList(this.internalList);
    }

    @Override
    public Iterator<Job> iterator() {
        return this.internalList.iterator();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof UniqueJobList otherUniqueJobList)) {
            return false;
        }
        return this.internalList.equals(otherUniqueJobList.internalList);
    }

    @Override
    public int hashCode() {
        return this.internalList.hashCode();
    }

    @Override
    public String toString() {
        return internalList.toString();
    }

    /**
     * Returns true if {@code jobs} contains only unique jobs.
     */
    private boolean areJobsUnique(List<Job> jobs) {
        for (int i = 0; i < jobs.size() - 1; i++) {
            for (int j = i + 1; j < jobs.size(); j++) {
                if (jobs.get(i).equals(jobs.get(j))) {
                    return false;
                }
            }
        }
        return true;
    }
}
