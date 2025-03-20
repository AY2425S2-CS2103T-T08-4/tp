package seedu.address.model.application;

import java.util.function.Predicate;

/**
 * Tests that an application's status matches the specified value.
 */
public class ApplicationStatusPredicate implements Predicate<Application> {
    private final int status;

    public ApplicationStatusPredicate(int status) {
        this.status = status;
    }

    @Override
    public boolean test(Application application) {
        return application.applicationStatus().applicationStatus == status;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof ApplicationStatusPredicate)) {
            return false;
        }
        ApplicationStatusPredicate otherPredicate = (ApplicationStatusPredicate) other;
        return status == otherPredicate.status;
    }
} 