package seedu.address.storage;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.ApplicationsManager;
import seedu.address.model.ReadOnlyApplicationsManager;
import seedu.address.model.application.Application;

/**
 * An Immutable ApplicationsManager that is serializable to JSON format.
 */
@JsonRootName(value = "applicationsmanager")
class JsonSerializableApplicationsManager {

    public static final String MESSAGE_DUPLICATE_APPLICATION = "Applications list contains duplicate application(s).";

    private final List<JsonAdaptedApplication> applications = new ArrayList<>();

    /**
     * Constructs a {@code JsonSerializableApplicationsManager} with the given
     * applications.
     */
    @JsonCreator
    public JsonSerializableApplicationsManager(
            @JsonProperty("applications") List<JsonAdaptedApplication> applications) {
        this.applications.addAll(applications);
    }

    /**
     * Converts a given {@code ReadOnlyApplicationsManager} into this class for
     * Jackson use.
     *
     * @param source future changes to this will not affect the created
     *               {@code JsonSerializableApplicationsManager}.
     */
    public JsonSerializableApplicationsManager(ReadOnlyApplicationsManager source) {
        applications.addAll(source.getUniqueApplicationList().asUnmodifiableObservableList().stream()
                .map(JsonAdaptedApplication::new).toList());
    }

    /**
     * Converts this applications manager into the model's
     * {@code ApplicationsManager} object.
     *
     * @throws IllegalValueException if there were any data constraints violated.
     */
    public ApplicationsManager toModelType() throws IllegalValueException {
        ApplicationsManager applicationsManager = new ApplicationsManager();

        for (JsonAdaptedApplication jsonAdaptedApplication : applications) {
            Application application = jsonAdaptedApplication.toModelType();
            if (applicationsManager.hasApplication(application)) {
                throw new IllegalValueException(MESSAGE_DUPLICATE_APPLICATION);
            }
            applicationsManager.addApplication(application);
        }

        return applicationsManager;
    }
}
