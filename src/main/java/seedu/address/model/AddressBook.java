package seedu.address.model;

import static java.util.Objects.requireNonNull;

import java.util.Objects;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.application.Application;
import seedu.address.model.application.UniqueApplicationList;
import seedu.address.model.job.Job;
import seedu.address.model.job.UniqueJobList;
import seedu.address.model.person.Person;
import seedu.address.model.person.UniquePersonList;

/**
 * Wraps all data at the address-book level Duplicates are not allowed (by
 * Person::isSamePerson comparison and Job::equals comparison)
 */
public class AddressBook implements ReadOnlyAddressBook {

    private final UniquePersonList persons;
    private final UniqueJobList jobs;
    private final UniqueApplicationList applications;

    /*
     * The 'unusual' code block below is a non-static initialization block,
     * sometimes used to avoid duplication between constructors. See
     * https://docs.oracle.com/javase/tutorial/java/javaOO/initial.html
     *
     * Note that non-static init blocks are not recommended to use. There are other
     * ways to avoid duplication among constructors.
     */
    {
        persons = new UniquePersonList();
        jobs = new UniqueJobList();
        applications = new UniqueApplicationList();
    }

    public AddressBook() {
    }

    /**
     * Creates an AddressBook using the Persons in the {@code toBeCopied}
     */
    public AddressBook(ReadOnlyAddressBook toBeCopied) {
        this();
        resetData(toBeCopied);
    }

    //// list overwrite operations

    /**
     * Replaces the contents of the person list with {@code persons}.
     * {@code persons} must not contain duplicate persons.
     */
    public void setPersons(UniquePersonList persons) {
        this.persons.setPersons(persons);
    }

    /**
     * Replaces the contents of the job list with {@code jobs}. {@code jobs} must
     * not contain duplicate jobs.
     */
    public void setJobs(UniqueJobList jobs) {
        this.jobs.setJobs(jobs);
    }

    /**
     * Replaces the contents of the application list with {@code applications}. {@code applications} must
     * not contain duplicate applications.
     */
    public void setApplications(UniqueApplicationList applications) {
        this.applications.setApplications(applications);
    }

    /**
     * Resets the existing data of this {@code AddressBook} with {@code newData}.
     */
    public void resetData(ReadOnlyAddressBook newData) {
        requireNonNull(newData);

        this.setPersons(newData.getUniquePersonList());
        this.setJobs(newData.getUniqueJobList());
        this.setApplications(newData.getUniqueApplicationList());
    }

    //// person-level operations

    /**
     * Returns true if a person with the same identity as {@code person} exists in
     * the address book.
     */
    public boolean hasPerson(Person person) {
        requireNonNull(person);
        return persons.contains(person);
    }

    /**
     * Adds a person to the address book. The person must not already exist in the
     * address book.
     */
    public void addPerson(Person p) {
        persons.add(p);
    }

    /**
     * Replaces the given person {@code target} in the list with
     * {@code editedPerson}. {@code target} must exist in the address book. The
     * person identity of {@code editedPerson} must not be the same as another
     * existing person in the address book.
     */
    public void setPerson(Person target, Person editedPerson) {
        requireNonNull(editedPerson);

        persons.setPerson(target, editedPerson);
    }

    /**
     * Removes {@code key} from this {@code AddressBook}. {@code key} must exist in
     * the address book.
     */
    public void removePerson(Person key) {
        persons.remove(key);
    }

    //// job-level operations

    /**
     * Returns true if a job with the same identity as {@code job} exists in the
     * address book.
     */
    public boolean hasJob(Job job) {
        requireNonNull(job);
        return jobs.contains(job);
    }

    /**
     * Adds a job to the address book. The job must not already exist in the address
     * book.
     */
    public void addJob(Job job) {
        jobs.add(job);
    }

    /**
     * Replaces the given job {@code target} in the list with {@code editedJob}.
     * {@code target} must exist in the address book. The job identity of
     * {@code editedJob} must not be the same as another existing job in the address
     * book.
     */
    public void setJob(Job target, Job editedJob) {
        requireNonNull(editedJob);

        jobs.setJob(target, editedJob);
    }

    /**
     * Removes {@code key} from this {@code AddressBook}. {@code key} must exist in
     * the address book.
     */
    public void removeJob(Job key) {
        jobs.remove(key);
    }

    public boolean hasApplication(Application application) {
        requireNonNull(application);
        return applications.contains(application);
    }

    public void addApplication(Application application) {
        applications.add(application);
    }

    public void setApplication(Application target, Application editedApplication) {
        requireNonNull(editedApplication);
        applications.setApplication(target, editedApplication);
    }

    public void removeApplication(Application key) {
        applications.remove(key);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("persons", persons).toString();
    }

    @Override
    public UniquePersonList getUniquePersonList() {
        return this.persons;
    }

    @Override
    public UniqueJobList getUniqueJobList() {
        return this.jobs;
    }

    @Override
    public UniqueApplicationList getUniqueApplicationList() {
        return this.applications;
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof AddressBook otherAddressBook)) {
            return false;
        }
        return this.persons.equals(otherAddressBook.persons) && this.jobs.equals(otherAddressBook.jobs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(persons, jobs);
    }
}
