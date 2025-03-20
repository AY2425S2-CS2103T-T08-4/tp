package seedu.address.model.application;

import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents the status of a job application. The status represents which round
 * has been completed up to.
 */
public record ApplicationStatus(int applicationStatus) implements Comparable<ApplicationStatus> {
    public static final String MESSAGE_CONSTRAINTS = "Application status should be a non-negative integer";

    /**
     * The status must be a non-negative integer. 0 means applied but no rounds
     * completed yet. A positive integer n means completed up to round n.
     */
    public static final String VALIDATION_REGEX = "^[0-9]\\d*$";

    /**
     * Constructs an {@code ApplicationStatus}.
     *
     * @param applicationStatus A valid application status.
     */
    public ApplicationStatus {
        checkArgument(isValidApplicationStatus(applicationStatus), MESSAGE_CONSTRAINTS);
    }

    /**
     * Returns true if a given integer is a valid application status.
     *
     * @param test The integer to validate.
     * @return True if the given integer is a valid application status.
     */
    public static boolean isValidApplicationStatus(int test) {
        return test >= 0;
    }

    /**
     * Returns the string representation of the application status.
     *
     * @return The string representation.
     */
    @Override
    public String toString() {
        return String.valueOf(this.applicationStatus);
    }

    /**
     * Returns true if this application status is the same as the specified object.
     *
     * @param other The object to compare to.
     * @return True if the objects are equal, false otherwise.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof ApplicationStatus otherApplicationStatus)) {
            return false;
        }
        return this.applicationStatus == otherApplicationStatus.applicationStatus;
    }

    /**
     * Returns the hash code of this application status.
     *
     * @return The hash code.
     */
    @Override
    public int hashCode() {
        return Integer.hashCode(applicationStatus);
    }

    @Override
    public int compareTo(ApplicationStatus other) {
        return Integer.compare(this.applicationStatus, other.applicationStatus);
    }
}
