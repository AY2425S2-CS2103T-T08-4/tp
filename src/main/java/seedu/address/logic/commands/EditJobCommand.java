package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_JOB_ROUNDS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_JOB_TITLE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SKILL;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.CollectionUtil;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.application.Application;
import seedu.address.model.job.Job;
import seedu.address.model.job.JobRounds;
import seedu.address.model.job.JobTitle;
import seedu.address.model.skill.Skill;

/**
 * Edits the details of an existing person in the address book.
 */
public class EditJobCommand extends Command {
    public static final String COMMAND_WORD = "editjob";
    public static final String BRIEF_MESSAGE_USAGE =
            "INDEX (must be a positive integer) " + "[" + PREFIX_JOB_TITLE + "JOB_TITLE] "
            + "[" + PREFIX_JOB_ROUNDS + "NUMBER_OF_ROUNDS] " + "[" + PREFIX_SKILL + "SKILL] ";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the details of the job identified "
            + "by the index number used in the displayed job list. "
            + "Existing values will be overwritten by the input values.\n"
            + "Parameters: " + BRIEF_MESSAGE_USAGE
            + "Existing values will be overwritten by the input values. Must provide at least 1 field.\n"
            + "Parameters: INDEX (must be a positive integer > 0) " + "[" + PREFIX_JOB_TITLE + "JOB_TITLE] "
            + "[" + PREFIX_JOB_ROUNDS + "NUMBER_OF_ROUNDS (1-" + JobRounds.MAX_ROUNDS + ")] "
            + "[" + PREFIX_SKILL + "SKILL]...\n"
            + "Example: " + COMMAND_WORD + " 2 " + PREFIX_JOB_ROUNDS + "4";

    public static final String MESSAGE_EDIT_JOB_SUCCESS = "Edited Job: %1$s";
    public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided.";
    public static final String MESSAGE_DUPLICATE_JOB = "This job already exists in the address book.";
    public static final String MESSAGE_INVALID_APPLICATION_STATUS = "Some applicants are already at an "
            + "application stage higher than the number of rounds in the edited job.";

    private final Index index;
    private final EditJobDescriptor editJobDescriptor;
    private final Logger logger = LogsCenter.getLogger(EditJobCommand.class);
    /**
     * @param index             of the person in the filtered person list to edit
     * @param editJobDescriptor details to edit the person with
     */
    public EditJobCommand(Index index, EditJobDescriptor editJobDescriptor) {
        requireNonNull(index);
        requireNonNull(editJobDescriptor);

        this.index = index;
        this.editJobDescriptor = new EditJobDescriptor(editJobDescriptor);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        // Check that we're in job view
        if (!model.isInJobView()) {
            throw new CommandException(Messages.MESSAGE_NOT_IN_JOB_VIEW);
        }

        List<Job> lastShownList = model.getFilteredJobList();

        if (this.index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_JOB_DISPLAYED_INDEX);
        }

        Job jobToEdit = lastShownList.get(this.index.getZeroBased());
        Job editedJob = createEditedJob(jobToEdit, editJobDescriptor);

        if (!jobToEdit.isSameJob(editedJob) && model.hasJob(editedJob)) {
            throw new CommandException(MESSAGE_DUPLICATE_JOB);
        }

        List<Application> existingApplications = model.getApplicationsByJob(jobToEdit);
        for (Application app : existingApplications) {
            if (app.getApplicationStatus().applicationStatus > editedJob.getJobRounds().jobRounds) {
                logger.info("Application status is greater than job rounds");
                throw new CommandException(MESSAGE_INVALID_APPLICATION_STATUS);
            }
        }

        model.setJob(jobToEdit, editedJob);
        model.resetFilteredJobList();
        return CommandResult.withRefreshApplications(
            String.format(MESSAGE_EDIT_JOB_SUCCESS, Messages.format(editedJob)));
    }

    /**
     * Creates and returns a {@code Person} with the details of {@code jobToEdit}
     * edited with {@code editJobDescriptor}.
     */
    private static Job createEditedJob(Job jobToEdit, EditJobDescriptor editJobDescriptor) {
        assert jobToEdit != null;
        JobTitle updatedJobTitle = editJobDescriptor.getJobTitle().orElse(jobToEdit.getJobTitle());
        JobRounds updatedJobRounds = editJobDescriptor.getJobRounds().orElse(jobToEdit.getJobRounds());
        Set<Skill> updatedSkills = editJobDescriptor.getSkills().orElse(jobToEdit.getSkills());

        return new Job(updatedJobTitle, updatedJobRounds, updatedSkills);
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof EditJobCommand otherEditJobCommand)) {
            return false;
        }
        return this.index.equals(otherEditJobCommand.index)
                && this.editJobDescriptor.equals(otherEditJobCommand.editJobDescriptor);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("index", this.index).add("editPersonDescriptor", this.editJobDescriptor)
                .toString();
    }

    /**
     * Stores the details to edit the person with. Each non-empty field value will
     * replace the corresponding field value of the person.
     */
    public static class EditJobDescriptor {
        private JobTitle jobTitle;
        private JobRounds jobRounds;
        private Set<Skill> skills;

        public EditJobDescriptor() {
        }

        /**
         * Copy constructor.
         * A defensive copy of {@code EditJobDescriptor} is used internally.
         */
        public EditJobDescriptor(EditJobDescriptor toCopy) {
            setJobTitle(toCopy.jobTitle);
            setJobRounds(toCopy.jobRounds);
            setSkills(toCopy.skills);
        }

        /**
         * Returns true if at least one field is edited.
         */
        public boolean isAnyFieldEdited() {
            return CollectionUtil.isAnyNonNull(this.jobTitle, this.jobRounds, this.skills);
        }

        public void setJobTitle(JobTitle jobTitle) {
            this.jobTitle = jobTitle;
        }

        public Optional<JobTitle> getJobTitle() {
            return Optional.ofNullable(this.jobTitle);
        }

        public void setJobRounds(JobRounds jobRounds) {
            this.jobRounds = jobRounds;
        }

        public Optional<JobRounds> getJobRounds() {
            return Optional.ofNullable(jobRounds);
        }

        /**
         * Sets {@code skills} to this object's {@code skills}. A defensive copy of
         * {@code skills} is used internally.
         */
        public void setSkills(Set<Skill> skills) {
            this.skills = (skills != null) ? new HashSet<>(skills) : null;
        }

        /**
         * Returns an unmodifiable skill set, which throws
         * {@code UnsupportedOperationException} if modification is attempted. Returns
         * {@code Optional#empty()} if {@code skills} is null.
         */
        public Optional<Set<Skill>> getSkills() {
            return (this.skills != null) ? Optional.of(Collections.unmodifiableSet(skills)) : Optional.empty();
        }

        @Override
        public boolean equals(Object other) {
            // short circuit if same object
            if (other == this) {
                return true;
            }

            // instanceof handles nulls
            if (!(other instanceof EditJobDescriptor)) {
                return false;
            }

            EditJobDescriptor otherEditJobDescriptor = (EditJobDescriptor) other;
            return Objects.equals(this.jobTitle, otherEditJobDescriptor.jobTitle)
                    && Objects.equals(this.jobRounds, otherEditJobDescriptor.jobRounds)
                    && Objects.equals(this.skills, otherEditJobDescriptor.skills);
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this).add("job title", this.jobTitle)
                    .add("job rounds", this.jobRounds)
                    .add("job skills", this.skills).toString();
        }
    }
}
