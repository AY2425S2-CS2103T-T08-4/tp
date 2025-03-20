package seedu.address.storage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.application.Application;
import seedu.address.model.application.ApplicationStatus;
import seedu.address.model.application.UniqueApplicationList;
import seedu.address.model.job.UniqueJobList;
import seedu.address.model.person.UniquePersonList;

import java.util.List;

/**
 * Jackson-friendly version of {@link Application}.
 */
public class JsonAdaptedApplication {
    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Application's %s field is missing!";

    private final JsonAdaptedPerson applicant;
    private final JsonAdaptedJob job;
    private final Integer applicationStatus;
    private final List<JsonAdaptedPerson> personList;
    private final List<JsonAdaptedJob> jobList;
    private final List<JsonAdaptedApplication> applicationList;

    /**
     * Constructs a {@code JsonAdaptedApplication} with the given application
     * details.
     */
    @JsonCreator
    public JsonAdaptedApplication(@JsonProperty("applicant") JsonAdaptedPerson applicant,
                                  @JsonProperty("job") JsonAdaptedJob job,
                                  @JsonProperty("applicationStatus") Integer applicationStatus,
                                  @JsonProperty("personList") List<JsonAdaptedPerson> personList,
                                  @JsonProperty("jobList") List<JsonAdaptedJob> jobList,
                                  @JsonProperty("applicationList") List<JsonAdaptedApplication> applicationList) {
        this.applicant = applicant;
        this.job = job;
        this.applicationStatus = applicationStatus;
        this.personList = personList;
        this.jobList = jobList;
        this.applicationList = applicationList;
    }

    /**
     * Converts a given {@code Application} into this class for Jackson use.
     */
    public JsonAdaptedApplication(Application source) {
        this.applicant = new JsonAdaptedPerson(source.getApplicant());
        this.job = new JsonAdaptedJob(source.getJob());
        this.applicationStatus = source.getApplicationStatus().applicationStatus();
        this.personList = source.getPersonList().asUnmodifiableObservableList().stream()
                .map(JsonAdaptedPerson::new).toList();
        this.jobList = source.getJobList().asUnmodifiableObservableList().stream()
                .map(JsonAdaptedJob::new).toList();
        this.applicationList = source.getApplicationList().asUnmodifiableObservableList().stream()
                .map(JsonAdaptedApplication::new).toList();
    }

    /**
     * Converts this Jackson-friendly adapted application object into the model's
     * {@code Application} object.
     */
    public Application toModelType() throws IllegalValueException {
        if (this.applicationStatus == null) {
            throw new IllegalValueException(
                    String.format(MISSING_FIELD_MESSAGE_FORMAT, ApplicationStatus.class.getSimpleName()));
        }
        if (!ApplicationStatus.isValidApplicationStatus(applicationStatus)) {
            throw new IllegalValueException(ApplicationStatus.MESSAGE_CONSTRAINTS);
        }
        final ApplicationStatus modelApplicationStatus = new ApplicationStatus(applicationStatus);
        UniqueApplicationList applicationList = new UniqueApplicationList();
        for (JsonAdaptedApplication adaptedApplication : this.applicationList) {
            applicationList.add(adaptedApplication.toModelType());
        }
        // Convert lists to the corresponding UniqueList types
        UniquePersonList uniquePersonList = new UniquePersonList();
        for (JsonAdaptedPerson adaptedPerson : this.personList) {
            uniquePersonList.add(adaptedPerson.toModelType());
        }

        UniqueJobList uniqueJobList = new UniqueJobList();
        for (JsonAdaptedJob adaptedJob : this.jobList) {
            uniqueJobList.add(adaptedJob.toModelType());
        }

        UniqueApplicationList uniqueApplicationList = new UniqueApplicationList();
        for (JsonAdaptedApplication adaptedApplication : this.applicationList) {
            uniqueApplicationList.add(adaptedApplication.toModelType());
        }

        return new Application(this.applicant.toModelType().getPhone(),
                this.job.toModelType().getJobTitle(), this.job.toModelType().getJobCompany(),
                modelApplicationStatus, uniquePersonList, uniqueJobList, uniqueApplicationList);
    }
}
