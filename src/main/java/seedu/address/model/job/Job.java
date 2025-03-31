package seedu.address.model.job;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Objects;

/**
 * Represents a job with a job title and additional properties.
 */
public class Job {
    private final JobTitle jobTitle;
    private final JobRounds jobRounds;
    private final JobSkills jobSkills;

    /**
     * Constructs a Job with the specified job title and properties.
     *
     * @param jobTitle  The title of the job.
     * @param jobRounds The rounds of the job.
     * @param jobSkills The requisite skills for the job.
     */
    public Job(JobTitle jobTitle, JobRounds jobRounds, JobSkills jobSkills) {
        requireAllNonNull(jobTitle, jobRounds, jobSkills);
        this.jobTitle = jobTitle;
        this.jobRounds = jobRounds;
        this.jobSkills = jobSkills;
    }

    /**
     * Returns the job title of this job.
     *
     * @return The job title.
     */
    public JobTitle getJobTitle() {
        return this.jobTitle;
    }

    /**
     * Returns the job rounds of this job.
     *
     * @return The job rounds.
     */
    public JobRounds getJobRounds() {
        return this.jobRounds;
    }

    /**
     * Returns the job skills of this job.
     *
     * @return The job skills.
     */
    public JobSkills getJobSkills() {
        return this.jobSkills;
    }

    /**
     * Returns true if both jobs have the same title and company. This defines a
     * weaker notion of equality between two jobs.
     */
    public boolean isSameJob(Job otherJob) {
        return otherJob != null && otherJob.getJobTitle().equals(this.jobTitle);
    }

    /**
     * Returns true if this job is the same as the specified object.
     *
     * @param other The object to compare to.
     * @return True if the objects are equal, false otherwise.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof Job otherJob)) {
            return false;
        }
        return this.jobTitle.equals(otherJob.jobTitle) && this.jobRounds.equals(otherJob.jobRounds)
                && this.jobSkills.equals(otherJob.jobSkills);
    }

    /**
     * Returns the hash code of this job.
     *
     * @return The hash code.
     */
    @Override
    public int hashCode() {
        return Objects.hash(this.jobTitle, this.jobRounds, this.jobSkills);
    }
}
